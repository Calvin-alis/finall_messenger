package messenger_client;

import com.google.gson.Gson;
import messenger_network.TCPCOnnectionListener;
import messenger_network.TCPConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

class Message {
    private String TYPE;
    private String Sender;
    private String USERNAME_TO;
    private String TEXT;
    private ArrayList<String> NAMELIST;

    public Message(String TYPE, String Sender, String USERNAME_TO,String TEXT, ArrayList<String> NAMELIST) {
        this.TYPE = TYPE;
        this.Sender = Sender;
        this.USERNAME_TO = USERNAME_TO;
        this.TEXT = TEXT;
        this.NAMELIST = NAMELIST;
    }

    public String getTYPE() {
        return TYPE;
    }
    public String getSender() {
        return Sender;
    }
    public String getUSERNAME_TO() {
        return USERNAME_TO;
    }
    public String getTEXT() {
        return TEXT;
    }

    public ArrayList<String> getNAMELIST() {
        return NAMELIST;
    }
}

public class ClientWindow extends JFrame implements ActionListener, TCPCOnnectionListener  {


    static String IP_ADDR = null/*"192.168.1.220"*/;

    private static final int PORT = 8882;
    private static final int WIDTH = 400;
    private static final int HIGHT = 300;
    private static String UserName_To = null;
    String UserName ;
    /*System.getProperty("user.name");*/
    private static String type = null;
    private static ArrayList<String> name_list = new ArrayList<String>();


    final JFrame form2 = new JFrame();
    final JTextField name = new JTextField();
    final JTextField ip = new JTextField();
    final JButton next = new JButton();



     final JFrame form1 = new JFrame();
     final JTextArea log = new JTextArea();
     final JTextField fieldinput = new JTextField();
     final DefaultListModel listModel = new DefaultListModel();
     final JList list = new JList(listModel);



     TCPConnection connection;
     ClientWindow(String ipText,String nameText){

         this.IP_ADDR = ipText;
         this.UserName = nameText;

        form1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form1.setSize(WIDTH,HIGHT);
        form1.setLocationRelativeTo(null);

        log.setEditable(false);
        log.setLineWrap(true);
        fieldinput.addActionListener(this);

        form1.add(log, BorderLayout.CENTER);
        JScrollPane scroll = new JScrollPane(log);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        form1.add(fieldinput, BorderLayout.SOUTH);

        listModel.addElement("To all");
        list.setFocusable(false);
         JTextField fieldnickname = new JTextField(UserName);
         fieldnickname.setEditable(false);
        form1.add(fieldnickname, BorderLayout.NORTH);
        form1.add(list, BorderLayout.EAST);
        form1.add(scroll);
        list.addMouseListener(new CustomListener());

        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);

            String msg = null;
            Message message = new Message("name_registration",UserName, UserName_To, msg, null);
            String json = new Gson().toJson(message);
            connection.sedString(json);
            System.out.println("name register "+json);


            String name = "Admin";
            String msg1 = UserName + " connected";
            Message message1 = new Message("admin_message",name, UserName_To, msg1, null);
            String json1 = new Gson().toJson(message1);
            connection.sedString(json1);
            System.out.println("admin "+json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        form1.setVisible(true);

        Message message = new Message("refresh", UserName,null,null,null);
        Thread refresh = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true){
                        String json = new Gson().toJson(message);
                        connection.sedString(json);
                        Thread.sleep(1000);}
                    } catch (InterruptedException e) {
                    e.printStackTrace();
                    }
            }
        });
        refresh.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(UserName_To == null) {
            type = "all";
            String msg = fieldinput.getText();
            if (msg.equals("")) return;
            fieldinput.setText(null);
            Message message = new Message(type, UserName, UserName_To, msg, null);
            String json = new Gson().toJson(message);
            connection.sedString(json);
        }
        if(UserName_To != null){
            if(UserName_To == "To all") {
                type = "all";
                String msg = fieldinput.getText();
                if (msg.equals("")) return;
                fieldinput.setText(null);
                Message message = new Message(type, UserName, null, msg, null);
                String json = new Gson().toJson(message);
                connection.sedString(json);
            } else {
                type = "private";
                String msg = fieldinput.getText();
                if (msg.equals("")) return;
                fieldinput.setText(null);
                Message message = new Message(type, UserName, UserName_To, msg, null);
                String json = new Gson().toJson(message);
                connection.sedString(json);
            }
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection, String value) {


        System.out.println("OnConnectionReady" + value);
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {

        Message message = new Gson().fromJson(value, Message.class);
        System.out.println("Параметры потока в определенный момент времени:"+value);
        name_list = message.getNAMELIST();
        System.out.println("Массив имен вошедших пользователей:"+name_list);

        if(message.getTYPE() != "Names_list" && message.getTEXT() != null) printMeg(value);
        if(message.getTYPE() == "Names_list") {
            name_list = message.getNAMELIST();
            System.out.println("client name list "+name_list);
            int lenght = name_list.size();
            listModel.removeAllElements();
            listModel.addElement("To all");
             for(int i = 0; i<lenght; i++){
                listModel.addElement(name_list.get(i));
             }
        }if(message.getSender() == null) {
            if(UserName_To == "To all")
            name_list = message.getNAMELIST();
            System.out.println("client name list "+name_list);
            int lenght = name_list.size();
            listModel.removeAllElements();
            listModel.addElement("To all");
            for(int i = 0; i<lenght; i++) {
                listModel.addElement(name_list.get(i));

            }
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {

        System.out.println("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("The exceptions of connections" + e);
    }


     synchronized  void printMeg (String mag){

        Message message1 = new Gson().fromJson(mag, Message.class);

        String name = message1.getSender();
        String text = message1.getTEXT();
        String name_to = message1.getUSERNAME_TO();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {


                if(message1.getSender() != null) {
                    if (name_to != null) {

                        log.append(name + " to " + name_to + " : " + text + "\n");
                        log.setCaretPosition(log.getDocument().getLength());
                    }
                    if (name_to == null) {
                        log.append(name + " : " + text + "\n");
                        log.setCaretPosition(log.getDocument().getLength());
                    }
                }
            }
        });
    }

    private class CustomListener implements java.awt.event.MouseListener  {
        @Override
        public void mouseClicked(MouseEvent e) {
            String name_to = (String)list.getSelectedValue();
           if(name_to != null){
               UserName_To = name_to;
            System.out.println("pereslat lichno "+name_to);
           }
           else {
               UserName_To = null;
               System.out.println("pereslat v obchiy" + name_to);
           }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
