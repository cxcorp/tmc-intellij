package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.codeInsight.daemon.impl.DaemonProgressIndicator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.DisposableEditorPanel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.DumbProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.util.BackgroundTaskUtil;
import com.intellij.openapi.progress.util.ProgressIndicatorBase;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.progress.util.ProgressWindowWithNotification;
import com.intellij.openapi.progress.util.StandardProgressIndicatorBase;
import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.intellij.holders.TmcCoreHolder;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;
import fi.helsinki.cs.tmc.intellij.runners.UploadRunner;
import fi.helsinki.cs.tmc.intellij.ui.submissionresult.SubmissionResultHandler;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Offers static methods to upload exercises.
 */
public class ExerciseUploadingService {

    public static void startUploadExercise(Project project, TmcCore core, ObjectFinder finder,
                                           CheckForExistingExercises checker,
                                           SubmissionResultHandler handler,
                                           SettingsTmc settings) {
        String[] exerciseCourse = PathResolver.getCourseAndExerciseName(project);

        if (!CourseAndExerciseManager.isCourseInDatabase(getCourseName(exerciseCourse))) {
            Messages.showErrorDialog(project, "Project not identified as TMC exercise", "Error");
            return;
        }

        try {
           Exercise exercise = CourseAndExerciseManager
                    .get(getCourseName(exerciseCourse),
                            getExerciseName(exerciseCourse));
            getResults(project, exercise, core, handler);
            CourseAndExerciseManager.updateSingleCourse(getCourseName(exerciseCourse),
                    checker, finder, settings);
        } catch (Exception exception) {
            Messages.showErrorDialog(project, "Are your credentials correct?\n"
                    + "Is this a TMC Exercise?\n"
                    + "Are you connected to the internet?\n"
                    + exception.getMessage() + " "
                    + exception.toString(), "Error while submitting");
        }

    }

    private static void getResults(final Project project, final Exercise exercise, final TmcCore core,
                                   final SubmissionResultHandler handler) {
        ThreadingService.runWithNotification(new Runnable() {
            @Override
            public void run() {
                try {
                    final SubmissionResult result = core.submit(ProgressObserver.NULL_OBSERVER, exercise).call();
                    handler.showResultMessage(exercise, result, project);
                } catch (Exception exception) {

                }
            }
        }, "Uploading exercise, this may take several minutes", project);
    }

    private static String getCourseName(String[] courseAndExercise) {
        return courseAndExercise[courseAndExercise.length - 2];
    }

    private static String getExerciseName(String[] courseAndExercise) {
        return courseAndExercise[courseAndExercise.length - 1];
    }

}
