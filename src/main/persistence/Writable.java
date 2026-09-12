package persistence;

import org.json.JSONObject;

// Represents an object that can be serialized to JSON.
public interface Writable {
    // EFFECTS: returns this object as JSON
    JSONObject toJson();
}
