package com.jingjiang.gb28181.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sip", ignoreInvalidFields = true)
public class SipServerProperties {

    private String ip = "0.0.0.0";

    private Integer port = 5060;

    private String id;

    private String domain;

    private String password;

    private String[] protocol = new String[]{"TCP", "UDP"};


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getProtocol() {
        return protocol;
    }

    public void setProtocol(String[] protocol) {
        this.protocol = protocol;
    }

}
