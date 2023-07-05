package com.jingjiang.gb28181;

import com.jingjiang.gb28181.application.SipAppService;
import com.jingjiang.gb28181.configuration.SipProviderProxy;
import com.jingjiang.gb28181.configuration.SipRequestHeaderProvider;
import com.jingjiang.gb28181.media.api.MediaApi;
import com.jingjiang.gb28181.media.api.domain.OpenRtpServer;
import gov.nist.javax.sip.header.CallID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.sip.*;
import javax.sip.address.AddressFactory;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class GB28181SIPServerApplication {

    private final MediaApi mediaApi;
    private final SipProviderProxy sipProviderProxy;
    private final SipRequestHeaderProvider sipRequestHeaderProvider;
    private final AddressFactory addressFactory;

    private final SipAppService gb28181ApplicationService;

    public GB28181SIPServerApplication(MediaApi mediaApi, SipProviderProxy sipProviderProxy, SipRequestHeaderProvider sipRequestHeaderProvider, AddressFactory addressFactory, SipAppService gb28181ApplicationService) {
        this.mediaApi = mediaApi;
        this.sipProviderProxy = sipProviderProxy;
        this.sipRequestHeaderProvider = sipRequestHeaderProvider;
        this.addressFactory = addressFactory;
        this.gb28181ApplicationService = gb28181ApplicationService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GB28181SIPServerApplication.class, args);
    }


    @GetMapping("/play")
    public void play(String host, String channelId, Integer port) throws InvalidArgumentException, ParseException, SipException, InterruptedException {
        gb28181ApplicationService.play(host, channelId, port);
    }

    @GetMapping("/PTZControl")
    public void PTZControl(String host, String channelId, String cmd, int speed) throws InvalidArgumentException, ParseException, SipException {
        gb28181ApplicationService.PTZControl(host, channelId, cmd, speed);
    }

    @GetMapping("/bye")
    public void bye(String host, String channelId) throws InvalidArgumentException, ParseException, SipException {
        gb28181ApplicationService.stop(host, channelId);
    }
}
