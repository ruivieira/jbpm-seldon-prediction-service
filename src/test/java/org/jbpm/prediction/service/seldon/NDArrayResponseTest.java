/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.prediction.service.seldon;

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
    public void testNDArrayResponse() throws IOException {
        stubFor(post(urlEqualTo("/predict"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(TestUtils.readJSONAsString("responses/ndarray.json"))));

        startAndReturnTaskOutputData("test item", "john", 5, false);
        Map<String, Object> outputs = startAndReturnTaskOutputData("test item", "john", 5, true);

        final double confidence = (double) outputs.get("confidence");
        assertEquals(confidence, 0.71, 1e-10);
    }

}