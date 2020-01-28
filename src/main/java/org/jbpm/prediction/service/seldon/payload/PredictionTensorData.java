package org.jbpm.prediction.service.seldon.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PredictionTensorData {

    @JsonProperty(value = "shape", required = false)
    private int[] shape = new int[2];
    @JsonProperty(value = "values", required = false)
    private List<Double> values = new ArrayList<>();

    public int[] getShape() {
        return shape;
    }

    public List<Double> getValues() {
        return values;
    }
}
