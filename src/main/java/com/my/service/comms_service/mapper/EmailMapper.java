package com.my.service.comms_service.mapper;

import com.my.service.comms_service.contracts.comms.event.EmailDispatchEvent;
import com.my.service.comms_service.contracts.comms.request.EmailMessageContext;

public class EmailMapper {
    public EmailDispatchEvent toDispatchEvent(EmailMessageContext emailMessageContext) {
        EmailDispatchEvent dto = new EmailDispatchEvent();

        // Common fields
        dto.setRecipients(emailMessageContext.getRecipients());
        dto.setSubject(emailMessageContext.getSubject());
        dto.setBody(emailMessageContext.getBody());
        dto.setConfigContext(emailMessageContext.getConfigContext());
        dto.setAttachments(emailMessageContext.getAttachments());
        dto.setCc(emailMessageContext.getCc());
        dto.setBcc(emailMessageContext.getBcc());

        // Optional: notificationId can be set if available (after saving in DB)
        // dto.setNotificationId(emailMessageContext.getNotificationId());

        return dto;
    }

}
