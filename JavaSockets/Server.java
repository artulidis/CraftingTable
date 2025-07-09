import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

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
        try {
            Socket connectedServerSocket = serverSocket.accept();

            DataInputStream dataInputStream = new DataInputStream(connectedServerSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(connectedServerSocket.getOutputStream());

            dataOutputStream.writeUTF("Connected on port " + connectedServerSocket.getLocalPort());
            dataOutputStream.writeUTF(menu());

            boolean connectionIsLive = true;

            while(connectionIsLive) {
                int operation = dataInputStream.readInt();
                int operand1 = dataInputStream.readInt();
                int operand2 = dataInputStream.readInt();

                switch(operation) {
                    case 0:
                        connectionIsLive = false;
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
                        dataOutputStream.writeInt(div(operand1, operand2));
                        break;
                }
            }

            System.out.println("Server shutting down...");
        }

        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String menu() {
        return "OPERATIONS: \t [1] + \t [2] - \t [3] * \t [4] /";
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

    private int div(int op1, int op2) {
        return op1 / op2;
    }
}
