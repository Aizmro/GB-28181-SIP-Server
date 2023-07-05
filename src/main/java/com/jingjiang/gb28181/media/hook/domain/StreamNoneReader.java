package com.jingjiang.gb28181.media.hook.domain;

public class StreamNoneReader {

    private String app;

    private String schema;

    private String stream;

    private String vhost;

    private String mediaServerId;


    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getMediaServerId() {
        return mediaServerId;
    }

    public void setMediaServerId(String mediaServerId) {
        this.mediaServerId = mediaServerId;
    }

    @Override
    public String toString() {
        return "StreamNoneReader{" +
                "app='" + app + '\'' +
                ", schema='" + schema + '\'' +
                ", stream='" + stream + '\'' +
                ", vhost='" + vhost + '\'' +
                ", mediaServerId='" + mediaServerId + '\'' +
                '}';
    }
}
