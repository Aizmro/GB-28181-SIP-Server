package com.jingjiang.gb28181.application;

import com.jingjiang.gb28181.configuration.SipProviderProxy;
import com.jingjiang.gb28181.configuration.SipRequestHeaderProvider;
import com.jingjiang.gb28181.domain.Device;
import com.jingjiang.gb28181.domain.DeviceRepository;
import com.jingjiang.gb28181.domain.Stream;
import com.jingjiang.gb28181.domain.StreamRepository;
import gov.nist.javax.sip.SIPConstants;
import gov.nist.javax.sip.header.CallID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.Objects;
import java.util.Optional;


/**
 * 视频业务类
 */
@Service
public class SipAppService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SipAppService.class);

    private final DeviceRepository deviceRepository;
    private final StreamRepository streamRepository;
    private final SipInfoContext sipInfoContext;
    private final SipProviderProxy sipProviderProxy;
    private final SipRequestHeaderProvider sipRequestHeaderProvider;

    public SipAppService(DeviceRepository deviceRepository, StreamRepository streamRepository, SipInfoContext sipInfoContext, SipProviderProxy sipProviderProxy, SipRequestHeaderProvider sipRequestHeaderProvider) {
        this.deviceRepository = deviceRepository;
        this.streamRepository = streamRepository;
        this.sipInfoContext = sipInfoContext;
        this.sipProviderProxy = sipProviderProxy;
        this.sipRequestHeaderProvider = sipRequestHeaderProvider;
    }


    /**
     * 云台控制
     *
     * @param host      地址
     * @param channelId 通道号
     * @param cmd       指令
     * @param speed     速度
     * @throws SipException             Sip异常
     * @throws InvalidArgumentException 无效参数异常
     * @throws ParseException           解析异常
     */
    public void PTZControl(String host, String channelId, String cmd, int speed) throws SipException, InvalidArgumentException, ParseException {
        int cmdCode = 0;

        switch (cmd) {
            case "left" -> cmdCode |= 0x02;
            case "right" -> cmdCode |= 0x01;
            case "up" -> cmdCode |= 0x08;
            case "down" -> cmdCode |= 0x04;
            case "zoomIn" -> cmdCode |= 0x10;
            case "zoomOut" -> cmdCode |= 0x20;
        }

        StringBuilder builder = new StringBuilder("A50F01");
        String strTmp =  String.format("%02X", cmdCode);
        builder.append(strTmp, 0, 2);

        strTmp = String.format("%02X", speed);
        builder.append(strTmp, 0, 2);
        builder.append(strTmp, 0, 2);

        strTmp = String.format("%X", speed);
        builder.append(strTmp, 0, 1).append("0");
        //计算校验码
        int checkCode = (0XA5 + 0X0F + 0X01 + cmdCode + speed + speed + (speed & 0XF0)) % 0X100;
        strTmp = String.format("%02X", checkCode);
        builder.append(strTmp, 0, 2);
        String s = builder.toString();

        String context = sipInfoContext.PTZControlContext(channelId, s);

        Optional<Device> optional = deviceRepository.findByHost(host);
        if (optional.isPresent()) {
            Device device = optional.get();
            SipProvider sipProvider = sipProviderProxy.getSipProvider(device.getProtocol());
            CallIdHeader callId = sipProvider.getNewCallId();
            String fromTag = String.valueOf(System.currentTimeMillis());
            String viaTag = SIPConstants.BRANCH_MAGIC_COOKIE + System.currentTimeMillis();

            Request request = sipRequestHeaderProvider.createMessageRequest(device.getReceived(), device.getRPort(), channelId, fromTag, viaTag, context, device.getProtocol(), callId);
            sipProvider.getNewClientTransaction(request).sendRequest();
        } else {
            throw new RuntimeException("摄像头不存在");
        }


    }

    /**
     * 发送 - 播放实时视频 - SIP信令
     *
     * @param host      地址
     * @param channelId 通道id
     * @param port      端口
     * @throws InvalidArgumentException 无效参数异常
     * @throws SipException             Sip异常
     * @throws ParseException           解析异常
     */
    public void play(String host, String channelId, Integer port) throws InvalidArgumentException, SipException, ParseException {

        Optional<Stream> optional = streamRepository.findStreamByHostAndChannelId(host, channelId);

        if (optional.isEmpty()) {
            Device device = deviceRepository.findByHost(host).orElseThrow();

            int ssrc = (int) ((Math.random() * 9 + 1) * 100000000);
            String context = sipInfoContext.playContext(ssrc, channelId, port);
            SipProvider sipProvider = sipProviderProxy.getSipProvider("UDP");

            CallID callID = new CallID(String.valueOf(System.currentTimeMillis()));

            String fromTag = String.valueOf(System.currentTimeMillis());
            String viaTag = SIPConstants.BRANCH_MAGIC_COOKIE + System.currentTimeMillis();

            Request request = sipRequestHeaderProvider.createInviteRequest(device.getReceived(), device.getRPort(), channelId, fromTag, viaTag, context, "UDP", callID);
            ClientTransaction transaction = sipProvider.getNewClientTransaction(request);

            transaction.sendRequest();

            streamRepository.save(Stream.builder().host(host).channelId(channelId).branchId(transaction.getBranchId()).callId(callID.getCallId()).fromTag(fromTag).viaTag(viaTag).build());
        } else {
            throw new RuntimeException("流已存在");
        }

    }

    /**
     * 发送 - 停止播放实时视频 - SIP信令
     *
     * @param host      地址
     * @param channelId 通道id
     */
    public void stop(String host, String channelId) throws SipException, InvalidArgumentException, ParseException {
        Optional<Stream> streamOptional = streamRepository.findStreamByHostAndChannelId(host, channelId);
        SipProvider sipProvider = sipProviderProxy.getSipProvider("UDP");

        if (streamOptional.isPresent()) {
            Stream stream = streamOptional.get();

            CallID callID = new CallID(stream.getCallId());

            Device device = deviceRepository.findByHost(host).orElseThrow();

            Request requestBye = sipRequestHeaderProvider.createByteRequest(device.getReceived(), device.getRPort(), channelId, stream.getFromTag(), stream.getViaTag(), "UDP", callID);
            ClientTransaction transaction = sipProvider.getNewClientTransaction(requestBye);
            transaction.sendRequest();

            streamRepository.delete(stream);
        } else {
            throw new RuntimeException("host" + "@" + channelId + "实时流不存在");
        }

    }


}
