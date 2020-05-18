package messenger_network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {

    private  Socket socketmy;
    private  Thread myThread;
    private TCPCOnnectionListener EventsListener;
    private  BufferedReader into;
    private  BufferedWriter from;

    public TCPConnection(TCPCOnnectionListener EventsListener, String ipAdr, int port) throws IOException {
        this(new Socket(ipAdr, port), EventsListener);
    }

    public TCPConnection(Socket socketmy, TCPCOnnectionListener EventsListener) throws IOException {
        this.socketmy = socketmy;
        this.EventsListener = EventsListener;
        into = new BufferedReader(new InputStreamReader(socketmy.getInputStream()));
        from = new BufferedWriter(new OutputStreamWriter(socketmy.getOutputStream()));
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String ThreadLine= into.readLine();

                    EventsListener.onConnectionReady(TCPConnection.this,ThreadLine );
                    while (!myThread.isInterrupted()) {
                        String mag = into.readLine();

                        EventsListener.onReceiveString(TCPConnection.this, mag);
                    }
                    String msg = into.readLine();

                } catch (IOException e) {

                    EventsListener.onException(TCPConnection.this, e);
                } finally {

                    EventsListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        myThread.start();
    }


    public synchronized void sedString(String value) {
        try {
            from.write(value + "\r\n");
            from.flush();
        } catch (IOException e) {
            EventsListener.onException(TCPConnection.this, e);
            disconnect();
        }

    }

    public synchronized void disconnect() {
        myThread.interrupt();

        try {
            socketmy.close();
        } catch (IOException e) {
            EventsListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socketmy.getInetAddress() + socketmy.getPort();
    }
}