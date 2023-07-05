package com.jingjiang.gb28181.media.hook.domain;

public class StreamNotFound {

    private String mediaServerId;

    private String app;

    private String id;

    private String ip;

    private String params;

    private int port;

    private String schema;

    private String stream;

    private String vhost;

    public void setMediaServerId(String mediaServerId) {
        this.mediaServerId = mediaServerId;
    }

    public String getMediaServerId() {
        return this.mediaServerId;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getApp() {
        return this.app;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return this.params;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getStream() {
        return this.stream;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getVhost() {
        return this.vhost;
    }

    @Override
    public String toString() {
        return "StreamNotFound{" +
                "mediaServerId='" + mediaServerId + '\'' +
                ", app='" + app + '\'' +
                ", id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", params='" + params + '\'' +
                ", port=" + port +
                ", schema='" + schema + '\'' +
                ", stream='" + stream + '\'' +
                ", vhost='" + vhost + '\'' +
                '}';
    }
}
