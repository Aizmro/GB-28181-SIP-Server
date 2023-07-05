package com.jingjiang.gb28181.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sip.*;

@Component
public class SipListenerImpl implements SipListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(SipListenerImpl.class);


    private final SipProcessProxy sipProcessProxy;

    public SipListenerImpl(SipProcessProxy sipProcessProxy) {
        this.sipProcessProxy = sipProcessProxy;
    }


    @Override
    public void processRequest(RequestEvent requestEvent) {
        try {
            sipProcessProxy.process(requestEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        LOGGER.debug("处理响应");
        sipProcessProxy.process(responseEvent);
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        LOGGER.debug("超时");
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
        LOGGER.debug("出现IO异常");
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        ClientTransaction clientTransaction = transactionTerminatedEvent.getClientTransaction();


        LOGGER.debug("{} 事务已终止", clientTransaction.getBranchId());
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        LOGGER.debug("对话已终止");
    }

}