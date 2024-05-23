package xyz.notagardener.common.error.exception;

import lombok.Getter;
import xyz.notagardener.common.error.code.ExceptionCode;

@Getter
public class HasSameUsernameException extends RuntimeException {
    private ExceptionCode code;
    private String message;

    public HasSameUsernameException(ExceptionCode code, String username) {
        super(code.getDescription());
        this.code = code;
        this.message = username + "은/는 이미 사융중인 아이디예요.";
    }
}
