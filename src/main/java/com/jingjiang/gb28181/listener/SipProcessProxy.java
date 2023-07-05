package com.jingjiang.gb28181.listener;

import com.jingjiang.gb28181.listener.strategy.RequestProcessStrategy;
import com.jingjiang.gb28181.listener.strategy.ResponseProcessStrategy;
import org.springframework.stereotype.Component;

import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.header.CSeqHeader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SipProcessProxy {

    private final ConcurrentHashMap<String, RequestProcessStrategy> requestStrategy = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ResponseProcessStrategy> responseStrategy = new ConcurrentHashMap<>();


    public SipProcessProxy(List<RequestProcessStrategy> requestProcessStrategies, List<ResponseProcessStrategy> responseProcessStrategies) {
        requestProcessStrategies.forEach(
                requestProcessStrategy -> requestStrategy.put(requestProcessStrategy.getMethod(), requestProcessStrategy)
        );
        responseProcessStrategies.forEach(
                responseProcessStrategy -> responseStrategy.put(responseProcessStrategy.getMethod(), responseProcessStrategy)
        );
    }

    public void process(RequestEvent requestEvent) throws Exception {
        RequestProcessStrategy requestProcessStrategy = requestStrategy.get(requestEvent.getRequest().getMethod());
        requestProcessStrategy.process(requestEvent);
    }

    public void process(ResponseEvent responseEvent) {
        Map<String, String> map = new HashMap<>();
        map.put("INVITE", "ACK");
        CSeqHeader header = (CSeqHeader) responseEvent.getResponse().getHeader("CSeq");
        ResponseProcessStrategy responseProcessStrategy = responseStrategy.get(map.get(header.getMethod()));
        responseProcessStrategy.process(responseEvent);
    }

}
