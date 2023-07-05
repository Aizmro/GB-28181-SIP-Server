package com.jingjiang.gb28181.listener.strategy;

import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import java.text.ParseException;

public interface RequestProcessStrategy {

    String getMethod();

    void process(RequestEvent requestEvent) throws PeerUnavailableException, ParseException;

}
