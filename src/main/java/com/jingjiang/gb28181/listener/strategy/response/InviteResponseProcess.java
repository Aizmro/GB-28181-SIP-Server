package com.jingjiang.gb28181.listener.strategy.response;

import com.jingjiang.gb28181.listener.strategy.ResponseProcessStrategy;
import gov.nist.javax.sip.ResponseEventExt;
import gov.nist.javax.sip.stack.SIPDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

@Component
public class InviteResponseProcess implements ResponseProcessStrategy {

    private final static Logger logger = LoggerFactory.getLogger(InviteResponseProcess.class);

    @Override
    public String getMethod() {
        return "ACK";
    }

    @Override
    public void process(ResponseEvent responseEvent) {
        logger.debug("回复ACK请求");
        try {
            Response response = responseEvent.getResponse();
            if (response.getStatusCode() == Response.OK) {
                ResponseEventExt event = (ResponseEventExt) responseEvent;

                SIPDialog dialog = (SIPDialog) responseEvent.getDialog();
                CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);

                Request reqAck = dialog.createAck(cseq.getSeqNumber());
                SipURI requestURI = (SipURI) reqAck.getRequestURI();
                requestURI.setHost(event.getRemoteIpAddress());
                requestURI.setPort(event.getRemotePort());
                reqAck.setRequestURI(requestURI);
                logger.info("向 " + event.getRemoteIpAddress() + ":" + event.getRemotePort() + "回复ack");
                dialog.sendAck(reqAck);
            }
        } catch (InvalidArgumentException | ParseException | SipException e) {
            e.printStackTrace();
        }
    }

}
