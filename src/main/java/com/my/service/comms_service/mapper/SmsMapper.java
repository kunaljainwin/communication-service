package com.my.service.comms_service.mapper;

import com.my.service.comms_service.contracts.comms.event.SmsDispatchEvent;
import com.my.service.comms_service.contracts.comms.request.SmsMessageContext;

public class SmsMapper {
public SmsDispatchEvent toDispatchEvent(SmsMessageContext smsMessageContext) {
    SmsDispatchEvent dto = new SmsDispatchEvent();

    // SMS-specific fields
    dto.setRecipients(smsMessageContext.getRecipients());
    dto.setSubject(smsMessageContext.getSubject());
    dto.setBody(smsMessageContext.getBody());
    dto.setConfigContext(smsMessageContext.getConfigContext());
    dto.setMetaConfig(smsMessageContext.getMetaConfig());
    dto.setWebhookUrl(null);

    return dto;
}


}
