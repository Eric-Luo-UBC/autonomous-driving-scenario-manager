package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// Represents a library containing an arbitrary number of driving scenarios.
public class ScenarioLibrary implements Writable {
    private List<DrivingScenario> scenarios;

    // EFFECTS: constructs an empty scenario library
    public ScenarioLibrary() {
        scenarios = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds the given scenario to this scenario library
    // and logs the addition
    public void addScenario(DrivingScenario scenario) {
        scenarios.add(scenario);

        EventLog.getInstance().logEvent(
            new Event("Driving scenario added: " + scenario.getName()));
    }

    // MODIFIES: this
    // EFFECTS: removes and returns the scenario at the given index
    // and logs the removal
    // REQUIRES: index >= 0 and index < the number of scenarios
    public DrivingScenario removeScenario(int index) {
        DrivingScenario removedScenario = scenarios.remove(index);

        EventLog.getInstance().logEvent(
                new Event("Driving scenario removed: "
                        + removedScenario.getName()));

        return removedScenario;
    }

    // EFFECTS: returns the scenarios stored in this scenario library
    public List<DrivingScenario> getScenarios() {
        return scenarios;
    }

    // EFFECTS: returns all scenarios whose calculated risk level matches
    // the given risk level, ignoring differences in letter case
    public List<DrivingScenario> getScenariosByRiskLevel(String riskLevel) {
        List<DrivingScenario> matchingScenarios = new ArrayList<>();

        for (DrivingScenario scenario : scenarios) {
            if (scenario.calculateRiskLevel().equalsIgnoreCase(riskLevel)) {
                matchingScenarios.add(scenario);
            }
        }

        return matchingScenarios;
    }

    // EFFECTS: returns the number of scenarios in this scenario library
    public int getNumberOfScenarios() {
        return scenarios.size();
    }

    // EFFECTS: returns this scenario library as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("scenarios", scenariosToJson());
        return json;
    }

    // EFFECTS: returns all scenarios in this library as a JSON array
    private JSONArray scenariosToJson() {
        JSONArray jsonArray = new JSONArray();

        for (DrivingScenario scenario : scenarios) {
            jsonArray.put(scenario.toJson());
        }

        return jsonArray;
    }
}
