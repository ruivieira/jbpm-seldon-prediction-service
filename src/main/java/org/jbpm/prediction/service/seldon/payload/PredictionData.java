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
