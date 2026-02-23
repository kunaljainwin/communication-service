package com.my.service.comms_service.contracts.comms.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.service.comms_service.contracts.comms.request.SmsMessageContext;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsDispatchEvent extends SmsMessageContext{    
}
