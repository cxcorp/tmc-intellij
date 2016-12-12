package fi.helsinki.cs.tmc.intellij.actions.buttonactions;

import fi.helsinki.cs.tmc.intellij.ui.settings.SettingsWindow;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;

/**
 * Opens the settings window. Defined in plugin.xml on line &lt;action id="Settings"
 * class="fi.helsinki.cs.tmc.intellij .actions.buttonactions.TmcSettingsAction"&gt;
 */
public class TmcSettingsAction extends AnAction {

    private static final Logger logger = LoggerFactory.getLogger(TmcSettingsAction.class);
    private SettingsWindow window;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        logger.info("Performing TmcSettingsAction. @TmcSettingsAction");
        Project currentProject = anActionEvent.getProject();
        showSettings();
        tryMoveToSameMonitorAsProject(currentProject);
    }

    private void showSettings() {
        logger.info("Opening TMC setting window. @TmcSettingsAction");
        if (window == null) {
            window = new SettingsWindow();
        }

        window.show();
    }

    private void tryMoveToSameMonitorAsProject(Project project) {
        WindowManager winMan = WindowManager.getInstance();
        JFrame projectFrame = winMan.getFrame(project);
        if (projectFrame != null) {
            window.setLocationRelativeTo(projectFrame);
        }
    }
}
