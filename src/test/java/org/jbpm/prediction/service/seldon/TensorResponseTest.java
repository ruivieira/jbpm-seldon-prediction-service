package org.jbpm.prediction.service.seldon;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.jbpm.prediction.service.seldon.examples.ExampleSeldonPredictionService;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.test.services.AbstractKieServicesTest;
import org.junit.*;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.query.QueryFilter;

import java.io.IOException;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class TensorResponseTest extends AbstractSeldonTestSuite {

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
                        .withBody(TestUtils.readJSONAsString("responses/tensor.json"))));

        System.setProperty("org.jbpm.task.prediction.service.confidence_threshold", "1.0");

        startAndReturnTaskOutputData("test item", "john", 5, false);
        Map<String, Object> outputs = startAndReturnTaskOutputData("test item", "john", 5, true);

        final double confidence = (double) outputs.get("confidence");
        assertEquals(confidence, 0.79, 1e-10);
    }

}