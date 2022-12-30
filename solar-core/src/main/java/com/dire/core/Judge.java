package com.dire.core;

public enum Judge {

    YES(1), NO(0);

    private Integer value;

    Judge(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
