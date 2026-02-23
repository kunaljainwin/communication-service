package com.my.service.comms_service.contracts.comms.request;

import com.my.service.comms_service.contracts.comms.base.MessageContext;
import com.my.service.comms_service.contracts.comms.enums.ChannelType;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SmsMessageContext extends MessageContext {

   /**
    * Returns the channel type for this message context.
    * For SMS, override with ChannelType.sms if needed.
    */
   @Override
   public ChannelType getChannel() {
       return ChannelType.sms;  // Since it's SmsMessageContext, should be sms
   }
}
