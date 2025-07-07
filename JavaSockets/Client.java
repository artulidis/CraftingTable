import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.NoSuchElementException;

/* 
Client:
- Stateless class
- Define socket, host, and port
- Try to initialize socket, data streams, input scanner, and print out server connection message
- Scan for the math operation, and the two operands
- Send scanned inputs to server
- Print server result
- Look for IOException, NoSuchElementException
- Finally, try to close the connection
 */

 class Client {
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

            int operation, operand1, operand2;
            
            do {
                System.out.print("Enter operation: ");
                operation = userInput.nextInt();
                System.out.print("Enter first operand: ");
                operand1 = userInput.nextInt();
                System.out.print("Enter second operand: ");
                operand2 = userInput.nextInt();

                dataOutputStream.writeInt(operation);
                dataOutputStream.writeInt(operand1);
                dataOutputStream.writeInt(operand2);

                System.out.println("Result: " + dataInputStream.readInt());
            }

            while(operation != 0);

            userInput.close();

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