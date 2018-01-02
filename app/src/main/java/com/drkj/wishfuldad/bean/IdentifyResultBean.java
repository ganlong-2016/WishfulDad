package com.drkj.wishfuldad.bean;

import java.util.List;

/**
 * Created by ganlong on 2017/12/25.
 */

public class IdentifyResultBean {

    /**
     * status : 1
     * message : OK
     * data : []
     */

    private int status;
    private String message;
    private List<?> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
