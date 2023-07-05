package com.jingjiang.gb28181.configuration;

import org.springframework.stereotype.Component;

import javax.sip.*;
import javax.sip.header.Header;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

@Component
public class SipResponseUtils {

    private final SipFactory sipFactory;
    private final MessageFactory messageFactory;
    private final SipProviderProxy sipProviderProxy;

    public SipResponseUtils(SipFactory sipFactory, MessageFactory messageFactory, SipProviderProxy sipProviderProxy) {
        this.sipFactory = sipFactory;
        this.messageFactory = messageFactory;
        this.sipProviderProxy = sipProviderProxy;
    }


    public void ok(RequestEvent requestEvent, Header... headers) {
        Request request = requestEvent.getRequest();
        try {
            Response response = messageFactory.createResponse(Response.OK, request);
            for (Header header : headers) {
                response.addHeader(header);
            }

            ViaHeader viaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
            SipProvider sipProvider = sipProviderProxy.getSipProvider(viaHeader.getTransport());
            sipProvider.getNewServerTransaction(request).sendResponse(response);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            e.printStackTrace();
        }
    }

}
