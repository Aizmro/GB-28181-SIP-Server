package com.jingjiang.gb28181.listener.strategy.request;

import com.jingjiang.gb28181.listener.strategy.RequestProcessStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Component
public class InfoRequestProcess implements RequestProcessStrategy {

    private final static Logger logger = LoggerFactory.getLogger(InfoRequestProcess.class);


    @Override
    public String getMethod() {
        return Request.INFO;
    }

    @Override
    public void process(RequestEvent requestEvent) {
        logger.debug("处理INFO请求");
    }

}
