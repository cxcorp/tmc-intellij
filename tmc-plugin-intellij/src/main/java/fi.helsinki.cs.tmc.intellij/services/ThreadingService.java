package fi.helsinki.cs.tmc.intellij.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.intellij.holders.TmcCoreHolder;

public class ThreadingService {

    public static void runWithNotification(final Runnable run, String title, Project project) {
        final ProgressWindow progressWindow = new ProgressWindow(false, true, project);
        progressWindow.setIndeterminate(true);
        progressWindow.setTitle(title);
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                ProgressManager.getInstance().runProcess(run, progressWindow);
            }
        });
    }


}
