package com.my.service.comms_service.contracts.comms.request;

import com.my.service.comms_service.contracts.comms.base.MessageContext;
import com.my.service.comms_service.contracts.comms.base.StorageRef;
import com.my.service.comms_service.contracts.comms.enums.ChannelType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailMessageContext extends MessageContext {

    // Attachments (optional)
    // Assuming StorageRef is a class with URL, fileName, etc.
    @Valid  
    @JsonProperty("list_attachments")
    private List<StorageRef> attachments;

    // Optional CC recipients
    @JsonProperty("list_cc")
    private List<String> cc;

    // Optional BCC recipients
    @JsonProperty("list_bcc")
    private List<String> bcc;

    @Override
    public ChannelType getChannel() {
        return ChannelType.email;
    }

}
