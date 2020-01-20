/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jbpm.prediction.service.seldon.payload.PredictionRequest;
import org.jbpm.prediction.service.seldon.payload.PredictionResponse;
import org.kie.api.task.model.Task;
import org.kie.internal.task.api.prediction.PredictionOutcome;
import org.kie.internal.task.api.prediction.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractSeldonPredictionService implements PredictionService {

    private final String SELDON_URL;
    protected final ResteasyClient client = new ResteasyClientBuilder().build();
    protected final ResteasyWebTarget predict;
    private static final Logger logger = LoggerFactory.getLogger(AbstractSeldonPredictionService.class);


    public AbstractSeldonPredictionService() {
        // Seldon connection configuration
        Configurations configs = new Configurations();
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();

        Configuration javaProperties = new PropertiesConfiguration();
        Configuration systemProperties = new SystemConfiguration();

        compositeConfiguration.addConfiguration(javaProperties);
        compositeConfiguration.addConfiguration(systemProperties);

        try {
            Configuration config = configs.properties(new File("seldon.properties"));
            compositeConfiguration.addConfiguration(config);
        } catch (ConfigurationException e) {
            logger.debug("Could not find the file 'seldon.properties'. Trying other configuration sources.");
        }

        SELDON_URL = compositeConfiguration.getString("org.jbpm.task.prediction.service.seldon.url");

        if (SELDON_URL == null) {
            final String errorMessage = "No Seldon endpoint URL specified";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        logger.debug("Using Seldon endpoint " + SELDON_URL);

        predict = client.target(SELDON_URL).path("predict");

    }

    @Override
    public abstract String getIdentifier();

    @Override
    public PredictionOutcome predict(Task task, Map<String, Object> map) {
        final List<List<Double>> features = buildPredictFeatures(task, map);
        final PredictionRequest req = new PredictionRequest(features);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);

        try {
            final String JSON = mapper.writeValueAsString(req);
            final String stringResponse = predict.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(JSON, MediaType.APPLICATION_JSON_TYPE), String.class);
            final PredictionResponse response = mapper.readValue(stringResponse, PredictionResponse.class);
            System.out.println(response.getData().getOutcomes());
            final Map<String, Object> parsedResponse = parsePredictFeatures(response);

            return new PredictionOutcome((Double) parsedResponse.get("confidence"), 1.0, parsedResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new PredictionOutcome();
    }

    @Override
    public void train(Task task, Map<String, Object> map, Map<String, Object> map1) {
        // Training not supported
    }

    public abstract List<List<Double>> buildPredictFeatures(Task task, Map<String, Object> map);

    public abstract Map<String, Object> parsePredictFeatures(PredictionResponse response);
}
