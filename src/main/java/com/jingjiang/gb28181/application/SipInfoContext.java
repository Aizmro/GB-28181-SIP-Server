package com.jingjiang.gb28181.application;

import com.jingjiang.gb28181.configuration.properties.MediaServerProperties;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class SipInfoContext {

    private final MediaServerProperties mediaServerProperties;

    public SipInfoContext(MediaServerProperties mediaServerProperties) {
        this.mediaServerProperties = mediaServerProperties;
    }

    /**
     * 云台控制
     *
     * @param channelId 设备ID
     * @param cmdStr    控制命令
     * @return 指令上下文
     */
    public String PTZControlContext(String channelId, String cmdStr) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        writer.println("<?xml version=\"1.0\" ?>");
        writer.println("<Control>");
        writer.println("<CmdType>DeviceControl</CmdType>");
        writer.println("<SN>" + (int) ((Math.random() * 9 + 1) * 100000) + "</SN>");
        writer.println("<DeviceID>" + channelId + "</DeviceID>");
        writer.println("<PTZCmd>" + cmdStr + "</PTZCmd>");
        writer.println("<Info>");
        writer.println("</Info>");
        writer.println("</Control>");
        return stringWriter.toString();
    }

    /**
     * 实时播放
     *
     * @param ssrc      RTP报文流的的 ssrc 标识
     * @param channelId 设备ID
     * @param port      推送端口
     * @return 指令上下文
     */
    public String playContext(Integer ssrc, String channelId, Integer port) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        writer.println("v=0");
        writer.println("o=" + channelId + " 0 0 IN IP4 " + mediaServerProperties.getAddress());
        writer.println("s=Play");
        writer.println("c=IN IP4 " + mediaServerProperties.getAddress());
        writer.println("t=0 0");
        writer.println("m=video " + port + " RTP/AVP 96 98 97");
        writer.println("a=recvonly");
        writer.println("a=rtpmap:96 PS/90000");
        writer.println("a=rtpmap:97 MPEG4/90000");
        writer.println("a=rtpmap:98 H264/90000");
        writer.println("y=0" + ssrc);
        return stringWriter.toString();
    }
}
