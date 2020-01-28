package org.jbpm.prediction.service.seldon.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionMetadata {

    @JsonProperty(value = "tags", required = false)
    private final Map<String, Object> tags = new HashMap<>();

    public Map<String, Object> getTags() {
        return tags;
    }
}
