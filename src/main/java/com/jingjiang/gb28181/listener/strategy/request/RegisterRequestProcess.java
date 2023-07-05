package com.jingjiang.gb28181.listener.strategy.request;

import com.jingjiang.gb28181.configuration.SipResponseUtils;
import com.jingjiang.gb28181.domain.Device;
import com.jingjiang.gb28181.domain.DeviceRepository;
import com.jingjiang.gb28181.listener.strategy.RequestProcessStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

@Component
public class RegisterRequestProcess implements RequestProcessStrategy {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegisterRequestProcess.class);

    private final DeviceRepository deviceRepository;
    private final SipResponseUtils sipResponseUtils;
    private final HeaderFactory headerFactory;

    public RegisterRequestProcess(DeviceRepository deviceRepository, SipResponseUtils sipResponseUtil, HeaderFactory headerFactory) {
        this.deviceRepository = deviceRepository;
        this.sipResponseUtils = sipResponseUtil;
        this.headerFactory = headerFactory;
    }

    @Override
    public String getMethod() {
        return Request.REGISTER;
    }

    @Override
    public void process(RequestEvent requestEvent) throws PeerUnavailableException, ParseException {
        Request request = requestEvent.getRequest();

        ContactHeader contactHeader = (ContactHeader) request.getHeader(ContactHeader.NAME);
        Address contactAddress = contactHeader.getAddress();
        URI contactAddressURI = contactAddress.getURI();

        String host = StringUtils.substringBetween(contactAddressURI.toString(), "sip:", "@");

        LOGGER.debug("收到来自 {} 的REGISTER请求", host);

        DateHeader dateHeader = headerFactory.createDateHeader(Calendar.getInstance(Locale.ENGLISH));
        sipResponseUtils.ok(requestEvent, dateHeader, request.getHeader(ContactHeader.NAME), request.getExpires());

        ViaHeader viaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);

        String received = viaHeader.getReceived();
        int rPort = viaHeader.getRPort();
        String transport = viaHeader.getTransport();


        Device build = Device.builder().host(host).received(received).rPort(rPort).protocol(transport).build();

        Optional<Device> optional = deviceRepository.findByHost(host);
        optional.ifPresentOrElse(device -> {
            build.setId(device.getId());
            deviceRepository.save(build);
        }, () -> deviceRepository.save(build));


    }

}
