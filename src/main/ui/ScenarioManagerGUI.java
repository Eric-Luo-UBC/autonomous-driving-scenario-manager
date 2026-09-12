package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import model.DrivingScenario;
import model.ScenarioLibrary;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.Event;
import model.EventLog;

// Represents the graphical user interface for the autonomous driving
// scenario manager.
public class ScenarioManagerGUI extends JFrame {
    private static final String JSON_STORE =
            "./data/scenarioLibrary.json";

    private ScenarioLibrary library;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    private DefaultListModel<String> scenarioListModel;
    private JList<String> scenarioList;
    private JLabel statusLabel;

    // EFFECTS: constructs and displays the scenario manager GUI
    public ScenarioManagerGUI() {
        super("Autonomous Driving Scenario Manager");

        library = new ScenarioLibrary();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);

        initializeWindow();
        initializeTitle();
        initializeScenarioList();
        initializeVisualPanel();
        initializeBottomPanel();

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes the main application window
    private void initializeWindow() {
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            printEventLog();
            dispose();
            System.exit(0);
            }
        });
    }

    // EFFECTS: prints all logged events to the console
    private void printEventLog() {
        for (Event event : EventLog.getInstance()) {
        System.out.println(event);
        System.out.println();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the title to the window
    private void initializeTitle() {
        JLabel titleLabel =
                new JLabel(
                        "Autonomous Driving Scenario Manager",
                        JLabel.CENTER);

        titleLabel.setFont(
                new Font("SansSerif", Font.BOLD, 24));

        add(titleLabel, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the scenario list to the window
    private void initializeScenarioList() {
        scenarioListModel = new DefaultListModel<>();
        scenarioList = new JList<>(scenarioListModel);

        scenarioList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane =
                new JScrollPane(scenarioList);

        scrollPane.setPreferredSize(
                new Dimension(680, 450));

        add(scrollPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the visual driving component
    private void initializeVisualPanel() {
        ScenarioVisualPanel visualPanel =
                new ScenarioVisualPanel();

        visualPanel.setPreferredSize(
                new Dimension(260, 450));

        add(visualPanel, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the buttons and status label
    private void initializeBottomPanel() {
        JPanel southPanel =
                new JPanel(new BorderLayout());

        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        buttonPanel.add(createAddButton());
        buttonPanel.add(createRemoveButton());
        buttonPanel.add(createRiskButton());
        buttonPanel.add(createSaveButton());
        buttonPanel.add(createLoadButton());

        statusLabel = new JLabel("Ready");

        southPanel.add(
                buttonPanel,
                BorderLayout.CENTER);

        southPanel.add(
                statusLabel,
                BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: returns a button that adds a scenario
    private JButton createAddButton() {
        JButton addButton =
                new JButton("Add Scenario");

        addButton.addActionListener(
                event -> doAddScenario());

        return addButton;
    }

    // EFFECTS: returns a button that removes the selected scenario
    private JButton createRemoveButton() {
        JButton removeButton =
                new JButton("Remove Selected");

        removeButton.addActionListener(
                event -> doRemoveScenario());

        return removeButton;
    }

    // EFFECTS: returns a button that displays scenario risk
    private JButton createRiskButton() {
        JButton riskButton =
                new JButton("Calculate Risk");

        riskButton.addActionListener(
                event -> doCalculateRisk());

        return riskButton;
    }

    // EFFECTS: returns a button that saves the library
    private JButton createSaveButton() {
        JButton saveButton =
                new JButton("Save");

        saveButton.addActionListener(
                event -> doSaveScenarioLibrary());

        return saveButton;
    }

    // EFFECTS: returns a button that loads the library
    private JButton createLoadButton() {
        JButton loadButton =
                new JButton("Load");

        loadButton.addActionListener(
                event -> doLoadScenarioLibrary());

        return loadButton;
    }

    // MODIFIES: this
    // EFFECTS: asks the user for information and adds a scenario
    private void doAddScenario() {
        JTextField[] fields =
                createScenarioFields();

        JPanel inputPanel =
                createScenarioInputPanel(fields);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        inputPanel,
                        "Add Driving Scenario",
                        JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            addScenarioFromFields(fields);
        }
    }

    // EFFECTS: returns text fields for a new scenario
    private JTextField[] createScenarioFields() {
        JTextField[] fields =
                new JTextField[6];

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
        }

        return fields;
    }

    // EFFECTS: returns a panel containing scenario input fields
    private JPanel createScenarioInputPanel(
            JTextField[] fields) {

        JPanel panel =
                new JPanel(
                        new GridLayout(0, 2, 5, 5));

        addInputRow(panel, "Name:", fields[0]);
        addInputRow(panel, "Weather:", fields[1]);
        addInputRow(panel, "Road type:", fields[2]);
        addInputRow(panel, "Vehicle speed:", fields[3]);
        addInputRow(panel, "Obstacle type:", fields[4]);
        addInputRow(panel, "Obstacle distance:", fields[5]);

        return panel;
    }

    // MODIFIES: panel
    // EFFECTS: adds a label and text field to the panel
    private void addInputRow(
            JPanel panel,
            String label,
            JTextField field) {

        panel.add(new JLabel(label));
        panel.add(field);
    }

    // MODIFIES: this
    // EFFECTS: constructs and adds a scenario from the fields
    private void addScenarioFromFields(
            JTextField[] fields) {

        try {
            DrivingScenario scenario =
                    constructScenario(fields);

            library.addScenario(scenario);
            updateScenarioList();

            statusLabel.setText(
                    "Scenario added.");

        } catch (NumberFormatException e) {
            showError(
                    "Speed and distance must be valid numbers.");
        }
    }

    // EFFECTS: constructs and returns a scenario from fields
    private DrivingScenario constructScenario(
            JTextField[] fields) {

        double speed =
                Double.parseDouble(
                        fields[3].getText());

        double distance =
                Double.parseDouble(
                        fields[5].getText());

        return new DrivingScenario(
                fields[0].getText(),
                fields[1].getText(),
                fields[2].getText(),
                speed,
                fields[4].getText(),
                distance);
    }

    // MODIFIES: this
    // EFFECTS: removes the selected scenario
    private void doRemoveScenario() {
        int selectedIndex =
                scenarioList.getSelectedIndex();

        if (selectedIndex < 0) {
            showError(
                    "Please select a scenario to remove.");
            return;
        }

        library.removeScenario(selectedIndex);
        updateScenarioList();

        statusLabel.setText(
                "Scenario removed.");
    }

    // EFFECTS: displays the risk level of the selected scenario
    private void doCalculateRisk() {
        int selectedIndex =
                scenarioList.getSelectedIndex();

        if (selectedIndex < 0) {
            showError(
                    "Please select a scenario first.");
            return;
        }

        DrivingScenario scenario =
                library.getScenarios().get(
                        selectedIndex);

        JOptionPane.showMessageDialog(
                this,
                "Risk level: "
                        + scenario.calculateRiskLevel(),
                "Scenario Risk",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // MODIFIES: this
    // EFFECTS: saves the complete scenario library to file
    private void doSaveScenarioLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();

            statusLabel.setText(
                    "Scenario library saved.");

            showInformation(
                    "Scenario library saved successfully.");

        } catch (FileNotFoundException e) {
            showError(
                    "Unable to save the scenario library.");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the complete scenario library from file
    private void doLoadScenarioLibrary() {
        try {
            library = jsonReader.read();
            updateScenarioList();

            statusLabel.setText(
                    "Scenario library loaded.");

            showInformation(
                    "Scenario library loaded successfully.");

        } catch (IOException e) {
            showError(
                    "Unable to load the scenario library.");
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the displayed scenario list
    private void updateScenarioList() {
        scenarioListModel.clear();

        for (DrivingScenario scenario
                : library.getScenarios()) {

            scenarioListModel.addElement(
                    formatScenario(scenario));
        }
    }

    // EFFECTS: returns a display string for the scenario
    private String formatScenario(
            DrivingScenario scenario) {

        return scenario.getName()
                + " | Weather: "
                + scenario.getWeatherCondition()
                + " | Road: "
                + scenario.getRoadType()
                + " | Speed: "
                + scenario.getVehicleSpeed()
                + " | Obstacle: "
                + scenario.getObstacleType()
                + " | Distance: "
                + scenario.getObstacleDistance();
    }

    // EFFECTS: displays an error message
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: displays an informational message
    private void showInformation(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Represents a visual drawing of a road
    // and an autonomous vehicle.
    private static class ScenarioVisualPanel
            extends JPanel {

        // EFFECTS: constructs the visual panel
        private ScenarioVisualPanel() {
            setBackground(
                    new Color(180, 220, 245));
        }

        // MODIFIES: this
        // EFFECTS: draws the driving visual component
        @Override
        protected void paintComponent(
                Graphics graphics) {

            super.paintComponent(graphics);

            drawSkyLabel(graphics);
            drawRoad(graphics);
            drawVehicle(graphics);
        }

        // MODIFIES: graphics
        // EFFECTS: draws the visual panel title
        private void drawSkyLabel(
                Graphics graphics) {

            graphics.setColor(
                    Color.DARK_GRAY);

            graphics.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            18));

            graphics.drawString(
                    "Autonomous Driving",
                    35,
                    45);
        }

        // MODIFIES: graphics
        // EFFECTS: draws the road and road markings
        private void drawRoad(
                Graphics graphics) {

            graphics.setColor(
                    Color.DARK_GRAY);

            graphics.fillRect(
                    0,
                    190,
                    getWidth(),
                    230);

            graphics.setColor(
                    Color.WHITE);

            for (int x = 10;
                    x < getWidth();
                    x += 70) {

                graphics.fillRect(
                        x,
                        300,
                        40,
                        8);
            }
        }

        // MODIFIES: graphics
        // EFFECTS: draws a simplified autonomous vehicle
        private void drawVehicle(
                Graphics graphics) {

            graphics.setColor(
                    Color.BLUE);

            graphics.fillRoundRect(
                    65,
                    240,
                    135,
                    55,
                    20,
                    20);

            graphics.setColor(
                    Color.CYAN);

            graphics.fillRect(
                    95,
                    220,
                    70,
                    30);

            graphics.setColor(
                    Color.BLACK);

            graphics.fillOval(
                    80,
                    280,
                    30,
                    30);

            graphics.fillOval(
                    160,
                    280,
                    30,
                    30);
        }
    }
}
