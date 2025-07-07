import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/*
Server:
- Inherits Thread class and overrides run method
- Define ServerSocket, and construct the Server with server socket and timeout (throws IOException)
- Main code captures port on which server listens on and tries to create the Server instance (listens for IOExceptions)
- Run method indefinitely tries to accept a connection from a client and return a server socket instance
- Server initializes its own input and output data streams
- Server sends initialization message to client and receives math operation and operands from client
- Based on math operation, calculate and write result to client. Break if operation is zero
- Listen for next inputs
- Close the server when done
- Catch SocketTimeoutException and IOException
 */

public class Server extends Thread {
    ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
    }

    public static void main(String args[]) {
        int port = Integer.parseInt(args[0]);
        
        try {
            Server server = new Server(port);
            server.start();
        }

        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                Socket connectedServerSocket = serverSocket.accept();

                DataInputStream dataInputStream = new DataInputStream(connectedServerSocket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(connectedServerSocket.getOutputStream());

                dataOutputStream.writeUTF("Connected on port: " + connectedServerSocket.getLocalPort());
                dataOutputStream.writeUTF(menu());

                int operation = dataInputStream.readInt();
                int operand1 = dataInputStream.readInt();
                int operand2 = dataInputStream.readInt();

                switch(operation) {
                    case 0:
                        dataOutputStream.writeUTF("Closing connection on port: " + connectedServerSocket.getLocalPort());
                        connectedServerSocket.close();
                        break;
                    
                    case 1:
                        dataOutputStream.writeInt(add(operand1, operand2));
                        break;

                    case 2:
                        dataOutputStream.writeInt(sub(operand1, operand2));
                        break;

                    case 3:
                        dataOutputStream.writeInt(mul(operand1, operand2));
                        break;

                    case 4:
                        dataOutputStream.writeDouble(div(operand1, operand2));
                        break;
                }
            }

            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String menu() {
        return "Please enter an operation and two operands";
    }
    
    private int add(int op1, int op2) {
        return op1 + op2;
    }

    private int sub(int op1, int op2) {
        return op1 - op2;
    }
    
    private int mul(int op1, int op2) {
        return op1 * op2;
    }

    private double div(int op1, int op2) {
        return (double)(op1 / op2);
    }
}
