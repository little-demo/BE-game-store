package antran.project.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "Người dùng đã tồn tại!", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username phải có ít nhất {min} kí tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password phải có ít nhất {min} kí tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại!", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Tuổi của bạn phải lớn hơn hoặc bằng {min}", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1009, "Role not found", HttpStatus.NOT_FOUND),
    USER_DISABLED(1010, "Người dùng đã bị vô hiệu hóa", HttpStatus.FORBIDDEN),
    OLD_PASSWORD_INCORRECT(1011, "Mật khẩu cũ không chính xác", HttpStatus.BAD_REQUEST),
    LISTING_NOT_FOUND(2001, "Thẻ được đăng bán không tồn tại", HttpStatus.NOT_FOUND),
    CANNOT_CANCEL_LISTING(2002, "Không thể hủy bán vì thẻ bài đã được mua", HttpStatus.BAD_REQUEST),
    USER_CARD_NOT_FOUND(2003, "Không tìm thấy thẻ bài của người dùng", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_BALANCE(2004, "Số dư không đủ để thực hiện giao dịch", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_CARD_QUANTITY(2005, "Số lượng thẻ bài không đủ để thực hiện giao dịch", HttpStatus.BAD_REQUEST),
    LISTING_ALREADY_CANCELLED(2006, "Thẻ bài đã được hủy bán trước đó", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(3001, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    NOTIFICATION_NOT_FOUND(4001, "Thông báo không tồn tại", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
