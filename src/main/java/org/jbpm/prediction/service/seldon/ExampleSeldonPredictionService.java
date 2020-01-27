package org.jbpm.prediction.service.seldon;

import org.jbpm.prediction.service.seldon.payload.PredictionResponse;
import org.kie.api.task.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleSeldonPredictionService extends AbstractSeldonPredictionService{

    public static final String IDENTIFIER = "SeldonPredictionService";

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public List<List<Double>> buildPredictFeatures(Task task, Map<String, Object> map) {
        List<List<Double>> result = new ArrayList<>();
        List<Double> single = new ArrayList<>();
        single.add(Math.random() < 0.5 ? 1.0 : 0.0);
        single.add(Math.random() * 2500.0);
        result.add(single);
        return result;
    }

    @Override
    public Map<String, Object> parsePredictFeatures(PredictionResponse response) {
        List<Double> singleFeature = response.getData().getOutcomes().get(0);
        double o1 = singleFeature.get(0);
        double o2 = singleFeature.get(1);

        Map<String, Object> result = new HashMap<>();

        if (o1 > o2) {
            result.put("outcome", true);
            result.put("confidence", o1);
        } else {
            result.put("outcome", false);
            result.put("confidence", o2);
        }

        return result;

    }
}
