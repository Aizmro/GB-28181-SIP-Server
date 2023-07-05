package com.jingjiang.gb28181.media.hook.domain;

import lombok.Data;

@Data
public class StreamChanged {
    /**
     * 流注册或注销
     */
    private Boolean regist;
    /**
     * 应用名
     */
    private String app;
    /**
     * 服务器id
     */
    private String mediaServerId;
    /**
     * 协议
     */
    private String schema;
    /**
     * 流id
     */
    private String stream;
}
