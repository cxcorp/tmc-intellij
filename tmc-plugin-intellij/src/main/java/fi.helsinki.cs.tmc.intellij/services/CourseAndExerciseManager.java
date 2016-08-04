package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.openapi.progress.BackgroundTaskQueue;
import com.intellij.openapi.project.Project;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;
import fi.helsinki.cs.tmc.intellij.holders.TmcCoreHolder;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;
import fi.helsinki.cs.tmc.intellij.ui.projectlist.ProjectListManager;

import com.intellij.openapi.ui.Messages;
import fi.helsinki.cs.tmc.intellij.ui.submissionresult.SubmissionResultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds a database of courses in memory, allowing quick fetching of course
 * when necessary without calling the TmcCore.
 *
 * <p>
 *   Also gives methods to update project list when necessary
 * </p>
 */
public class CourseAndExerciseManager {

    public static void setDatabase(Map<String, List<Exercise>> database) {
        CourseAndExerciseManager.database = database;
    }

    static Map<String, List<Exercise>> database;
    private static List<Course> courses;

    public CourseAndExerciseManager() {
        try {
            initiateDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setup() {
        if (database != null) {
            return;
        }

        try {
            initiateDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Exercise get(String course, String exercise) {
        try {
            List<Exercise> exercises = database.get(course);
            for (Exercise exc : exercises) {
                if (exerciseIsTheCorrectOne(exc, exercise)) {
                    return exc;
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    private static boolean exerciseIsTheCorrectOne(Exercise exc, String exerciseName) {
        return exc.getName().equals(exerciseName);
    }

    public static List<Exercise> getExercises(String course) {
        try {
            return database.get(course);
        } catch (Exception e) {

        }
        return null;
    }

    public static Map<String, List<Exercise>> getDatabase() {
        return database;
    }

    static void initiateDatabase() throws Exception {
        try {
            List<String> directoriesOnDisk = new ObjectFinder().listAllDownloadedCourses();

            database = new HashMap<>();
            courses = new ArrayList<>();
            courses = (ArrayList<Course>) TmcCoreHolder.get()
                    .listCourses(ProgressObserver.NULL_OBSERVER).call();

            for (Course course : courses) {
                List<Exercise> exercises;
                if (!directoriesOnDisk.contains(course.getName())) {
                    continue;
                }

                try {
                    course = TmcCoreHolder.get()
                            .getCourseDetails(ProgressObserver.NULL_OBSERVER, course).call();
                    exercises = (ArrayList<Exercise>) new CheckForExistingExercises()
                            .getListOfDownloadedExercises(course.getExercises(),
                                    TmcSettingsManager.get());
                    database.put(course.getName(), exercises);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (TmcCoreException exception) {
            Messages.showErrorDialog(new ObjectFinder().findCurrentProject(),
                    exception.getMessage()
                    + " " + exception.toString(), "Error");
        }
    }

    private void updateDatabase(Course course, List<Exercise> exercises) {
        database.put(course.getName(), exercises);
    }

    public static void updateSingleCourse(String courseName, CheckForExistingExercises checker,
                                          ObjectFinder finder,
                                          SettingsTmc settings) {
        boolean isNewCourse = database.get(courseName) == null;
        Course course = finder.findCourseByName(courseName, TmcCoreHolder.get());

        List<Exercise> existing = (ArrayList<Exercise>) checker
                .getListOfDownloadedExercises(course.getExercises(), settings);

        database.put(courseName, existing);

        if (isNewCourse) {
            ProjectListManager.refreshAllCourses();
        } else {
            ProjectListManager.refreshCourse(courseName);
        }
    }

    public static void updateAll() {
        try {
            initiateDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isCourseInDatabase(String string) {
        return database.keySet().contains(string);
    }
}
