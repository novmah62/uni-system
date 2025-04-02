import {Component, OnInit} from '@angular/core';
import {UserService} from '../user.service';
import {Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MessageService} from 'primeng/api';
import {CookieService} from 'ngx-cookie-service';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {ToastModule} from 'primeng/toast';

@Component({
  selector: 'app-profile',
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ToastModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  createPostRequest: any = {};
  request: any = {};
  posts: any = []
  isPopupOpen = false;
  newComment = '';
  comments: any[] = [];
  selectedPost: any = null;
  createCommentRequest: any = {}
  cuUser: any = {};
  isLiked: boolean = false;
  createLikeRequest: any = {};
  constructor(private userService: UserService,
              private router: Router,
              private cookieService: CookieService,
              private messageService: MessageService,
              private spinner: NgxSpinnerService) {
  }

  upPost() {
    this.userService.upPost(this.createPostRequest).subscribe(res => {
      console.log(res)
    })
  }

  ngOnInit(): void {
    this.showSuccess("aaa")
    this.userService.getNewFeed(this.request).subscribe(res => {
      this.posts = res.data.items;
      this.posts.forEach((item: { userInfo: { firstName: any; }; }) => {
        console.log(item.userInfo.firstName)
      })

    })
    this.userService.auth().subscribe(res => {
      this.cuUser = res.data;
    })
  }

  getCmtByPost(id: any) {
    this.userService.getCmtByIDPost(id).subscribe(res => {
      console.log(res)
    })
  }

  timeAgo(postTime: string | Date) {
    const now = new Date();

    // Xử lý nếu chuỗi thời gian có microseconds (6 chữ số thập phân)
    if (typeof postTime === "string" && postTime.includes(".")) {
      postTime = postTime.substring(0, 23) + "Z"; // Cắt xuống đến milliseconds
    }

    const postDate = new Date(postTime);
    const diffMs = now.getTime() - postDate.getTime(); // Chênh lệch tính bằng milliseconds
    const diffMinutes = Math.floor(diffMs / 60000); // Chuyển sang phút
    const diffHours = Math.floor(diffMinutes / 60);
    const diffDays = Math.floor(diffHours / 24);
    const diffWeeks = Math.floor(diffDays / 7);
    const diffMonths = Math.floor(diffDays / 30);
    const diffYears = Math.floor(diffDays / 365);

    if (diffMinutes < 1) return "Vừa xong";
    if (diffMinutes < 60) return `${diffMinutes} phút trước`;
    if (diffHours < 24) return `${diffHours} giờ trước`;
    if (diffDays < 7) return `${diffDays} ngày trước`;
    if (diffWeeks < 4) return `${diffWeeks} tuần trước`;
    if (diffMonths < 12) return `${diffMonths} tháng trước`;
    return `${diffYears} năm trước`;
  }

  openPopup(post: any) {
    this.selectedPost = post;
    this.comments = [];
    this.isPopupOpen = true;
    this.userService.getCmtByIDPost(post.postId).subscribe(res => {
      this.comments = res.data.items;
    })
  }

  closePopup() {
    this.isPopupOpen = false;
    this.selectedPost = null;
  }

  addComment(post: any) {
    if (this.createCommentRequest.content != null && this.createCommentRequest.content.trim()) {
      this.createCommentRequest.postId = post.postId
      this.userService.addComt(this.createCommentRequest).subscribe(res => {
        if (res.data) {
          this.showSuccess("Bình luận thành công")
          this.closePopup();
        }
        else {
          this.showFail("Bình luận thất bại")
        }

      })
    }
  }
  showSuccess(content: any){
    this.messageService.add({severity: 'success', summary: 'Thành công', detail: content});

  }
  showFail(content: any) {
    this.messageService.add({severity: 'error', summary: 'Thành công', detail: content});

  }
  like(id: any){
    this.createLikeRequest.postId = id;
    this.userService.like(this.createLikeRequest).subscribe(res => {
      this.isLiked = true;
    })
  }
  unlike(id: any){
    this.createLikeRequest.postId = id;
    this.userService.like(this.createLikeRequest).subscribe(res => {
      this.isLiked = false;
    })
  }

}
