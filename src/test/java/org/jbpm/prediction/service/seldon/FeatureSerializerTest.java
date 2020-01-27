package org.jbpm.prediction.service.seldon;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jbpm.prediction.service.seldon.payload.PredictionRequest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FeatureSerializerTest {

    private static List<Double> buildFeatures(double... features) {
        return Arrays.stream(features).boxed().collect(Collectors.toList());
    }

    @Test
    public void testSingleFeatureSerialization() throws JsonProcessingException {
        final List<List<Double>> features = new ArrayList<>();
        features.add(buildFeatures(1.0, 2500.0));

        final String request = PredictionRequest.build(features);
        assertEquals("{\"data\":{\"ndarray\":[[1.0,2500.0]]}}", request);
    }

    @Test
    public void testMultipleFeatureSerialization() throws JsonProcessingException {
        final List<List<Double>> features = new ArrayList<>();

        features.add(buildFeatures(1.0, 2500.0));
        features.add(buildFeatures(0.0, 1944.2));

        String result = PredictionRequest.build(features);
        assertEquals("{\"data\":{\"ndarray\":[[1.0,2500.0],[0.0,1944.2]]}}", result);
    }

}