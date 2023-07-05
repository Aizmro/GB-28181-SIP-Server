package com.jingjiang.gb28181.media;

public class ImeiParser {

    public static String getHost(String imei) {
        return imei.split("@")[0];
    }

    public static String getChannel(String imei) {
        return imei.split("@")[1];
    }

}
