package com.jingjiang.gb28181.listener.strategy.request;

import com.jingjiang.gb28181.configuration.SipResponseUtils;
import com.jingjiang.gb28181.domain.Device;
import com.jingjiang.gb28181.domain.DeviceRepository;
import com.jingjiang.gb28181.listener.strategy.RequestProcessStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.address.SipURI;
import javax.sip.header.FromHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import java.util.Optional;

@Component
public class MessageRequestProcess implements RequestProcessStrategy {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageRequestProcess.class);


    private final SipResponseUtils sipResponseUtils;
    private final DeviceRepository deviceRepository;

    public MessageRequestProcess(SipResponseUtils sipResponseUtils, DeviceRepository deviceRepository) {
        this.sipResponseUtils = sipResponseUtils;
        this.deviceRepository = deviceRepository;
    }


    @Override
    public String getMethod() {
        return Request.MESSAGE;
    }

    @Override
    public void process(RequestEvent requestEvent) {
        Request request = requestEvent.getRequest();

        FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
        LOGGER.debug("收到来自 {} 的MESSAGE请求", fromHeader.getAddress());
        ViaHeader reqViaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
        SipURI uri = (SipURI) fromHeader.getAddress().getURI();

        Optional<Device> optional = deviceRepository.findDeviceByHost(uri.getUser());
        Device build = Device.builder().host(uri.getUser()).received(reqViaHeader.getReceived()).rPort(reqViaHeader.getRPort()).protocol(reqViaHeader.getTransport()).build();

        optional.ifPresentOrElse(device -> {
            build.setId(device.getId());
            deviceRepository.save(build);
        }, () -> deviceRepository.save(build));

        sipResponseUtils.ok(requestEvent);
    }

}
