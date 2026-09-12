package model.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.DrivingScenario;
import model.ScenarioLibrary;

// Tests scenario collection operations and risk-level filtering.
public class ScenarioLibraryTest {
    private ScenarioLibrary library;
    private DrivingScenario lowRiskScenario;
    private DrivingScenario mediumRiskScenario;
    private DrivingScenario highRiskScenario;

    @BeforeEach
    void runBefore() {
        library = new ScenarioLibrary();

        lowRiskScenario = new DrivingScenario(
                "Low Risk", "sunny", "city",
                60.0, "car", 40.0);

        mediumRiskScenario = new DrivingScenario(
                "Medium Risk", "sunny", "city",
                60.0, "pedestrian", 20.0);

        highRiskScenario = new DrivingScenario(
                "High Risk", "sunny", "highway",
                60.0, "car", 10.0);
    }

    @Test
    void testConstructor() {
        assertEquals(0, library.getNumberOfScenarios());
        assertEquals(0, library.getScenarios().size());
    }

    @Test
    void testAddOneScenario() {
        library.addScenario(lowRiskScenario);

        assertEquals(1, library.getNumberOfScenarios());
        assertEquals(lowRiskScenario, library.getScenarios().get(0));
    }

    @Test
    void testAddMultipleScenarios() {
        library.addScenario(lowRiskScenario);
        library.addScenario(mediumRiskScenario);
        library.addScenario(highRiskScenario);

        assertEquals(3, library.getNumberOfScenarios());
        assertEquals(lowRiskScenario, library.getScenarios().get(0));
        assertEquals(mediumRiskScenario, library.getScenarios().get(1));
        assertEquals(highRiskScenario, library.getScenarios().get(2));
    }

    @Test
    void testRemoveScenario() {
        library.addScenario(lowRiskScenario);
        library.addScenario(highRiskScenario);

        DrivingScenario removedScenario = library.removeScenario(0);

        assertEquals(lowRiskScenario, removedScenario);
        assertEquals(1, library.getNumberOfScenarios());
        assertEquals(highRiskScenario, library.getScenarios().get(0));
    }

    @Test
    void testGetScenariosByRiskLevelNoMatches() {
        library.addScenario(lowRiskScenario);

        assertEquals(
                0,
                library.getScenariosByRiskLevel("HIGH").size());
    }

    @Test
    void testGetScenariosByRiskLevelOneMatch() {
        library.addScenario(lowRiskScenario);
        library.addScenario(mediumRiskScenario);
        library.addScenario(highRiskScenario);

        assertEquals(
                highRiskScenario,
                library.getScenariosByRiskLevel("HIGH").get(0));
    }

    @Test
    void testGetScenariosByRiskLevelMultipleMatches() {
        DrivingScenario anotherHighRiskScenario = new DrivingScenario(
                "Second High Risk", "rainy", "city",
                60.0, "object", 10.0);

        library.addScenario(highRiskScenario);
        library.addScenario(lowRiskScenario);
        library.addScenario(anotherHighRiskScenario);

        assertEquals(
                2,
                library.getScenariosByRiskLevel("high").size());
    }
}
