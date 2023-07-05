package com.jingjiang.gb28181.configuration;

import com.jingjiang.gb28181.configuration.properties.SipServerProperties;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Component
public class SipRequestHeaderProvider {

    private final SipFactory sipFactory;
    private final SipServerProperties sipServerProperties;

    public SipRequestHeaderProvider(SipFactory sipFactory, SipServerProperties sipServerProperties) {
        this.sipFactory = sipFactory;
        this.sipServerProperties = sipServerProperties;
    }

    public Request createInviteRequest(String address, Integer port, String channelId, String fromTag, String viaTag, String content, String protocol, CallIdHeader callIdHeader) throws ParseException, InvalidArgumentException, PeerUnavailableException {
        //请求行
        SipURI requestLine = sipFactory.createAddressFactory().createSipURI(sipServerProperties.getId(), address + ":" + port);
        //via
        List<ViaHeader> viaHeaders = Collections.singletonList(
                sipFactory.createHeaderFactory().createViaHeader(sipServerProperties.getIp(), sipServerProperties.getPort(), protocol, viaTag)
        );
        //from
        SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(sipServerProperties.getId(), sipServerProperties.getDomain());
        Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
        FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, fromTag);
        //to
        SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(channelId, channelId.substring(0, 10));
        Address toAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
        ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toAddress, null);
        //Forwards
        MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);
        //ceq
        CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(1L, Request.INVITE);
        Request request = sipFactory.createMessageFactory().createRequest(requestLine, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
        SipURI concatSipURI = sipFactory.createAddressFactory().createSipURI(channelId, address + ":5060");
        Address concatAddress = sipFactory.createAddressFactory().createAddress(concatSipURI);
        request.addHeader(sipFactory.createHeaderFactory().createContactHeader(concatAddress));
        // Subject
        SubjectHeader subjectHeader = sipFactory.createHeaderFactory().createSubjectHeader(String.format("%s:%s,%s:%s", channelId, "1", sipServerProperties.getId(), 1));
        request.addHeader(subjectHeader);
        ContentTypeHeader contentTypeHeader = sipFactory.createHeaderFactory().createContentTypeHeader("APPLICATION", "SDP");
        request.setContent(content, contentTypeHeader);
        return request;
    }

    public Request createMessageRequest(String address, Integer port, String channelId, String fromTag, String viaTag, String content, String protocol, CallIdHeader callIdHeader) throws ParseException, InvalidArgumentException, PeerUnavailableException {
        // sipuri
        SipURI requestURI = sipFactory.createAddressFactory().createSipURI(sipServerProperties.getId(), address + ":" + port);
        // via
        List<ViaHeader> viaHeaders = Collections.singletonList(
                sipFactory.createHeaderFactory().createViaHeader(sipServerProperties.getIp(), sipServerProperties.getPort(), protocol, viaTag)
        );

        // from
        SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(sipServerProperties.getId(), sipServerProperties.getDomain());
        Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
        FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, fromTag);
        // to
        SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(channelId, channelId.substring(0, 10));
        Address toAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
        ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toAddress, null);

        // Forwards
        MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);
        // ceq
        CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(1L, Request.MESSAGE);

        Request request = sipFactory.createMessageFactory().createRequest(requestURI, Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader,
                toHeader, viaHeaders, maxForwards);
        ContentTypeHeader contentTypeHeader = sipFactory.createHeaderFactory().createContentTypeHeader("Application", "MANSCDP+xml");
        request.setContent(content, contentTypeHeader);
        return request;
    }

    public Request createByteRequest(String address, Integer port, String channelId, String fromTag, String viaTag, String protocol, CallIdHeader callIdHeader) throws ParseException, InvalidArgumentException, PeerUnavailableException {
        //请求行
        SipURI requestURI = sipFactory.createAddressFactory().createSipURI(sipServerProperties.getId(), address + ":" + port);
        // via
        List<ViaHeader> viaHeaders = Collections.singletonList(
                sipFactory.createHeaderFactory().createViaHeader(sipServerProperties.getIp(), sipServerProperties.getPort(), protocol, viaTag)
        );
        //from
        SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(sipServerProperties.getId(), sipServerProperties.getDomain());
        Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
        FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, fromTag);
        //to
        SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(channelId, channelId.substring(0, 10));
        Address toAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
        ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toAddress, "f569d024");

        //Forwards
        MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);

        //ceq
        CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(2L, Request.BYE);

        Request request = sipFactory.createMessageFactory().createRequest(requestURI, Request.BYE, callIdHeader, cSeqHeader, fromHeader,
                toHeader, viaHeaders, maxForwards);

        SipURI concatSipURI = sipFactory.createAddressFactory().createSipURI(channelId, address + ":5060");
        Address concatAddress = sipFactory.createAddressFactory().createAddress(concatSipURI);
        request.addHeader(sipFactory.createHeaderFactory().createContactHeader(concatAddress));

        return request;
    }

}
