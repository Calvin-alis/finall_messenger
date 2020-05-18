package messenger_network;

public interface TCPCOnnectionListener {


     void onConnectionReady(TCPConnection tcpConnection,String value);
     void onReceiveString(TCPConnection tcpConnection, String value);
     void onDisconnect(TCPConnection tcpConnection);
     void onException(TCPConnection tcpConnection, Exception e);
}
