package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.DrivingScenario;
import model.ScenarioLibrary;

// Tests the behaviour of JsonReader.
public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");

        try {
            reader.read();
            fail("IOException was expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyScenarioLibrary() {
        JsonReader reader =
                new JsonReader("./data/testReaderEmptyScenarioLibrary.json");

        try {
            ScenarioLibrary library = reader.read();

            assertEquals(0, library.getNumberOfScenarios());
        } catch (IOException e) {
            fail("Could not read from file");
        }
    }

    @Test
    void testReaderGeneralScenarioLibrary() {
        JsonReader reader =
                new JsonReader("./data/testReaderGeneralScenarioLibrary.json");

        try {
            ScenarioLibrary library = reader.read();
            List<DrivingScenario> scenarios = library.getScenarios();

            assertEquals(2, scenarios.size());

            checkScenario(
                    "Rainy Highway",
                    "rainy",
                    "highway",
                    60.0,
                    "car",
                    10.0,
                    scenarios.get(0));

            checkScenario(
                    "Sunny City",
                    "sunny",
                    "city",
                    40.0,
                    "pedestrian",
                    50.0,
                    scenarios.get(1));

        } catch (IOException e) {
            fail("Could not read from file");
        }
    }

    private void checkScenario(String name,
                               String weatherCondition,
                               String roadType,
                               double vehicleSpeed,
                               String obstacleType,
                               double obstacleDistance,
                               DrivingScenario scenario) {
        assertEquals(name, scenario.getName());
        assertEquals(weatherCondition, scenario.getWeatherCondition());
        assertEquals(roadType, scenario.getRoadType());
        assertEquals(vehicleSpeed, scenario.getVehicleSpeed());
        assertEquals(obstacleType, scenario.getObstacleType());
        assertEquals(obstacleDistance, scenario.getObstacleDistance());
    }
}
