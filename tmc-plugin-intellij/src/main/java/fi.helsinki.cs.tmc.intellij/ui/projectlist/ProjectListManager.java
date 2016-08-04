package fi.helsinki.cs.tmc.intellij.ui.projectlist;

import com.intellij.ui.components.JBList;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.intellij.services.CourseAndExerciseManager;
import fi.helsinki.cs.tmc.intellij.services.ObjectFinder;
import fi.helsinki.cs.tmc.intellij.ui.elements.ProjectListJBList;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class ProjectListManager {

    private static Map<String, List<ProjectListJBList>> currentListElements;
    static List<ProjectListWindow> projectListWindows;

    public ProjectListManager() {
        projectListWindows = new ArrayList<>();
        currentListElements = new HashMap<>();
    }

    public static void setup() {
        if (currentListElements == null) {
            currentListElements = new HashMap<>();
            projectListWindows = new ArrayList<>();
        }
    }

    public static void addList(ProjectListJBList list) {
        if (currentListElements.get(list.getName()) == null) {
            currentListElements.put(list.getName(), new ArrayList<ProjectListJBList>());
        }
        currentListElements.get(list.getName()).add(list);
    }

    public static void refreshAllCourses() {
        for (ProjectListWindow window : projectListWindows) {
            window.addCourseTabsAndExercises();
        }
    }


    public static void refreshCourse(String course) {
        List<ProjectListJBList> list = currentListElements.get(course);
        if (list == null) {
            return;
        }

        for (ProjectListJBList ProjectListJBList : list) {
            if (list == null || !ProjectListJBList.getName().equals(course)) {
                continue;
            }
            DefaultListModel model = (DefaultListModel) ProjectListJBList.getModel();
            model.removeAllElements();
            addExercisesToList(new ObjectFinder(), course, model);
            ProjectListJBList.setModel(model);
        }
        refreshAllCourses();
    }

    private static int counter = 0;

    public static void refresh(JBList list) {
        if (counter%20 == 0) {
            refreshAllCourses();
        }

    }

    public static void addExercisesToList(ObjectFinder finder,
                                          String course, DefaultListModel defaultListModel) {

        if (CourseAndExerciseManager.isCourseInDatabase(course)) {
            List<Exercise> exercises = CourseAndExerciseManager.getExercises(course);
            addExercisesToListModel(defaultListModel, exercises);
        } else {
            List<String> exercises = finder.listAllDownloadedExercises(course);
            addExercisesToListModelAsStrings(defaultListModel, exercises);
        }
    }

    private static void addExercisesToListModel(DefaultListModel listModel,
                                                List<Exercise> exercises) {
        for (Exercise ex : exercises) {
            listModel.addElement(ex);
        }
    }

    private static void addExercisesToListModelAsStrings(DefaultListModel listModel,
                                                         List<String> exercises) {
        for (String ex : exercises) {
            listModel.addElement(ex);
        }
    }

    public static void setCurrentListElements(HashMap<String, List<ProjectListJBList>> currentListElements) {
        ProjectListManager.currentListElements = currentListElements;
    }

    public static void addWindow(ProjectListWindow window) {
        projectListWindows.add(window);
    }

}
