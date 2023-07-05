package com.jingjiang.gb28181.scheduled;

import com.fasterxml.jackson.databind.JsonNode;
import com.jingjiang.gb28181.application.SipAppService;
import com.jingjiang.gb28181.domain.Stream;
import com.jingjiang.gb28181.domain.StreamRepository;
import com.jingjiang.gb28181.media.api.MediaApi;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class MediaListSyncTask {

    private final MediaApi mediaApi;
    private final SipAppService sipAppService;
    private final StreamRepository streamRepository;

    public MediaListSyncTask(MediaApi mediaApi, SipAppService sipAppService, StreamRepository streamRepository) {
        this.mediaApi = mediaApi;
        this.sipAppService = sipAppService;
        this.streamRepository = streamRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void scheduledTask() throws InvalidArgumentException, ParseException, SipException {
        JsonNode mediaList = mediaApi.getMediaList();

        JsonNode dataList = mediaList.get("data");

        if (dataList != null) {
            List<String> stream = StreamSupport.stream(dataList.spliterator(), false)
                    .map(item -> item.get("stream").asText())
                    .distinct()
                    .toList();

            List<Stream> all = streamRepository.findAll();

            all.removeIf(item -> stream.contains(item.getHost() + "@" + item.getChannelId()));

            for (Stream item : all) {
                sipAppService.stop(item.getHost(), item.getChannelId());
                streamRepository.delete(item);
            }

        }


    }

}
