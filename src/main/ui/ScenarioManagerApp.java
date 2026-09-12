package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import model.DrivingScenario;
import model.ScenarioLibrary;
import persistence.JsonReader;
import persistence.JsonWriter;


// Represents the console user interface for the autonomous driving
// scenario manager.
public class ScenarioManagerApp {
    private Scanner input;
    private ScenarioLibrary library;
    private boolean keepGoing;
    private static final String JSON_STORE = "./data/scenarioLibrary.json";

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    // EFFECTS: constructs and runs the scenario manager application
    public ScenarioManagerApp() {
        input = new Scanner(System.in);
        library = new ScenarioLibrary();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runScenarioManager();
    }

    // MODIFIES: this
    // EFFECTS: processes user input until the user chooses to quit
    private void runScenarioManager() {
        keepGoing = true;

        System.out.println("\nWelcome to the Autonomous Driving Scenario Manager!");

        while (keepGoing) {
            displayMenu();
            String command = input.nextLine().trim().toLowerCase();
            processCommand(command);
        }

        System.out.println("\nThank you for using the application!");
    }


    // EFFECTS: displays the main menu to the user
    private void displayMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("\ta -> add a driving scenario");
        System.out.println("\tv -> view all driving scenarios");
        System.out.println("\tc -> calculate the risk level of a scenario");
        System.out.println("\tf -> filter scenarios by risk level");
        System.out.println("\tr -> remove a driving scenario");
        System.out.println("\ts -> save scenario library");
        System.out.println("\tl -> load scenario library");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes the given menu command
    private void processCommand(String command) {
        if (command.equals("a")) {
            doAddScenario();
        } else if (command.equals("v")) {
            doViewScenarios();
        } else if (command.equals("c")) {
            doCalculateRisk();
        } else if (command.equals("f")) {
            doFilterScenarios();
        } else if (command.equals("r")) {
            doRemoveScenario();
        } else if (command.equals("s")) {
            doSaveScenarioLibrary();
        } else if (command.equals("l")) {
            doLoadScenarioLibrary();
        } else if (command.equals("q")) {
            keepGoing = false;
        } else {
            System.out.println("Selection is not valid.");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new driving scenario based on user input
    private void doAddScenario() {
        System.out.print("Enter scenario name: ");
        String name = input.nextLine();

        System.out.print("Enter weather condition: ");
        String weather = input.nextLine();

        System.out.print("Enter road type: ");
        String roadType = input.nextLine();

        System.out.print("Enter vehicle speed: ");
        double speed = input.nextDouble();
        input.nextLine();

        System.out.print("Enter obstacle type: ");
        String obstacleType = input.nextLine();

        System.out.print("Enter obstacle distance: ");
        double distance = input.nextDouble();
        input.nextLine();

        DrivingScenario scenario = new DrivingScenario(
            name, weather, roadType, speed, obstacleType, distance);

        library.addScenario(scenario);
        System.out.println("Driving scenario added successfully.");
    }

    // EFFECTS: displays all driving scenarios in the library
    private void doViewScenarios() {
        if (library.getNumberOfScenarios() == 0) {
            System.out.println("There are no driving scenarios.");
        } else {
            displayScenarios(library.getScenarios());
        }
    }

    // EFFECTS: displays the given list of driving scenarios
    private void displayScenarios(List<DrivingScenario> scenarios) {
        for (int i = 0; i < scenarios.size(); i++) {
            DrivingScenario scenario = scenarios.get(i);

            System.out.println((i + 1) + ". " + scenario.getName()
                + " | Weather: " + scenario.getWeatherCondition()
                + " | Road: " + scenario.getRoadType()
                + " | Speed: " + scenario.getVehicleSpeed()
                + " | Obstacle: " + scenario.getObstacleType()
                + " | Distance: " + scenario.getObstacleDistance());
        }
    }


    // EFFECTS: displays the calculated risk level of a selected scenario
    private void doCalculateRisk() {
        if (library.getNumberOfScenarios() == 0) {
            System.out.println("There are no driving scenarios.");
            return;
        }

        doViewScenarios();
        System.out.print("Select a scenario number: ");
        int index = input.nextInt();
        input.nextLine();

        if (index >= 1 && index <= library.getNumberOfScenarios()) {
            DrivingScenario scenario = library.getScenarios().get(index - 1);
            System.out.println("Risk level: " + scenario.calculateRiskLevel());
        } else {
            System.out.println("Invalid scenario number.");
        }
    }
    

    // EFFECTS: displays scenarios matching a user-selected risk level
    private void doFilterScenarios() {
        if (library.getNumberOfScenarios() == 0) {
            System.out.println("There are no driving scenarios.");
            return;
        }

        System.out.print("Enter a risk level (LOW, MEDIUM, or HIGH): ");
        String riskLevel = input.nextLine().trim();
        List<DrivingScenario> matches =
                library.getScenariosByRiskLevel(riskLevel);

        if (matches.isEmpty()) {
            System.out.println("No scenarios have that risk level.");
        } else {
            displayScenarios(matches);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes a user-selected scenario from the library
    private void doRemoveScenario() {
        if (library.getNumberOfScenarios() == 0) {
            System.out.println("There are no driving scenarios.");
            return;
        }

        doViewScenarios();
        System.out.print("Select a scenario number to remove: ");
        int index = input.nextInt();
        input.nextLine();

        if (index >= 1 && index <= library.getNumberOfScenarios()) {
            DrivingScenario removed = library.removeScenario(index - 1);
            System.out.println(removed.getName() + " was removed.");
        } else {
            System.out.println("Invalid scenario number.");
        }
    }

    // EFFECTS: saves the scenario library to file
    private void doSaveScenarioLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();
            System.out.println("Scenario library saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save scenario library.");
        }
        }


// MODIFIES: this
// EFFECTS: loads the scenario library from file
    private void doLoadScenarioLibrary() {
        try {
            library = jsonReader.read();
            System.out.println("Scenario library loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to load scenario library.");
        }
    }
}
