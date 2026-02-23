package com.my.service.comms_service.contracts.comms.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageRef {
    
    @NotBlank(message = "file_id is required")
    @JsonProperty("file_id")
    private String fileId;
    
    @NotBlank(message = "owner is required")
    @JsonProperty("owner")
    private String ownerContext;
    
    // @NotBlank(message = "file_name is required")
    // @JsonProperty("file_name")
    // private String fileName;
}
