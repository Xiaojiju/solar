/*
 * Copyright 2022 一块小饼干(莫杨)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dire.core;

/**
 * 统一业务代码
 * @author 一块小饼干
 */
public enum ResultCode implements CodeExpression {

    OK(0, "OK"),
    ERROR(50000, "Server Error"),
    PAYLOAD_TOO_LARGE(50001,  "Request payload is too large"),
    INVALID_PARAMETERS(50002, "Request invalid parameters"),
    FORBIDDEN(50003, "forbidden"),
    UNAUTHORIZED(50004, "unauthorized");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
