package TCPWebServer.src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 Stateless application class that creates a ServerSocket on specified port, and indefinitely accepts new clients
 After a successful client connection, a Thread initialized with a Connection, passed with a ServerSocket, is created and started
 */

public class Server {
    public static void main(String args[]) {
        int port = Integer.parseInt(args[0]);
        
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(100000);

            while(true) {
                Socket connectionSocket = serverSocket.accept();
                Thread connection = new Thread(new Connection(connectionSocket));
                connection.start();
            }
        }
        catch(IOException e) { e.printStackTrace(); }

    }
}
