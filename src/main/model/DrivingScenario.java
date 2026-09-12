package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a simplified driving scenario and its rule-based risk level.
public class DrivingScenario implements Writable {
    private String name;
    private String weatherCondition;
    private String roadType;
    private double vehicleSpeed;
    private String obstacleType;
    private double obstacleDistance;

    // REQUIRES: vehicleSpeed >= 0 and obstacleDistance >= 0
    // EFFECTS: constructs a driving scenario with the given name, weather
    // condition, road type, vehicle speed, obstacle type, and obstacle distance
    public DrivingScenario(String name, String weatherCondition, String roadType,
                           double vehicleSpeed, String obstacleType,
                           double obstacleDistance) {
        this.name = name;
        this.weatherCondition = weatherCondition;
        this.roadType = roadType;
        this.vehicleSpeed = vehicleSpeed;
        this.obstacleType = obstacleType;
        this.obstacleDistance = obstacleDistance;
    }

    // EFFECTS: calculates the required safe distance as half the vehicle speed;
    // increases the required safe distance by 50 percent in rainy or foggy
    // weather; returns "HIGH" if the obstacle distance is less than half the
    // required safe distance, "MEDIUM" if it is less than the required safe
    // distance, and "LOW" otherwise
    public String calculateRiskLevel() {
        double safeDistance = vehicleSpeed / 2.0;

        if (weatherCondition.equalsIgnoreCase("rainy")
            || weatherCondition.equalsIgnoreCase("foggy")) {
            safeDistance = safeDistance * 1.5;
        }

        if (obstacleDistance < safeDistance / 2.0) {
            return "HIGH";
        } else if (obstacleDistance < safeDistance) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    public String getName() {
        return name;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public String getRoadType() {
        return roadType;
    }

    public double getVehicleSpeed() {
        return vehicleSpeed;
    }

    public String getObstacleType() {
        return obstacleType;
    }

    public double getObstacleDistance() {
        return obstacleDistance;
    }

    // EFFECTS: returns this driving scenario as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("weatherCondition", weatherCondition);
        json.put("roadType", roadType);
        json.put("vehicleSpeed", vehicleSpeed);
        json.put("obstacleType", obstacleType);
        json.put("obstacleDistance", obstacleDistance);

        return json;
    }

}
