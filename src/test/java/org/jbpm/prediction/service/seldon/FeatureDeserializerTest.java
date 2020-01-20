package org.jbpm.prediction.service.seldon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jbpm.prediction.service.seldon.payload.PredictionRequest;
import org.jbpm.prediction.service.seldon.payload.PredictionResponse;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeatureDeserializerTest {

    @Test
    public void testSingleFeatureDeserialization() throws IOException {
        final PredictionResponse response = new PredictionResponse();

        final String JSON = "{\"data\":{\"names\":[\"t:0\",\"t:1\"],\"ndarray\":[[0.71,0.29]]},\"meta\":{}}";

        ObjectMapper mapper = new ObjectMapper();
        PredictionResponse result = mapper.readValue(JSON, PredictionResponse.class);

        assertEquals(result.getData().getOutcomes().size(), 1);
        assertEquals(result.getData().getNames().size(), 2);
        List<Double> firstOutcome = result.getData().getOutcomes().get(0);
        assertEquals(firstOutcome.size(), 2);
        assertEquals(firstOutcome.get(0), 0.71, 0.0);
        assertEquals(firstOutcome.get(1), 0.29, 0.0);
    }

}