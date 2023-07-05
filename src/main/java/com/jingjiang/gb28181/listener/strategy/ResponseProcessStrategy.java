package com.jingjiang.gb28181.listener.strategy;

import javax.sip.ResponseEvent;

public interface ResponseProcessStrategy {

    String getMethod();

    void process(ResponseEvent responseEvent);

}
