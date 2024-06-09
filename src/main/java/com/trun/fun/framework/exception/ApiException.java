package com.trun.fun.framework.exception;

import com.trun.fun.framework.emuns.ErrorCodeEnum;
import com.trun.fun.framework.model.ErrorCode;

/**
 * <p>
 * API 业务异常类
 * </p>
 *
 * @author Mawei
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    public ApiException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.msg());
        this.errorCode = errorCodeEnum.convert();
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getError());
        this.errorCode = errorCode;

    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
