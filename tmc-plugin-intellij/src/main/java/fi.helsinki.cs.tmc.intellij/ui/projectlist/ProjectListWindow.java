package fi.helsinki.cs.tmc.intellij.ui.projectlist;

import fi.helsinki.cs.tmc.intellij.actions.OpenToolWindowAction;
import fi.helsinki.cs.tmc.intellij.holders.TmcSettingsManager;
import fi.helsinki.cs.tmc.intellij.io.ProjectOpener;
import fi.helsinki.cs.tmc.intellij.services.CourseAndExerciseManager;
import fi.helsinki.cs.tmc.intellij.services.ObjectFinder;
import fi.helsinki.cs.tmc.intellij.ui.OperationInProgressNotification;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import icons.TmcIcons;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 * Swing component that is displayed in the project list side panel.
 */
public class ProjectListWindow {

    public JTabbedPane getTabbedPanelBase() {
        return tabbedPanelBase;
    }

    public void setTabbedPanelBase(JTabbedPane tabbedPanelBase) {

        this.tabbedPanelBase = tabbedPanelBase;
    }

    private JTabbedPane tabbedPanelBase;

    public JPanel getBasePanel() {
        return basePanel;
    }

    private JPanel basePanel;
    private JButton openButton;
    private JButton hideButton;
    private JToolBar toolbar;

    public ProjectListWindow() {
        addCourseTabsAndExercises();
    }

    public void addCourseTabsAndExercises() {
        tabbedPanelBase.removeAll();
        toolbar.removeAll();
        ObjectFinder finder = new ObjectFinder();
        List<String> courses = finder.listAllDownloadedCourses();
        final ProjectOpener opener = new ProjectOpener();
        CourseTabFactory factory = new CourseTabFactory();

        createCourseSpecificTabs(finder, opener, tabbedPanelBase,
                courses, factory);

        addFunctionalityToHideButton();
        JButton refreshButton = addFunctionalityToRefreshButton();
        toolbar.add(refreshButton);
        addFunctionalityToOpenButton();
    }

    private void createCourseSpecificTabs(ObjectFinder finder,
                                          ProjectOpener opener,
                                          JTabbedPane tabbedPanelBase,
                                          List<String> courses,
                                          CourseTabFactory factory) {

        for (String course : courses) {
            factory.createCourseSpecificTab(finder, opener, course, tabbedPanelBase);
        }
    }

    private void addFunctionalityToOpenButton() {
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JBList list = (JBList) tabbedPanelBase
                        .getSelectedComponent().getComponentAt(10, 10)
                        .getComponentAt(10, 10);

                ProjectOpener opener = new ProjectOpener();
                String courseName = (list.getName());

                opener.openProject(TmcSettingsManager.get().getProjectBasePath()
                        + File.separator + courseName + File.separator
                        + list.getSelectedValue());
            }
        });
    }

    @NotNull
    private JButton addFunctionalityToRefreshButton() {
        JButton refreshButton = new JButton(TmcIcons.REFRESH);
        refreshButton.setBorderPainted(true);
        refreshButton.setEnabled(true);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                OperationInProgressNotification note =
                        new OperationInProgressNotification("Refreshing exercises");
                refreshProjectList();
                note.hide();
            }
        });

        return refreshButton;
    }

    private void addFunctionalityToHideButton() {
        hideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DataContext dataContext =
                        DataManager.getInstance().getDataContextFromFocus().getResult();
                Project project = DataKeys.PROJECT.getData(dataContext);
                new OpenToolWindowAction().hideToolWindow(project);
            }
        });
    }

    public void refreshProjectList() {
        CourseAndExerciseManager.updateAll();
        ProjectListManager.refreshAllCourses();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        setupUi();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer.
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void setupUi() {
        basePanel = new JPanel();
        basePanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));

        tabbedPanelBase = new JTabbedPane();

        basePanel.add(tabbedPanelBase, new GridConstraints(1, 0, 1, 3,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, new Dimension(200, 200), null, 0, false));

        toolbar = new JToolBar();
        toolbar.setBorderPainted(false);
        toolbar.setFloatable(false);
        toolbar.setForeground(new Color(-16777216));

        basePanel.add(toolbar, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                new Dimension(-1, 20), null, 0, false));

        openButton = new JButton();
        openButton.setText("Open");

        basePanel.add(openButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_NONE, 1,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        hideButton = new JButton();
        hideButton.setText("Hide");

        basePanel.add(hideButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_NONE, 1,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        final Spacer spacer1 = new Spacer();
        basePanel.add(spacer1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL.
     */
    public JComponent getRootComponent() {
        return basePanel;
    }
}
