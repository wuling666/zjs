package com.yks.oms.pojo;

import java.io.Serializable;

/**
 * @author sunshen
 * @date 2018-01-31
 */
public class BaseDataVO implements Serializable{
	
    /**
     * 响应业务状态
     * */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应中的数据
     * */
    private Object func;

    /**
     * 用户名
     * */
    private String name;

    /**
     * 昵称
     * */
    private String nike;

    /**
     * 票据
     * */
    private String ticket;

    /**
     * user
     */
    private String user;

    /**
     * 是否超级管理员
     * */
    private Boolean admin;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getFunc() {
        return func;
    }

    public void setFunc(Object func) {
        this.func = func;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNike() {
        return nike;
    }

    public void setNike(String nike) {
        this.nike = nike;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "BaseDataVO{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", func=" + func +
                ", name='" + name + '\'' +
                ", nike='" + nike + '\'' +
                ", ticket='" + ticket + '\'' +
                ", user='" + user + '\'' +
                ", admin=" + admin +
                '}';
    }
}
