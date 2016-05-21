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

}