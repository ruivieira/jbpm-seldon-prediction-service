package org.jbpm.prediction.service.seldon.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PredictionData {

    @JsonProperty("names")
    private final List<String> names = new ArrayList<>();

    @JsonProperty(value = "ndarray", required = false)
    private List<List<Double>> array;
    @JsonProperty(value = "tftensor", required = false)
    private List<Byte> tftensor;
    @JsonProperty(value = "tensor", required = false)
    private PredictionTensorData tensorData;

    public List<Byte> getTftensor() {
        return tftensor;
    }

    public List<String> getNames() {
        return names;
    }

    public List<List<Double>> getArray() {
        return array;
    }

    public PredictionTensorData getTensorData() {
        return tensorData;
    }
}
