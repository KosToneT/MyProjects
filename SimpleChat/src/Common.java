import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Common {
    public static void main(String[] args){

        //ChatServer.startMain();
        //ClientWindow.startMain();

        new Thread(new Runnable() {

            @Override
            public void run() {
                ChatServer.startMain();
            }
            
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                ClientWindow.startMain();
            }
            
        }).start();

    }
}
class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener{

    private static String IP_ADDR = "192.168.43.31";
    private static int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;



    public static void startMain(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow(); 
            }
        });
    }
    
    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("User");
    private final JTextField fieldIP = new JTextField(IP_ADDR);
    private final JTextField fieldPORT = new JTextField(""+PORT);

    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

    private ClientWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        log.setEditable(false);
        log.setLineWrap(true);

        add(log, BorderLayout.CENTER);
        add(fieldInput, BorderLayout.SOUTH);

        Box jp = Box.createHorizontalBox();
        jp.add(fieldNickname);
        jp.add(fieldIP);
        jp.add(fieldPORT);
        jp.add(Box.createHorizontalGlue());
        jp.setMaximumSize(new Dimension(600,100));
        
        add(jp, BorderLayout.NORTH);
        setContentPane(jp);
        Box vertMain = Box.createVerticalBox();
        vertMain.add(jp);
        vertMain.add(log);
        fieldInput.setMaximumSize(new Dimension(600,100));
        vertMain.add(fieldInput);
        setContentPane(vertMain);

        //add(fieldNickname, BorderLayout.NORTH);
        //add(fieldIP, BorderLayout.NORTH);
        //add(fieldPORT, BorderLayout.NORTH);



        // try {
        //     connection = new TCPConnection(this, IP_ADDR, PORT);
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     printMsg("Connection exception "+ e);
        // }


        fieldInput.addActionListener(this);
        fieldIP.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
                    PORT = Integer.parseInt(fieldPORT.getText());
                    IP_ADDR = fieldIP.getText();
                    connection = new TCPConnection(ClientWindow.this, IP_ADDR, PORT);
                } catch (IOException ex) {
                    // TODO Auto-generated catch block
                    printMsg("Connection exception "+ ex);
                }
            }

        });
        

        setVisible(true);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready... ");
        
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
        
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");

        
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connecton excetpion"+ e);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.length()==0) return;
        fieldInput.setText(null);
        connection.sendString(fieldNickname.getText()+": "+msg);
    }

    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
                
            }
            
        });
    }


}


class ChatServer implements TCPConnectionListener{
    public static void startMain(){
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer(){
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("InetIP "+serverSocket.getInetAddress());
            while(true){
                try{
                    System.out.println("LoclInetIP "+serverSocket.getLocalSocketAddress());
                    
                    new TCPConnection(serverSocket.accept(), this);
                }catch(IOException e){
                    System.out.println("TCPConnection exception: "+ e);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connectedd: " + tcpConnection);
    }
    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }
    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }
    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
        
    }
    private void sendToAllConnections(String value){
        System.out.println(value);
        for(TCPConnection i:connections){
            i.sendString(value);
        }
    }
}


interface TCPConnectionListener{
    void onConnectionReady(TCPConnection tcpConnection);
    void onReceiveString(TCPConnection tcpConnection, String value);
    void onDisconnect(TCPConnection tcpConnection);
    void onException(TCPConnection tcpConnection, Exception e);
}

class TCPConnection{
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;

    private final BufferedReader in;
    private final BufferedWriter out;


    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port)throws IOException{
        this(new Socket(ipAddr, port), eventListener);
    }

    public TCPConnection(Socket socket, TCPConnectionListener eventListener) throws IOException{
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);

                    while(!rxThread.isInterrupted()){
                        String msg = in.readLine();
                        eventListener.onReceiveString(TCPConnection.this, msg);
                    }
                    


                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally{
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
            
        });
        rxThread.start();
    }

    public synchronized void sendString(String value){
        try {
            out.write(value+"\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }
    public synchronized void disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }
    @Override
    public String toString(){
        return "TCPConnection: "+ socket.getInetAddress() + ": "+socket.getPort();
    }
}
