package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import model.DrivingScenario;
import model.ScenarioLibrary;

// Represents a reader that reads a scenario library from JSON data
// stored in a file.
// Code is based on the JsonSerializationDemo provided by CPSC 210.
public class JsonReader {
    private String source;

    // EFFECTS: constructs a reader that reads from the given source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads a scenario library from the source file and returns it;
    // throws IOException if an error occurs while reading data from the file
    public ScenarioLibrary read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseScenarioLibrary(jsonObject);
    }

    // EFFECTS: reads the source file as a string and returns it
    private String readFile(String source) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(source));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // EFFECTS: parses and returns a scenario library from the JSON object
    private ScenarioLibrary parseScenarioLibrary(JSONObject jsonObject) {
        ScenarioLibrary library = new ScenarioLibrary();
        addScenarios(library, jsonObject);
        return library;
    }

    // MODIFIES: library
    // EFFECTS: parses scenarios from the JSON object and adds them to library
    private void addScenarios(ScenarioLibrary library,
                              JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("scenarios");

        for (Object json : jsonArray) {
            JSONObject nextScenario = (JSONObject) json;
            addScenario(library, nextScenario);
        }
    }

    // MODIFIES: library
    // EFFECTS: parses a scenario from the JSON object and adds it to library
    private void addScenario(ScenarioLibrary library, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String weather = jsonObject.getString("weatherCondition");
        String roadType = jsonObject.getString("roadType");
        double speed = jsonObject.getDouble("vehicleSpeed");
        String obstacleType = jsonObject.getString("obstacleType");
        double distance = jsonObject.getDouble("obstacleDistance");

        DrivingScenario scenario = new DrivingScenario(
                name, weather, roadType, speed, obstacleType, distance);

        library.addScenario(scenario);
    }
}