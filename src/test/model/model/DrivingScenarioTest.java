package model.model;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.DrivingScenario;

// Tests the rule-based risk classification for a driving scenario.
public class DrivingScenarioTest {
    private DrivingScenario sunnyScenario;

    @BeforeEach
    void runBefore() {
        sunnyScenario = new DrivingScenario(
                "City Drive",
                "sunny",
                "city",
                60.0,
                "car",
                40.0
        );
    }

    @Test
    void testConstructor() {
        assertEquals("City Drive", sunnyScenario.getName());
        assertEquals("sunny", sunnyScenario.getWeatherCondition());
        assertEquals("city", sunnyScenario.getRoadType());
        assertEquals(60.0, sunnyScenario.getVehicleSpeed());
        assertEquals("car", sunnyScenario.getObstacleType());
        assertEquals(40.0, sunnyScenario.getObstacleDistance());
    }

    @Test
    void testCalculateLowRisk() {
        assertEquals("LOW", sunnyScenario.calculateRiskLevel());
    }

    @Test
    void testCalculateMediumRisk() {
        DrivingScenario scenario = new DrivingScenario(
                "Medium Risk",
                "sunny",
                "city",
                60.0,
                "pedestrian",
                20.0
        );

        assertEquals("MEDIUM", scenario.calculateRiskLevel());
    }

    @Test
    void testCalculateHighRisk() {
        DrivingScenario scenario = new DrivingScenario(
                "High Risk",
                "sunny",
                "highway",
                60.0,
                "car",
                10.0
        );

        assertEquals("HIGH", scenario.calculateRiskLevel());
    }

    @Test
    void testRainIncreasesRisk() {
        DrivingScenario scenario = new DrivingScenario(
                "Rainy Drive",
                "rainy",
                "city",
                60.0,
                "car",
                40.0
        );

        assertEquals("MEDIUM", scenario.calculateRiskLevel());
    }

    @Test
    void testFogIncreasesRisk() {
        DrivingScenario scenario = new DrivingScenario(
                "Foggy Drive",
                "foggy",
                "highway",
                60.0,
                "object",
                40.0
        );

        assertEquals("MEDIUM", scenario.calculateRiskLevel());
    }
}
