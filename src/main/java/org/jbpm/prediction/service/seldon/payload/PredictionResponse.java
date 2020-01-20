package org.jbpm.prediction.service.seldon.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PredictionResponse {

    public PredictionData getData() {
        return data;
    }

    @JsonProperty("data")
    private final PredictionData data = new PredictionData();

    public PredictionMetadata getMetadata() {
        return metadata;
    }

    @JsonProperty("meta")
    private final PredictionMetadata metadata = new PredictionMetadata();

    public PredictionResponse() {

    }

}
