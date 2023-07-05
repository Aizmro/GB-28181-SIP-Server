package com.jingjiang.gb28181.configuration;

import com.jingjiang.gb28181.configuration.properties.SipServerProperties;
import com.jingjiang.gb28181.listener.SipListenerImpl;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import javax.sip.*;
import java.util.TooManyListenersException;

public class SipServerInitializerConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private final SipStack sipStack;
    private final SipListenerImpl sipListener;
    private final SipServerProperties sipServerProperties;
    private final ApplicationContext applicationContext;

    public SipServerInitializerConfiguration(SipStack sipStack, SipListenerImpl sipListener, SipServerProperties sipServerProperties, ApplicationContext applicationContext) {
        this.sipStack = sipStack;
        this.sipListener = sipListener;
        this.sipServerProperties = sipServerProperties;
        this.applicationContext = applicationContext;
    }


    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        try {
            for (String s : sipServerProperties.getProtocol()) {
                ListeningPoint listeningPoint = sipStack.createListeningPoint(sipServerProperties.getIp(), sipServerProperties.getPort(), s);
                SipProvider sipProvider = sipStack.createSipProvider(listeningPoint);
                sipProvider.addSipListener(sipListener);
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                defaultListableBeanFactory.registerSingleton(s + SipProvider.class.getName(), sipProvider);
            }
        } catch (TransportNotSupportedException | InvalidArgumentException | TooManyListenersException | ObjectInUseException e) {
            e.printStackTrace();
        }
    }

}
