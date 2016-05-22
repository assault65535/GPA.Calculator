import Models.GPAMethods.GradeIndenifier.GradeIdentifier;
import Models.GPAMethods.LevelIdentifier.ScoreIdentifier;
import Resources.Values;

import javax.swing.*;
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

        allClearButton.addActionListener(e -> {
            stopAnimationTrigger = true;
            deleteAllData();
        });

        cbb_PointSystem.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setStandardComboBoxAlternativesTo(Values.inUsePointSystems.get(e.getItem() + "System"));
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
            Details.generateDetails("踏马不写了");
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

        stopAnimationTrigger = false;

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

        stopAnimationTrigger = false;

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
            stopAnimationTrigger = true;
        }
    }

    private boolean stopAnimationTrigger = false;

    private void deleteAllData() {

        Lectures.setModel(new DefaultListModel());
        numericDatas.clear();
        nonNumericDatas.clear();

        txt_Lecture.setText(null);
        txt_Score.setText(null);
        txt_Credit.setText(null);
        InputData.setText(null);

    }

    private void setStandardComboBoxAlternativesTo(String[] alternatives) {
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

        turnLabelValuesAnimation(ans);
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

        turnLabelValuesAnimation(ans);
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

    private void turnLabelValuesAnimation(double toValue) {

        toValue = Math.abs(toValue);

        if (toValue != 0) {
            while (toValue < 100.0) {
                toValue *= 10.0;
            }
        }

        final int digit = (int) toValue;

        Thread animation = new Thread(() -> {

            double offset;
            final int MAXR = 240, MAXG = 190, MAXV = 430, collapse = 150;
            double L, R, G;

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

                        L = ((offset - collapse) > 0 ? (offset - collapse) : 0) / (MAXV - collapse) * (MAXG + MAXR);

                        R = L > MAXR ? (MAXG + MAXR - L) : MAXR;
                        G = L > MAXG ? MAXG : L;

                        lbl_GPA.setForeground(new Color(((int) R), ((int) G), 0));

                        if (offset == digit) {
                            return;
                        }

                        if (stopAnimationTrigger) {
                            lbl_GPA.setForeground(new Color(0,0,0));
                            lbl_GPA.setText(Values.lbl_GPATextIfEmpty);
                            return;
                        }

                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        animation.setName(Values.threadNameOf.get("turnLabelValuesAnimation"));
        animation.start();
    }

}