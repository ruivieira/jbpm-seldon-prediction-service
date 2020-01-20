package org.jbpm.prediction.service.seldon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jbpm.prediction.service.seldon.payload.PredictionRequest;
import org.junit.Test;

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
        final PredictionRequest request = new PredictionRequest();
        request.addFeatures(buildFeatures(1.0, 2500.0));

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String result = mapper.writeValueAsString(request);
        System.out.println(result);
        assertEquals("{\"data\":{\"ndarray\":[[1.0,2500.0]]}}", result);
    }

    @Test
    public void testMultipleFeatureSerialization() throws JsonProcessingException {
        final PredictionRequest request = new PredictionRequest();

        request.addFeatures(buildFeatures(1.0, 2500.0));
        request.addFeatures(buildFeatures(0.0, 1944.2));


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String result = mapper.writeValueAsString(request);
        System.out.println(result);
        assertEquals("{\"data\":{\"ndarray\":[[1.0,2500.0],[0.0,1944.2]]}}", result);
    }

}