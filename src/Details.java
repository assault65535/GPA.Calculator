import javax.swing.*;
import java.awt.event.*;

public class Details extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel detailLabel;

    public Details(JPanel rootPanel,String standard) {
        setLocationRelativeTo(rootPanel);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setTitle("Details");
        this.detailLabel.setText(standard);

        buttonOK.addActionListener(e -> onOK());
    }

    private void onOK() {
        dispose();
    }


    public static void generateDetails(JPanel where, String arg) {
        Details dialog = new Details(where,arg);
        dialog.pack();
        dialog.setVisible(true);
    }
}
