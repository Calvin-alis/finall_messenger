package messenger_client;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.EventQueue;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.Insets;


public class loginPage extends JFrame {

    private JPanel PanelOfContent;
    private JTextField IPField;
    private JTextField NameField;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                    loginPage Border = new loginPage();
                    Border.setVisible(true);
            }
        });
    }


    public loginPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(900, 140, 300, 150);
        PanelOfContent = new JPanel();
        PanelOfContent.setBorder(new EmptyBorder(30, 10, 30, 10));
        setContentPane(PanelOfContent);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{10, 10, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        PanelOfContent.setLayout(gbl_contentPane);

        JLabel lblNickname = new JLabel("A name of the user");
        GridBagConstraints gbc_lblNickname = new GridBagConstraints();
        gbc_lblNickname.anchor = GridBagConstraints.WEST;
        gbc_lblNickname.insets = new Insets(0, 0, 5, 0);
        gbc_lblNickname.gridx = 0;
        gbc_lblNickname.gridy = 4;
        PanelOfContent.add(lblNickname, gbc_lblNickname);

        IPField = new JTextField();
        GridBagConstraints gbc_IPField = new GridBagConstraints();
        gbc_IPField.insets = new Insets(0, 0, 5, 0);
        gbc_IPField.fill = GridBagConstraints.HORIZONTAL;
        setSize(400,150);
        gbc_IPField.gridx = 2;
        gbc_IPField.gridy = 4;
        PanelOfContent.add(IPField, gbc_IPField);
        IPField.setColumns(10);

        JLabel lblNickname1 = new JLabel("An IP of a server");
        GridBagConstraints gbc_lblip = new GridBagConstraints();
        gbc_lblip.anchor = GridBagConstraints.WEST;
        gbc_lblip.insets = new Insets(0, 0, 5, 0);
        gbc_lblip.gridx = 0;
        gbc_lblip.gridy = 1;
        PanelOfContent.add(lblNickname1, gbc_lblip);

        NameField = new JTextField();
        GridBagConstraints gbc_NameField = new GridBagConstraints();
        gbc_NameField.insets = new Insets(0, 0, 5, 0);
        gbc_NameField.fill = GridBagConstraints.HORIZONTAL;
        gbc_NameField.gridx = 2;
        gbc_NameField.gridy = 1;
        PanelOfContent.add(NameField, gbc_NameField);
        NameField.setColumns(10);

        JButton btnNewButton = new JButton("Login");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.gridx = 15;
        gbc_btnNewButton.gridy = 10;
        PanelOfContent.add(btnNewButton, gbc_btnNewButton );
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ipText = NameField.getText();
                String nameText = IPField.getText();
                new ClientWindow(ipText,nameText);
            }
        });

    }

}