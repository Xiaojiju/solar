package com.dire.guard.enums;

public enum Identifier {

    DEFAULT_USERNAME(0),
    MOBILE_PHONE(1),
    EMAIL(2),
    WEIXIN(3),
    WEIBO(4),
    QQ(5),
    DOUYIN(6),
    ALIPAY(7);

    private final int value;

    Identifier(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
