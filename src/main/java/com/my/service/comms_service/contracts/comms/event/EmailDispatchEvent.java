package com.my.service.comms_service.contracts.comms.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.service.comms_service.contracts.comms.request.EmailMessageContext;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailDispatchEvent extends EmailMessageContext{
}
