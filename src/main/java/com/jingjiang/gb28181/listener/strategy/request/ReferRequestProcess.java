package com.jingjiang.gb28181.listener.strategy.request;

import com.jingjiang.gb28181.listener.strategy.RequestProcessStrategy;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Component
public class ReferRequestProcess implements RequestProcessStrategy {

    @Override
    public String getMethod() {
        return Request.REFER;
    }

    @Override
    public void process(RequestEvent requestEvent) {
        System.out.println("处理REFER请求");
    }

}
