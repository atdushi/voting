package com.github.atdushi.common.error;

import static com.github.atdushi.common.error.ErrorType.BAD_REQUEST;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg, BAD_REQUEST);
    }
}