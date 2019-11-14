package com.tedu.base.auth;

public enum LoginType {
	EMPLOYEE("Employee"),  CUSTOM("Custom"), TEACHER("Teacher");

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
