package fi.helsinki.cs.tmc.intellij.services;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.intellij.actions.RunTestsAction;
import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;
import fi.helsinki.cs.tmc.intellij.ui.submissionresult.SubmissionResultHandler;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Offers method to upload exercises.
 */
public class ExerciseUploadingService {

    private static final Logger logger = LoggerFactory.getLogger(CheckForExistingExercises.class);

    public void startUploadExercise(Project project, TmcCore core, ObjectFinder finder,
                                           CheckForExistingExercises checker,
                                           SubmissionResultHandler handler,
                                           SettingsTmc settings,
                                           CourseAndExerciseManager courseAndExerciseManager,
                                           ThreadingService threadingService) {
        logger.info("Starting to upload an exercise. @ExerciseUploadingService");

        String[] exerciseCourse = PathResolver.getCourseAndExerciseName(project);

        if (!courseAndExerciseManager.isCourseInDatabase(getCourseName(exerciseCourse))) {
            Messages.showErrorDialog(project, "Project not identified as TMC exercise", "Error");
            return;
        }

        Exercise exercise = courseAndExerciseManager
                .getExercise(getCourseName(exerciseCourse),
                        getExerciseName(exerciseCourse));
        getResults(project, exercise, core, handler, threadingService);
        courseAndExerciseManager.updateSingleCourse(getCourseName(exerciseCourse),
                checker, finder, settings);
    }

    private void getResults(final Project project,
                                   final Exercise exercise, final TmcCore core,
                                   final SubmissionResultHandler handler,
                                   ThreadingService threadingService) {
        logger.info("Calling for threadingService from getResult. @ExerciseUploadingService.");
        ThreadingService.runWithNotification(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Getting submission results. @ExerciseUploadingService");
                    final SubmissionResult result = core
                            .submit(ProgressObserver.NULL_OBSERVER, exercise).call();
                    handler.showResultMessage(exercise, result, project);
                } catch (Exception exception) {
                    logger.warn("Could not getExercise submission results. @ExerciseUploadingService",
                            exception, exception.getStackTrace());
                    exception.printStackTrace();
                }
            }
        }, "Uploading exercise, this may take several minutes", project);
        RunTestsAction.displayTestWindow();
    }

    private String getCourseName(String[] courseAndExercise) {
        logger.info("Getting course name. @ExerciseUploadingService.");
        return courseAndExercise[courseAndExercise.length - 2];
    }

    private String getExerciseName(String[] courseAndExercise) {
        logger.info("Getting exercise name. @ExerciseUploadingService.");
        return courseAndExercise[courseAndExercise.length - 1];
    }

}