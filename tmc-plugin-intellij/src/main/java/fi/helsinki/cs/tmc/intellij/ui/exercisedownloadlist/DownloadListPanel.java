package fi.helsinki.cs.tmc.intellij.ui.exercisedownloadlist;

import fi.helsinki.cs.tmc.core.domain.Exercise;

import fi.helsinki.cs.tmc.intellij.services.ExerciseDownloadingService;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DownloadListPanel {
    private JButton downloadButton;
    private JButton cancelButton;
    private CustomCheckBoxList exerciselist;
    private JPanel mainpanel;
    private JButton selectAllButton;

    public DownloadListPanel(List<Exercise> exercises, DownloadListWindow window) {
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                List<Exercise> downloadThese = new ArrayList<>();
                for (JCheckBox box : exerciselist) {
                    if (box.isSelected()) {
                        for (Exercise ex : exercises) {
                            if (box.getText().equals(ex.getName())) {
                                downloadThese.add(ex);
                            }
                        }
                    }
                }
                window.close();
                ExerciseDownloadingService.startDownloading(downloadThese);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                window.close();
            }
        });
        for (Exercise ex : exercises) {
            JCheckBox box = new JCheckBox(ex.getName());
            box.setSelected(true);
            exerciselist.addCheckbox(box);
        }

        selectAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int checked = 0;
                int all = 0;
                for (JCheckBox box : exerciselist) {
                    all++;
                    if (box.isSelected()) {
                        checked++;
                    }
                    box.setSelected(true);
                }
                if (all == checked) {
                    for (JCheckBox box : exerciselist) {
                        box.setSelected(false);
                    }
                }
            }
        });
    }

    public JPanel getPanel() {
        return mainpanel;
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
        mainpanel = new JPanel();
        mainpanel.setLayout(new GridLayoutManager(7, 6, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainpanel.add(scrollPane1, new GridConstraints(3, 1, 1, 4,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                new Dimension(500, 200), new Dimension(500, 200),
                new Dimension(500, 200), 0, false));
        exerciselist = new CustomCheckBoxList();
        scrollPane1.setViewportView(exerciselist);
        final JLabel label1 = new JLabel();
        label1.setText("Select exercises");
        mainpanel.add(label1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainpanel.add(spacer1, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1,
                null, null, null, 0, false));
        downloadButton = new JButton();
        downloadButton.setText("Download");
        mainpanel.add(downloadButton, new GridConstraints(5, 3, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        mainpanel.add(cancelButton, new GridConstraints(5, 4, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainpanel.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1,
                new Dimension(10, -1), new Dimension(10, -1), new Dimension(10, -1), 0, false));
        final Spacer spacer3 = new Spacer();
        mainpanel.add(spacer3, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED,
                1, new Dimension(10, -1), new Dimension(10, -1), new Dimension(10, -1), 0, false));
        final Spacer spacer4 = new Spacer();
        mainpanel.add(spacer4, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED,
                new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final Spacer spacer5 = new Spacer();
        mainpanel.add(spacer5, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED,
                new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final Spacer spacer6 = new Spacer();
        mainpanel.add(spacer6, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED,
                new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final Spacer spacer7 = new Spacer();
        mainpanel.add(spacer7, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED,
                new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        selectAllButton = new JButton();
        selectAllButton.setText("Toggle all");
        mainpanel.add(selectAllButton, new GridConstraints(5, 2, 1, 1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK
                | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
    }

    /**
     * @noinspection ALL.
     */
    public JComponent getRootComponent() {
        return mainpanel;
    }
}
