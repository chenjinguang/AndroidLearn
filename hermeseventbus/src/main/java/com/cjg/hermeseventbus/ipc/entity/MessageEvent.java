package com.cjg.hermeseventbus.ipc.entity;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2021/2/20
 * @修改人和其它信息
 */
public class MessageEvent {


    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
