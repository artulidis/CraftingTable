import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

 public class Client {
    public static void main(String args[]) {
        Socket client = null;
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            client = new Socket(host, port);

            DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());

            Scanner userInput = new Scanner(System.in);

            System.out.println("Server says: " + dataInputStream.readUTF());
            
            // Print menu
            System.out.println(dataInputStream.readUTF());

            int operation, operand1, operand2;
            
            while(true) {
                System.out.print("Enter operation: ");
                operation = userInput.nextInt();

                if(operation == 0) break;

                System.out.print("Enter first operand: ");
                operand1 = userInput.nextInt();
                System.out.print("Enter second operand: ");
                operand2 = userInput.nextInt();

                dataOutputStream.writeInt(operation);
                dataOutputStream.writeInt(operand1);
                dataOutputStream.writeInt(operand2);

                System.out.println("Result: " + dataInputStream.readInt());
            }

            // Send final message to server before exiting gracefully
            dataOutputStream.writeInt(0);
            dataOutputStream.writeInt(0);
            dataOutputStream.writeInt(0);

            userInput.close();
            client.close();
            System.out.println("Connection closed gracefully");
        }

        catch(IOException e) {
            e.printStackTrace();
        }

        finally {
            try {
                client.close();
            }

            catch(Exception e) {
                System.out.println("Couldn't close socket with exception \": " + e + "\"");
            }
        }

    }
}