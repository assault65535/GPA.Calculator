import Models.GPAMethods.GradeIndenifier.GradeIdentifier;
import Models.GPAMethods.LevelIdentifier.ScoreIdentifier;
import Resources.Values;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Tnecesoc on 2016/5/12.
 */
public class MainPage {
    private JTabbedPane tabbedPane1;
    private JList Lectures;
    private JTextPane InputData;
    private JTextField txt_Lecture;
    private JTextField txt_Credit;
    private JTextField txt_Score;
    private JPanel Jpanel1;
    private JButton addButton;
    private JButton removeButton;
    private JButton allClearButton;
    private JComboBox cbb_Standard;
    private JComboBox cbb_PointSystem;
    private JLabel lbl_GPA;
    private JLabel lbl_score_or_grade;
    private JButton detailsButton;
    private JPanel GPAPanel;
    private JPanel courcePanel;

    private LectureDatas numericDatas;
    private NonNumericLectureDatas nonNumericDatas;

    private boolean is_using_nonNumericDatas;

    private MainPage() {

        datasInit();

        txt_Lecture.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txt_Score.requestFocus();
                }
            }
        });

        txt_Score.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    txt_Credit.requestFocus();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!is_using_nonNumericDatas) {
                    txt_Score.setText(txt_Score.getText().replaceAll("[^0-9|\\.]", ""));
                }
            }
        });

        txt_Credit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                boolean flag = txt_Lecture.getText().isEmpty() || txt_Score.getText().isEmpty() || txt_Credit.getText().isEmpty();
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!flag) {
                        insertTXTData();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                txt_Score.setText(txt_Score.getText().replaceAll("[^0-9|\\.]", ""));
            }
        });

        addButton.addActionListener(e -> {
            boolean flag = txt_Lecture.getText().isEmpty() || txt_Score.getText().isEmpty() || txt_Credit.getText().isEmpty();

            if (!flag) {
                insertTXTData();
            }

            if (!InputData.getText().isEmpty()) {
                insertMassInputData();
            }
        });

        removeButton.addActionListener(e -> deleteSingleData());

        allClearButton.addActionListener(e -> deleteAllData());

        cbb_PointSystem.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setCbb_StandardAlternativesTo(Values.inUsePointSystems.get(e.getItem() + "System"));
                is_using_nonNumericDatas = Values.nonNumericSystems.contains(e.getItem() + "System");
                lbl_score_or_grade.setText(is_using_nonNumericDatas ? "Grade" : "Score");
            }
        });

        cbb_Standard.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (!is_using_nonNumericDatas) {
                    calculate();
                } else {
                    calculateNonNumeric();
                }
            }
        });

        detailsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this.Jpanel1, "踏马，不写了");
        });

        tabbedPane1.addChangeListener(e -> {
            txt_Score.setText(null);
            txt_Lecture.setText(null);
            txt_Credit.setText(null);
            InputData.setText(null);
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GPA Calculator");
        frame.setContentPane(new MainPage().Jpanel1);
        //noinspection MagicConstant
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void insertMassInputData() {
        String str = InputData.getText();
        String lectureName, scoreOrGrade, credit;
        InputData.setText(null);

        str = str.replaceAll("[\\t\\n\\r]", "");

        String[] args = str.split("[;]");

        for (String i : args) {
            lectureName = i.split("[ ]")[0];
            scoreOrGrade = i.split("[ ]")[1];
            credit = i.split("[ ]")[2];

            if (is_using_nonNumericDatas) {
                addNonNumericLecture(scoreOrGrade, Double.parseDouble(credit));
            } else {
                addLecture(Double.parseDouble(scoreOrGrade), Double.parseDouble(credit));
            }
            insertListData(lectureName, scoreOrGrade, credit);
        }

        if (is_using_nonNumericDatas) {
            calculateNonNumeric();
        } else {
            calculate();
        }
    }

    private void insertTXTData() {

        insertListData(txt_Lecture.getText(), txt_Score.getText(), txt_Credit.getText());

        if (!is_using_nonNumericDatas) {
            addLecture(
                    Double.parseDouble(txt_Score.getText()),
                    Double.parseDouble(txt_Credit.getText())
            );
            calculate();
        } else {
            addNonNumericLecture(
                    txt_Score.getText(),
                    Double.parseDouble(txt_Credit.getText())
            );
            calculateNonNumeric();
        }


        txt_Lecture.requestFocus();
    }

    private void insertListData(String name, String score, String credit) {
        DefaultListModel dim = new DefaultListModel();

        ListModel now = Lectures.getModel();

        for (int i = 0; i < now.getSize(); i++)
            dim.addElement(now.getElementAt(i));

        dim.addElement(
                name
                        + " "
                        + score
                        + " "
                        + credit
        );

        Lectures.setModel(dim);
    }

    private void deleteSingleData() {

        DefaultListModel dim = new DefaultListModel();

        ListModel now = Lectures.getModel();

        for (int i = 0; i < now.getSize() - 1; i++)
            dim.addElement(now.getElementAt(i));

        Lectures.setModel(dim);

        if (!is_using_nonNumericDatas) {
            numericDatas.pop();
            calculate();
        } else {
            nonNumericDatas.pop();
            calculateNonNumeric();
        }

        if (dim.isEmpty()) {
            lbl_GPA.setForeground(new Color(0, 0, 0));
        }
    }

    private void deleteAllData() {
        Lectures.setModel(new DefaultListModel());
        numericDatas.clear();
        nonNumericDatas.clear();

        txt_Lecture.setText(null);
        txt_Score.setText(null);
        txt_Credit.setText(null);
        InputData.setText(null);

        lbl_GPA.setForeground(new Color(0, 0, 0));
        lbl_GPA.setText(Values.lbl_GPATextIfEmpty);
    }

    private void setCbb_StandardAlternativesTo(String[] alternatives) {
        DefaultComboBoxModel temp = new DefaultComboBoxModel();

        for (String i : alternatives)
            temp.addElement(i);

        cbb_Standard.setModel(temp);
    }

    private void calculate() {

        double ans = 0;

        try {
            ans = (
                    numericDatas.getGPA(
                            (ScoreIdentifier) Class.forName(
                                    Values.levelIdentifierPackage
                                            + "."
                                            + cbb_Standard.getSelectedItem()
                                            + "Identifier"
                            ).newInstance()
                    )
            );
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (numericDatas.isEmpty()) {
            lbl_GPA.setText(Values.lbl_GPATextIfEmpty);
            return;
        }

        anim_turnLabelValuesTo(ans);
    }

    private void calculateNonNumeric() {

        double ans = 0;

        try {
            ans = (
                    nonNumericDatas.getGPA(
                            (GradeIdentifier) Class.forName(
                                    Values.gradeIdentifierPackage
                                            + "."
                                            + cbb_Standard.getSelectedItem()
                                            + "Identifier"
                            ).newInstance()
                    )
            );
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (nonNumericDatas.isEmpty()) {
            lbl_GPA.setText(Values.lbl_GPATextIfEmpty);
            return;
        }

        anim_turnLabelValuesTo(ans);
    }

    private void addLecture(double score, double credit) {
        try {
            numericDatas.addLectureData(
                    Class.forName(
                            Values.numericSystemPackage
                                    + "."
                                    + cbb_PointSystem.getSelectedItem()
                                    + "System"
                    ),
                    score,
                    credit
            );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addNonNumericLecture(String grade, double credit) {

        try {
            nonNumericDatas.addLectureData(
                    Class.forName(
                            Values.nonNumericSystemPackage
                                    + "."
                                    + cbb_PointSystem.getSelectedItem()
                                    + "System"
                    ),
                    grade,
                    credit
            );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void datasInit() {
        numericDatas = new LectureDatas();
        nonNumericDatas = new NonNumericLectureDatas();
    }

    private void anim_turnLabelValuesTo(double val) {

        val = Math.abs(val);

        if (val != 0) {
            while (val < 100.0) {
                val *= 10.0;
            }
        }

        final int digit = (int) val;

        Thread animation = new Thread(() -> {

            double offset;
            final int MAXR = 255, MAXG = 180, MAXV = 430, damp = 200;
            int L, R, G;

            removeButton.setEnabled(false);
            allClearButton.setEnabled(false);
            tabbedPane1.setEnabled(false);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    for (int k = 0; k < 10; k++) {
                        lbl_GPA.setText(
                                Integer.toString(i)
                                        + "."
                                        + Integer.toString(j)
                                        + Integer.toString(k)
                        );

                        offset = i * 100 + j * 10 + k;

                        L = (int) (((offset - damp) > 0 ? (offset - damp) : 0) / (MAXV - damp) * (MAXG + MAXR));

                        R = L > MAXR ? (MAXG + MAXR - L) : MAXR;
                        G = L > MAXG ? MAXG : L;

                        lbl_GPA.setForeground(new Color(R, G, 0));

                        if (offset == digit) {
                            removeButton.setEnabled(true);
                            allClearButton.setEnabled(true);
                            return;
                        }

                        try {
                            Thread.currentThread().sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            removeButton.setEnabled(true);
            allClearButton.setEnabled(true);

        });

        animation.setName(Values.threadNameOf.get("anim_turnLabelValuesTo"));
        animation.start();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        Jpanel1 = new JPanel();
        Jpanel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        Jpanel1.setMinimumSize(new Dimension(570, 250));
        Jpanel1.setPreferredSize(new Dimension(570, 250));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        Jpanel1.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setFocusable(false);
        tabbedPane1.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        panel1.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setMinimumSize(new Dimension(250, 197));
        panel2.setPreferredSize(new Dimension(250, 197));
        tabbedPane1.addTab("Input", panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txt_Lecture = new JTextField();
        txt_Lecture.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        txt_Lecture.setText("");
        panel3.add(txt_Lecture, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txt_Credit = new JTextField();
        txt_Credit.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        txt_Credit.setText("");
        panel3.add(txt_Credit, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cbb_PointSystem = new JComboBox();
        cbb_PointSystem.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Centesimal");
        defaultComboBoxModel1.addElement("FivePoint");
        defaultComboBoxModel1.addElement("Grade");
        cbb_PointSystem.setModel(defaultComboBoxModel1);
        panel3.add(cbb_PointSystem, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cbb_Standard = new JComboBox();
        cbb_Standard.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Basic");
        defaultComboBoxModel2.addElement("Canada");
        defaultComboBoxModel2.addElement("Improved1");
        defaultComboBoxModel2.addElement("Improved2");
        defaultComboBoxModel2.addElement("PKU");
        defaultComboBoxModel2.addElement("SJTU");
        defaultComboBoxModel2.addElement("USTC");
        cbb_Standard.setModel(defaultComboBoxModel2);
        cbb_Standard.setToolTipText("");
        panel3.add(cbb_Standard, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txt_Score = new JTextField();
        txt_Score.setFocusable(true);
        txt_Score.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        panel3.add(txt_Score, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        label1.setText("Standard");
        panel3.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        label2.setText("System");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setAlignmentX(0.0f);
        label3.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        label3.setText("Cource");
        panel3.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lbl_score_or_grade = new JLabel();
        lbl_score_or_grade.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        lbl_score_or_grade.setText("Score");
        panel3.add(lbl_score_or_grade, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        label4.setText("Credit");
        panel3.add(label4, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("MassInput", panel4);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        InputData = new JTextPane();
        InputData.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        InputData.setName("");
        scrollPane1.setViewportView(InputData);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        Jpanel1.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        courcePanel = new JPanel();
        courcePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(courcePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, -1), null, 0, false));
        courcePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Cource", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Arial Unicode MS", Font.PLAIN, 16), new Color(-16777216)));
        final JScrollPane scrollPane2 = new JScrollPane();
        courcePanel.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollPane2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        Lectures = new JList();
        Lectures.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        scrollPane2.setViewportView(Lectures);
        GPAPanel = new JPanel();
        GPAPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        GPAPanel.setToolTipText("");
        panel5.add(GPAPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        GPAPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "GPA", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Arial Unicode MS", Font.PLAIN, 16), new Color(-16777216)));
        detailsButton = new JButton();
        detailsButton.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        detailsButton.setText("Details");
        GPAPanel.add(detailsButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lbl_GPA = new JLabel();
        lbl_GPA.setFont(new Font("Arial Unicode MS", Font.PLAIN, 36));
        lbl_GPA.setForeground(new Color(-16777216));
        lbl_GPA.setText("wait for +1s");
        GPAPanel.add(lbl_GPA, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        Jpanel1.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        allClearButton = new JButton();
        allClearButton.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        allClearButton.setText("Clear All");
        panel6.add(allClearButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        removeButton = new JButton();
        removeButton.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        removeButton.setText("<<");
        panel6.add(removeButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addButton = new JButton();
        addButton.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        addButton.setText(">>");
        panel6.add(addButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel6.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel6.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        label1.setLabelFor(cbb_Standard);
        label2.setLabelFor(cbb_PointSystem);
        label3.setLabelFor(txt_Lecture);
        lbl_score_or_grade.setLabelFor(txt_Score);
        label4.setLabelFor(txt_Credit);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return Jpanel1;
    }
}