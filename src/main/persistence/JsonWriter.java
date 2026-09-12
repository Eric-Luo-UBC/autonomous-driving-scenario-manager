package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.json.JSONObject;

import model.ScenarioLibrary;

// Represents a writer that writes a scenario library to a JSON file.
// Code is based on the JsonSerializationDemo provided by CPSC 210.
public class JsonWriter {
    private static final int TAB = 4;

    private String destination;
    private PrintWriter writer;

    // EFFECTS: constructs a writer that writes to the destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens the destination file for writing;
    // throws FileNotFoundException if the file cannot be opened
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes the scenario library to the destination file
    public void write(ScenarioLibrary library) {
        JSONObject json = library.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes the given JSON string to the destination file
    private void saveToFile(String json) {
        writer.print(json);
    }
}