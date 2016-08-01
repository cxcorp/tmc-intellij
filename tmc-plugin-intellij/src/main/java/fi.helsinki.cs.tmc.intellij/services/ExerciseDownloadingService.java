package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.openapi.application.ApplicationManager;
import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.intellij.io.ProjectOpener;
import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExerciseDownloadingService {

    public static void startDownloadExercise(final TmcCore core,
                                             final SettingsTmc settings,
                                             final CheckForExistingExercises checker,
                                             ProjectOpener opener) throws Exception {

        Thread run = createThread(core, settings, checker);
        ThreadingService
                .runWithNotification(run,
                        "Downloading exercises, this may take several minutes",
                        new ObjectFinder().findCurrentProject());
    }

    @NotNull
    private static Thread createThread(final TmcCore core, final SettingsTmc settings, final CheckForExistingExercises checker) {
        return new Thread() {
            @Override
            public void run() {
                ObjectFinder finder = new ObjectFinder();
                Course course = finder
                        .findCourseByName(settings.getCourse()
                                .getName(), core);
                List<Exercise> exercises = course.getExercises();
                exercises = checker.clean(exercises, settings);
                try {
                    core.downloadOrUpdateExercises(ProgressObserver.NULL_OBSERVER,
                                            exercises).call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                CourseAndExerciseManager
//                        .updateSingleCourse(course.getName(),
//                                checker, finder, settings);
            }
        };
    }
}
