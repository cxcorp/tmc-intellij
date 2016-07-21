package fi.helsinki.cs.tmc.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.siyeh.ig.ui.TextField;
import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.intellij.holders.TmcCoreHolder;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.io.SettingsTmc;
import fi.helsinki.cs.tmc.intellij.services.CheckForExistingExercises;

import fi.helsinki.cs.tmc.intellij.io.ProjectOpener;
import fi.helsinki.cs.tmc.intellij.services.PersistentTmcSettings;
import fi.helsinki.cs.tmc.intellij.ui.SubmissionResultWaitingDialog;
import fi.helsinki.cs.tmc.intellij.ui.elements.SettingsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DownloadExerciseAction extends AnAction {

    private Exercise exercise;
    private List<Exercise> list;

    public DownloadExerciseAction() {
        exercise = new Exercise();
        list = new ArrayList<Exercise>();
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        try {
            downloadExerciseAction(TmcCoreHolder.get(), TmcSettingsManager.get(), new CheckForExistingExercises(), new ProjectOpener());
        } catch (Exception e) {
            Messages.showMessageDialog(project, "Downloading failed \n" + e.getMessage(), "Result", Messages.getErrorIcon());
        }
    }

    public List<Exercise> downloadExerciseAction(TmcCore core, SettingsTmc settings, CheckForExistingExercises checker, ProjectOpener opener) throws Exception {
        Course course = core.getCourseDetails(ProgressObserver.NULL_OBSERVER, settings.getCourse()).call();
        List<Exercise> exercises = course.getExercises();
        exercises = checker.clean(exercises);
        core.downloadOrUpdateExercises(ProgressObserver.NULL_OBSERVER, exercises).call();
        return exercises;
    }
}