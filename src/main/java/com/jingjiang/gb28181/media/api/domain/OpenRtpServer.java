package com.jingjiang.gb28181.media.api.domain;

public class OpenRtpServer {

    private Integer code;

    private Integer port;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "OpenRtpServer{" +
                "code=" + code +
                ", port=" + port +
                ", msg='" + msg + '\'' +
                '}';
    }
}
