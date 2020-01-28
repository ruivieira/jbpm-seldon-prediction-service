package org.jbpm.prediction.service.seldon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TestUtils {

    public static String readJSONAsString(String relPath) throws IOException {
        final InputStream is = NDArrayResponseTest.class.getClassLoader().getResourceAsStream(relPath);
        return new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
    }
}
