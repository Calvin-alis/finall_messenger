package messenger_server;

import com.google.gson.Gson;
import messenger_network.TCPConnection;
import messenger_network.TCPCOnnectionListener;

import java.util.ArrayList;
import java.net.ServerSocket;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

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

public class ChatServer implements TCPCOnnectionListener {

    Integer Connection_Send_Index = null;
    String UserName_To = null;
    String SenderEmail = null;
    String Text = null;
    private String Type = null;
    int cnt = 0;




    public static void main(String[] args) {

        new ChatServer();


    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private final Map<String, Integer> connection_list = new HashMap<String, Integer>();

    private ArrayList<String> name_list = new ArrayList<String>();




    private  ChatServer(){
        System.out.println("Server has been successfully run!");
        try (ServerSocket ServSock = new ServerSocket(8882)) {
            while(true) {
                try {
                    new TCPConnection(ServSock.accept(), this);
                } catch (IOException e) {
                    System.out.println("TCPConnection exeption" + e);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection, String value) {

        connections.add(tcpConnection);




        Message message1 = new Gson().fromJson(value, Message.class);
        SenderEmail = message1.getSender();

        name_list.add(SenderEmail);
        System.out.println("nameList"+name_list);
        connection_list.put(SenderEmail,cnt );
        System.out.println("connectionlist"+connection_list);
        System.out.println(name_list);
        System.out.println(connections);
        System.out.println(name_list.indexOf(SenderEmail));
        System.out.println(connections.indexOf(tcpConnection));

        sendToAllConnections("user_conected","Admin", null, null, name_list);

        System.out.println(name_list);
        cnt = connections.size();
        System.out.println("это просто индекс "+cnt);

    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {

        Message message1 = new Gson().fromJson(value, Message.class);

        Type = message1.getTYPE();
        SenderEmail = message1.getSender();
        UserName_To = message1.getUSERNAME_TO();
        Text = message1.getTEXT();
        System.out.println(Type);


        sendToAllConnections( Type,SenderEmail, UserName_To, Text, name_list);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        int indexEl = connections.indexOf(tcpConnection);
        int i = connections.size();


        String name = name_list.get(indexEl);
        name_list.remove(indexEl);
        connection_list.remove(name);
        connections.remove(tcpConnection);


       System.out.println("..."+name);
        connection_list.remove(name);


        System.out.println("This Name_List need to be "+name_list);

        sendToAllConnections("User has been disconnected",null, null, null, name_list);
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {


        System.out.println("The exceptions of TCPConnection " + e);
    }
    private void sendToAllConnections(String Type,String SenderEmail,String UserName_To,String Text, ArrayList<String> name_list){

        final int cnt = connections.size();

        if(Type == "refresh"){
            Connection_Send_Index =name_list.indexOf(SenderEmail);
            Message message1 = new Message("name_list", null, null, null, name_list);
            String json1 = new Gson().toJson(message1);
            connections.get(Connection_Send_Index).sedString(json1);
        } else {

            Message message = new Message(Type, SenderEmail, null, Text, name_list);
            String json = new Gson().toJson(message);
            if (UserName_To == null) {

                for (int i = 0; i < cnt; i++) {
                    connections.get(i).sedString(json);
                    System.out.println(json);
                }
            }
            if (UserName_To != null) {


                Connection_Send_Index = name_list.indexOf(UserName_To);
                Message message1 = new Message(Type,SenderEmail, UserName_To, Text, name_list);
                String json1 = new Gson().toJson(message1);
                connections.get(Connection_Send_Index).sedString(json1);


            }


            Message message2 = new Message("name_list", null, null, null, name_list);
            String json2 = new Gson().toJson(message2);
            for (int i = 0; i < cnt; i++) {
                connections.get(i).sedString(json2);
                System.out.println("json on connection " + json2);
            }
        }

    }
}
