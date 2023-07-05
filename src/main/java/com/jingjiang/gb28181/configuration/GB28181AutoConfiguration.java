package com.jingjiang.gb28181.configuration;

import com.jingjiang.gb28181.configuration.properties.MediaServerProperties;
import com.jingjiang.gb28181.configuration.properties.SipServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipStack;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import java.util.Properties;

@Configuration
@ComponentScan("com.jingjiang.gb28181")
@Import({SipServerInitializerConfiguration.class, SipServerClosedConfiguration.class})
@EnableConfigurationProperties({SipServerProperties.class, MediaServerProperties.class})
public class GB28181AutoConfiguration {

    @Bean
    public SipFactory sipFactory() {
        SipFactory sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
        return sipFactory;
    }

    @Bean
    public SipStack sipStack(@Autowired SipFactory sipFactory) throws PeerUnavailableException {
        Properties properties = new Properties();
        properties.setProperty("javax.sip.STACK_NAME", "com.jingjiang.gb28181.configuration.SipConfiguration");
        return sipFactory.createSipStack(properties);
    }

    @Bean
    public AddressFactory addressFactory(@Autowired SipFactory sipFactory) throws PeerUnavailableException {
        return sipFactory.createAddressFactory();
    }

    @Bean
    public HeaderFactory headerFactory(@Autowired SipFactory sipFactory) throws PeerUnavailableException {
        return sipFactory.createHeaderFactory();
    }

    @Bean
    public MessageFactory messageFactory(@Autowired SipFactory sipFactory) throws PeerUnavailableException {
        return sipFactory.createMessageFactory();
    }

}
