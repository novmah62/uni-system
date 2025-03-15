package kma.ktlt.post.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum AppErrorCode {
    USER_NOT_EXISTED(1,"Người dùng không tồn tại" ,HttpStatus.BAD_REQUEST),
    USERNAME_IS_SET(2,"Bạn chỉ có thể thay đổi tên người dùng 1 lần" ,HttpStatus.BAD_REQUEST ),
    USERNAME_IS_USED(3,"Tên ngừoi dùng đã được sử dụng" ,HttpStatus.BAD_REQUEST ),
    UNAUTHENTICATED(9999, "Xác thực không thành công", HttpStatus.UNAUTHORIZED),
    INVALID_USERNAME_PASSWORD(4,"Tài khoản hoặc mật khẩu không chính xác" ,HttpStatus.BAD_REQUEST ),
    PASSWORDS_INVALID(5,"Mật khẩu không thoả mãn chính sách" , HttpStatus.BAD_REQUEST ),
    UNCATEGORIZED_EXCEPTION(8888, "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(777, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    RESOURCE_NOT_FOUND(6, "Không tìm thấy tài nguyên", HttpStatus.NOT_FOUND),
    PRIMARY_MEDIA_MISSING(7, "Thiếu media chính", HttpStatus.BAD_REQUEST),
    INVALID_RELATED_MEDIA_COUNT(8, "Số lượng media liên quan không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_FILE_CONTENT(9, "Nội dung tệp không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_HOMESTAY_STEP(10, "Bước homestay không hợp lệ", HttpStatus.BAD_REQUEST),
    TOO_MANY_REQUEST(11, "Quá nhiều yêu cầu, vui lòng thử lại sau", HttpStatus.TOO_MANY_REQUESTS),
    BOOKING_INVALID(12,"Booking không hợp lệ" , HttpStatus.BAD_REQUEST),
    BOOKING_NOT_FOUND(13, "Booking không tồn tại " , HttpStatus.BAD_REQUEST ),
    EXIST_BOOKING(14,"Homestay hiện đang có booking đang chờ xử lý" ,HttpStatus.BAD_REQUEST ),
    EXIST_AVAILABILITY_BOOKING(15,"Homestay availability  hiện đang có booking đang chờ xử lý" ,HttpStatus.BAD_REQUEST ),
    ALREADY_LIKED(16,"Already liked" , HttpStatus.BAD_REQUEST);



    AppErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }



    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
