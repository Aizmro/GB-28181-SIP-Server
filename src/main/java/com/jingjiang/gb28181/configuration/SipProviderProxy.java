package com.jingjiang.gb28181.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sip.SipProvider;

@Component
public class SipProviderProxy {

    private final ApplicationContext applicationContext;

    public SipProviderProxy(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public SipProvider getSipProvider(String protocol) {
       return applicationContext.getBean(protocol + SipProvider.class.getName(), SipProvider.class);
    }

}
