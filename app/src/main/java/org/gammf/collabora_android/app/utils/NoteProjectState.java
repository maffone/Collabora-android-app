package org.gammf.collabora_android.app.utils;

/**
 * An enum representing note states in project collaborations.
 */
public enum NoteProjectState {
    TO_DO("To Do"),
    DOING("Doing"),
    DONE("Done"),
    REVIEW("Review"),
    TESTING("Testing"),
    BLOCKED("Blocked");

    private final String stateName;

    NoteProjectState(final String name) {
        this.stateName = name;
    }

    @Override
    public String toString() {
        return this.stateName;
    }
};
