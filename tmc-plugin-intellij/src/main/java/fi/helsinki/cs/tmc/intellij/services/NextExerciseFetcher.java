package fi.helsinki.cs.tmc.intellij.services;

import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.intellij.actions.DownloadExerciseAction;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.io.ProjectOpener;
import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NextExerciseFetcher {

    private static final Logger logger = LoggerFactory.getLogger(NextExerciseFetcher.class);
    private String course;
    private Project project;
    private Exercise exercise;

    public NextExerciseFetcher(String course, Exercise exercise, Project project) {
        this.course = course;
        this.exercise = exercise;
        this.project = project;
    }

    /**
     * Tries to open the next exercise, or one that was previously skipped.
     * If one can't be found, asks user to download more.
     */
    public void tryToOpenNext() {
        logger.info("Finding next exercise.");
        this.exercise = findNext();
        if (exercise != null) {
            logger.info("Exercise was found successfully. Opening found exercise.");
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ProjectOpener().openProject(exercise.getExerciseDirectory(TmcSettingsManager
                            .get().getTmcProjectDirectory()));
                }
            });
        } else {
            logger.info("No next exercise candidate was found, prompting user to download more.");
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    SettingsTmc settings = TmcSettingsManager.get();
                    if (Messages.showYesNoDialog("All local exercises for " + course
                                    + " seem to be done! Would you"
                                    + " like to try to download the next batch?",
                            "Great work!", null) == 0) {
                        logger.info("Decided to attempt to download more exercises.");
                        if (!settings.getCourseName()
                                .equals(PathResolver.getCourseName(project.getBasePath()))) {
                            logger.info("Setting current course"
                                    + " to match the course we want to download from.");
                            settings.setCourse(PathResolver.getCourse(project.getBasePath()));
                        }
                        new DownloadExerciseAction().downloadExercises(project, false);
                    }
                }
            });
        }
    }

    public Exercise findNext() {
        logger.info("Trying to find next exercise candidate.");
        CourseAndExerciseManager manager = new CourseAndExerciseManager();
        List<Exercise> exercises = manager.getExercises(course);
        Exercise next = null;
        for (Exercise ex : exercises) {
            if (!ex.isCompleted() && ex.getName()
                    .compareTo(exercise.getName()) < 0 && next == null) {
                next = ex;
            } else if (!ex.isCompleted() && ex.getName()
                    .compareTo(exercise.getName()) > 0) {
                logger.info("Next exercise found.");
                return ex;
            }
        }
        return next;
    }

    /**
     * Opens first incomplete exercise in list.
     */

    public static void openFirst(List<Exercise> exercises) {
        for (Exercise ex: exercises) {
            if (!ex.isCompleted()) {
                new ProjectOpener().openProject(ex
                        .getExerciseDirectory(TmcSettingsManager.get().getTmcProjectDirectory()));
                return;
            }
        }
    }
}
