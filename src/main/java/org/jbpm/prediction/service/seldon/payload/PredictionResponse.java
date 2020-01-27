package org.jbpm.prediction.service.seldon.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionResponse {

    @JsonProperty(value = "data", required = true)
    private final PredictionData data = new PredictionData();
    @JsonProperty(value = "meta", required = false)
    private final PredictionMetadata metadata = new PredictionMetadata();

    public PredictionResponse() {

    }

    public PredictionData getData() {
        return data;
    }

    public PredictionMetadata getMetadata() {
        return metadata;
    }

    public static PredictionResponse parse(String response) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, PredictionResponse.class);
    }


}
