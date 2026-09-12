package model;

import java.util.Calendar;
import java.util.Date;

// Represents an event in the application.
// Code is based on the Event class from the CPSC 210 AlarmSystem example.
public class Event {
    private static final int HASH_CONSTANT = 13;

    private Date dateLogged;
    private String description;

    // EFFECTS: creates an event with the given description
    // and the current date/time stamp
    public Event(String description) {
        dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    // EFFECTS: returns the date and time at which this event was logged
    public Date getDate() {
        return dateLogged;
    }

    // EFFECTS: returns the description of this event
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        Event otherEvent = (Event) other;

        return this.dateLogged.equals(otherEvent.dateLogged)
                && this.description.equals(otherEvent.description);
    }

    @Override
    public int hashCode() {
        return HASH_CONSTANT * dateLogged.hashCode()
                + description.hashCode();
    }

    @Override
    public String toString() {
        return dateLogged.toString()
                + "\n"
                + description;
    }
}
