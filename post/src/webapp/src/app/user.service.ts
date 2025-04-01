import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseURL = "http://localhost:8082/api/v1/posts";
  private baseURL1 = "http://localhost:8081/api/v1/users";
  private baseURL2 = "http://localhost:8082/api/v1/comments"

  constructor(private http: HttpClient,
              private router: Router,
  ) {
  }

  getNewFeed(request: any): Observable<any> {
    return this.http.get(`${this.baseURL}`, request);
  }

  getCmtByIDPost(id: any): Observable<any> {
    return this.http.get(`${this.baseURL}/${id}/comments`);
  }

  upPost(content: any) {
    return this.http.post<any>(`${this.baseURL}`, content);
  }

  auth(): Observable<any>  {
    return this.http.get(`${this.baseURL1}`);
  }

  addComt(createCommentRequest: any) {
    return this.http.post<any>(`${this.baseURL2}`, createCommentRequest);
  }
}
