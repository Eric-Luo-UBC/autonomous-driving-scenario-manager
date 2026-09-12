package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.DrivingScenario;
import model.ScenarioLibrary;

// Tests the behaviour of JsonWriter.
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            ScenarioLibrary library = new ScenarioLibrary();
            JsonWriter writer =
                    new JsonWriter("./data/noSuchDirectory/test.json");

            writer.open();
            fail("FileNotFoundException was expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyScenarioLibrary() {
        try {
            ScenarioLibrary library = new ScenarioLibrary();
            JsonWriter writer =
                    new JsonWriter("./data/testWriterEmptyScenarioLibrary.json");

            writer.open();
            writer.write(library);
            writer.close();

            JsonReader reader =
                    new JsonReader("./data/testWriterEmptyScenarioLibrary.json");

            library = reader.read();
            assertEquals(0, library.getNumberOfScenarios());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralScenarioLibrary() {
        try {
            ScenarioLibrary library = new ScenarioLibrary();

            library.addScenario(new DrivingScenario(
                    "Rainy Highway",
                    "rainy",
                    "highway",
                    60.0,
                    "car",
                    10.0));

            library.addScenario(new DrivingScenario(
                    "Sunny City",
                    "sunny",
                    "city",
                    40.0,
                    "pedestrian",
                    50.0));

            JsonWriter writer =
                    new JsonWriter("./data/testWriterGeneralScenarioLibrary.json");

            writer.open();
            writer.write(library);
            writer.close();

            JsonReader reader =
                    new JsonReader("./data/testWriterGeneralScenarioLibrary.json");

            library = reader.read();
            List<DrivingScenario> scenarios = library.getScenarios();

            assertEquals(2, scenarios.size());

            checkScenario(
                    "Rainy Highway", "rainy", "highway",
                    60.0, "car", 10.0, scenarios.get(0));

            checkScenario(
                    "Sunny City", "sunny", "city",
                    40.0, "pedestrian", 50.0, scenarios.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
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
