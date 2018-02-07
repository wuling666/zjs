package com.yks.oms.pojo;

public class LogVO {
    private String param;
    private String message;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LogVO{" +
                "param='" + param + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
