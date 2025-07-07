import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

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