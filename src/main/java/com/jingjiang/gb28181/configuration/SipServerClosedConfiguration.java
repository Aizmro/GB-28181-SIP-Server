package com.jingjiang.gb28181.configuration;

import com.jingjiang.gb28181.application.SipAppService;
import com.jingjiang.gb28181.domain.Stream;
import com.jingjiang.gb28181.domain.StreamRepository;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;

import java.util.List;

public class SipServerClosedConfiguration implements ApplicationListener<ContextClosedEvent> {

    private final SipAppService sipAppService;
    private final StreamRepository streamRepository;

    public SipServerClosedConfiguration(SipAppService sipAppService, StreamRepository streamRepository) {
        this.sipAppService = sipAppService;
        this.streamRepository = streamRepository;
    }

    @Override
    @SneakyThrows
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        List<Stream> all = streamRepository.findAll();

        for (Stream stream : all) {
            sipAppService.stop(stream.getHost(), stream.getChannelId());
        }

    }
}
