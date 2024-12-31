package com.github.atdushi.common.error;

import static com.github.atdushi.common.error.ErrorType.NOT_FOUND;

public class NotFoundException extends AppException {
    public NotFoundException(String msg) {
        super(msg, NOT_FOUND);
    }
}