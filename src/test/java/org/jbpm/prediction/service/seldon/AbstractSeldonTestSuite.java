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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AbstractSeldonTestSuite extends AbstractKieServicesTest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(5000);

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    private List<Long> instances = new ArrayList<>();

    @BeforeClass
    public static void setupOnce() {
        System.setProperty("org.jbpm.task.prediction.service", ExampleSeldonPredictionService.IDENTIFIER);
        System.setProperty("org.jbpm.task.prediction.service.seldon.url", "http://localhost:5000");
    }

    @AfterClass
    public static void cleanOnce() {
        System.clearProperty("org.jbpm.task.prediction.service");
    }

    @Override
    protected List<String> getProcessDefinitionFiles() {
        List<String> processes = new ArrayList<>();
        processes.add("BPMN2-UserTask.bpmn2");
        return processes;
    }


    @Override
    public DeploymentUnit prepareDeploymentUnit() throws Exception {
        // specify GROUP_ID, ARTIFACT_ID, VERSION of your kjar
        return createAndDeployUnit("org.jbpm.test.prediction", "seldon-test", "1.0.0");
    }

    protected Map<String, Object> startAndReturnTaskOutputData(String item, String userId, Integer level, Boolean approved) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("item", item);
        parameters.put("level", level);
        parameters.put("actor", userId);
        long processInstanceId = processService.startProcess(deploymentUnit.getIdentifier(), "UserTask", parameters);
        instances.add(processInstanceId);

        List<TaskSummary> tasks = runtimeDataService.getTasksByStatusByProcessInstanceId(processInstanceId, null, new QueryFilter());
        assertNotNull(tasks);

        if (!tasks.isEmpty()) {

            Long taskId = tasks.get(0).getId();

            Map<String, Object> outputs = userTaskService.getTaskOutputContentByTaskId(taskId);
            assertNotNull(outputs);

            userTaskService.completeAutoProgress(taskId, userId, Collections.singletonMap("approved", approved));

            return outputs;
        }

        return new HashMap<>();
    }


}