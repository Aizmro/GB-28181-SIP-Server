package com.jingjiang.gb28181.media.hook;

import com.jingjiang.gb28181.application.SipAppService;
import com.jingjiang.gb28181.configuration.SipProviderProxy;
import com.jingjiang.gb28181.configuration.SipRequestHeaderProvider;
import com.jingjiang.gb28181.domain.Stream;
import com.jingjiang.gb28181.domain.StreamRepository;
import com.jingjiang.gb28181.media.ImeiParser;
import com.jingjiang.gb28181.media.api.MediaApi;
import com.jingjiang.gb28181.media.api.domain.OpenRtpServer;
import com.jingjiang.gb28181.media.hook.domain.*;
import gov.nist.javax.sip.header.CallID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/index/hook")
public class MediaHook {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediaHook.class);

    private final MediaApi mediaApi;
    private final SipProviderProxy sipProviderProxy;
    private final StreamRepository streamRepository;
    private final SipAppService gb28181ApplicationService;
    private final SipRequestHeaderProvider sipRequestHeaderProvider;

    public MediaHook(MediaApi mediaApi, SipProviderProxy sipProviderProxy, StreamRepository streamRepository, SipAppService gb28181ApplicationService, SipRequestHeaderProvider sipRequestHeaderProvider) {
        this.mediaApi = mediaApi;
        this.sipProviderProxy = sipProviderProxy;
        this.streamRepository = streamRepository;
        this.gb28181ApplicationService = gb28181ApplicationService;
        this.sipRequestHeaderProvider = sipRequestHeaderProvider;
    }

    /**
     * 流量统计事件
     */
    @PostMapping("/on_flow_report")
    public void on_flow_report() {

    }

    /**
     * 访问http文件服务器上hls之外的文件时触发。
     */
    @PostMapping("/on_http_access")
    public void on_http_access() {

    }

    /**
     * 播放器鉴权事件
     */
    @PostMapping("/on_play")
    public void on_play() {

    }

    /**
     * rtsp/rtmp/rtp推流鉴权事件。
     *
     * @return
     */
    @PostMapping("/on_publish")
    public Map<String, Object> on_publish(@RequestBody Publish publish) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "success");
        map.put("enable_hls", false);
        map.put("enable_mp4", false);
        map.put("enable_rtsp", true);
        map.put("enable_rtmp", false);
        map.put("enable_ts", false);
        map.put("enable_fmp4", true);
        map.put("enable_audio", false);
        return map;

    }

    /**
     * 录制mp4完成后通知事件
     */
    @PostMapping("/on_record_mp4")
    public void on_record_mp4(@RequestBody RecordMp4 recordMp4) {
        System.out.println(recordMp4);
        mediaApi.closeRtpServer(recordMp4.getStream());
    }

    /**
     * 该rtsp流是否开启rtsp专用方式的鉴权事件，开启后才会触发on_rtsp_auth事件。
     */
    @PostMapping("/on_rtsp_realm")
    public void on_rtsp_realm() {

    }

    /**
     * rtsp专用的鉴权事件，先触发on_rtsp_realm事件然后才会触发on_rtsp_auth事件。
     */
    @PostMapping("/on_rtsp_auth")
    public void on_rtsp_auth() {

    }

    /**
     * shell登录鉴权，ZLMediaKit提供简单的telnet调试方式
     */
    @PostMapping("/on_shell_login")
    public void on_shell_login() {

    }

    /**
     * rtsp/rtmp流注册或注销时触发此事件
     */
    @PostMapping("/on_stream_changed")
    public Map<String, Object> on_stream_changed(@RequestBody StreamChanged streamChanged) throws InvalidArgumentException, SipException, ParseException {
        // 持久化缓存
        LOGGER.info("流-{}-事件 流ID: {} 协议: {}", streamChanged.getRegist() ? "注册" : "注销", streamChanged.getStream(), streamChanged.getSchema());

        if (!streamChanged.getRegist()) {
            String stream = streamChanged.getStream();
            String[] split = stream.split("@");
            String host = split[0];
            String channelId = split[1];

            gb28181ApplicationService.stop(host, channelId);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "success");
        return map;
    }

    /**
     * 流无人观看时事件，用户可以通过此事件选择是否关闭无人看的流。
     */
    @PostMapping("/on_stream_none_reader")
    public Map<String, Object> on_stream_none_reader(@RequestBody StreamNoneReader streamNoneReader) throws InvalidArgumentException, SipException, ParseException {
        LOGGER.info("无人观看流事件 流ID: {} 协议: {}", streamNoneReader.getStream(), streamNoneReader.getSchema());

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("close", true);

        String stream = streamNoneReader.getStream();
        String[] split = stream.split("@");
        String host = split[0];
        String channelId = split[1];

        gb28181ApplicationService.stop(host, channelId);
        return map;
    }

    /**
     * 流未找到事件
     *
     * @param streamNotFound 回调参数
     */
    @PostMapping("/on_stream_not_found")
    public void on_stream_not_found(@RequestBody StreamNotFound streamNotFound) throws InvalidArgumentException, ParseException, SipException, InterruptedException {
        String host = ImeiParser.getHost(streamNotFound.getStream());
        String channel = ImeiParser.getChannel(streamNotFound.getStream());

        OpenRtpServer openRtpServer = mediaApi.openRtpServer(streamNotFound.getStream());
        // 判断 是否正确获取到 RTP推流端口
        if (openRtpServer.getCode() == 0) {
            gb28181ApplicationService.play(host, channel, openRtpServer.getPort());
        }
    }

    /**
     * 服务器启动事件，可以用于监听服务器崩溃重启；此事件对回复不敏感。
     */
    @PostMapping("/on_server_started")
    public void on_server_started() {

    }

    /**
     * 服务器定时上报时间，上报间隔可配置，默认10s上报一次
     */
    @PostMapping("/on_server_keepalive")
    public void on_server_keepalive() {

    }


}
