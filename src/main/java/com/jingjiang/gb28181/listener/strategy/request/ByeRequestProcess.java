package com.jingjiang.gb28181.listener.strategy.request;

import com.jingjiang.gb28181.listener.strategy.RequestProcessStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Component
public class ByeRequestProcess implements RequestProcessStrategy {

    private final static Logger logger = LoggerFactory.getLogger(ByeRequestProcess.class);


    @Override
    public String getMethod() {
        return Request.BYE;
    }

    @Override
    public void process(RequestEvent requestEvent) {
        logger.debug("处理BYE请求");
    }

}
