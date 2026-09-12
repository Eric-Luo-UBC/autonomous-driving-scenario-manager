package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// Represents the event log for the application.
// Uses the Singleton design pattern so that there is only one EventLog.
// Code is based on the EventLog class from the CPSC 210 AlarmSystem example.
public class EventLog implements Iterable<Event> {
    private static EventLog theLog;

    private Collection<Event> events;

    // EFFECTS: constructs an empty event log
    private EventLog() {
        events = new ArrayList<>();
    }

    // EFFECTS: returns the single EventLog instance,
    // creating it if it does not already exist
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }

        return theLog;
    }

    // MODIFIES: this
    // EFFECTS: adds the given event to this event log
    public void logEvent(Event event) {
        events.add(event);
    }

    // MODIFIES: this
    // EFFECTS: clears this event log and then logs that it was cleared
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}