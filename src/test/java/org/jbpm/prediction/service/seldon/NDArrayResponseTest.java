package org.jbpm.prediction.service.seldon;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class NDArrayResponseTest extends AbstractSeldonTestSuite {

    /**
     * Insert an equal number of true and false samples, making
     * sure the total number of samples is larger than the dataset
     * size threshold. Since the dataset size
     * threshold will have been met and the probability of true
     * and false will be nearly equal, we expect confidence to be
     * lower than 0.55 (55%).
     */
    @Test
    public void testEqualProbabilityRandomForestPredictionService() throws IOException {
        stubFor(post(urlEqualTo("/predict"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(TestUtils.readJSONAsString("responses/ndarray.json"))));

        System.setProperty("org.jbpm.task.prediction.service.confidence_threshold", "1.0");

        startAndReturnTaskOutputData("test item", "john", 5, false);
        Map<String, Object> outputs = startAndReturnTaskOutputData("test item", "john", 5, true);

        final double confidence = (double) outputs.get("confidence");
        assertEquals(confidence, 0.71, 1e-10);
    }

}