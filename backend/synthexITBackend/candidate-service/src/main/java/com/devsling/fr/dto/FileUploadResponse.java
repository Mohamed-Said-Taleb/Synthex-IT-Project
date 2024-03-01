package com.devsling.fr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {
    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("downloadUri")
    private String downloadUri;
    @JsonProperty("size")
    private long size;
}
