package fi.helsinki.cs.tmc.intellij.services;

import fi.helsinki.cs.tmc.core.domain.Exercise;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls the courses. Used by ExerciseDatabaseManager to set and get the course map.
 */
public class ExerciseDatabase implements Serializable {

    private Map<String, List<Exercise>> courses;

    public ExerciseDatabase() {
        courses =  new HashMap();
    }

    public Map<String, List<Exercise>> getCourses() {
        return courses;
    }

    public void setCourses(Map<String, List<Exercise>> courses) {
        this.courses = courses;
    }

}
