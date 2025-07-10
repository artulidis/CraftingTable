package TCPWebServer.src;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;

/*
 Client class implements Runnable (Thread) interface with a Run method. Client Socket, and request/redirect HashMaps are initialized
 
 Request map stores keys/values for client requests
 Redirect map stores keys that need to be redirected to corresponding values

 Constructor initializes the passed-in client socket, and initializes request/redirect HashMaps
 Constructor initializes redirect map for HTTP 301 simulation

 Run method simply parses a request, sends a response, and closes the socket connection
 Catch IOException errors



 Request parser method is created that parses client requests and inserts all request fields into the request map
 Throws IOException error if client socket is not present and inputStream is not available
 A BufferedReader is connected to the client socket's input stream and reads it its request data

 Read the top request line of the client. ex: GET/index.html HTTP/1.1

 Start parsing the request line if it exists
 Capture member Strings of the top request line, separated by spaces, into an array
 Extract the relevant information from the top line of the request. Method, Resource URL, and Protocol
 Add the Method, Resource, and Protocol to the request HashMap
 Read the next line of the client request header
 Loop while the request header still has lines to read
 Continue reading and storing the values of each request field into the request HashMap
 
 To store values of the request in the map, split the request field into its key/value pair:
 Put the request field key and value into the request map,
 Read the next line of the request



 Send response method is created that sends appropriate response based on the client request
 
 If the URL requested is inside of the redirect HashMap, the client is sent a HTTP 301 error redirect response
 The client is sent to a new URL location.

 If the URL requested is not inside of the redirect HashMap and does not exist, an HTTP 404 error response is sent
 If the URL request exists, an HTTP 200 OK response is sent

 Method throws IOException method if outStream, fileStream, or bufInputStream is closed or does not exist while being used

 A DataOutputStream is created
 
 File path of the file requested by the client connection is captured and the requested File is opened
 If the requested file is in the redirect HashMap, send the client an HTTP 301 response
 Client is redirected to the new file address

 If the file requested does not exist, send the client an HTTP 404 response and a 404 webpage
 (Experiment with \r and \r\n)
 Send the HTTP 404 response to the client using the UTF-8 encoding

 If a file is not in the redirect HashMap and exists, send an HTTP 200 response and serve the file
 Open a file input stream to read data from the file
 Get the MIME file type of the file that the client is requesting
 Create a BufferedInputStream to read data from the fileStream
 Create an array of bytes the same length of the file requested to hold the file data bytes
 Send the header of the HTTP 200 response
 Read in the data from the file requested into the bytes array
 Send the data contained by the bytes array to the client connection and flush the output stream
 Close the output stream
 */

public class Connection implements Runnable {
    Socket connectionSocket;

    HashMap<String, String> request;
    HashMap<String, String> redirect;

    public Connection(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;

        request = new HashMap<>();
        redirect = new HashMap<>();

        redirect.put("/", "/indexx.html");
        redirect.put("/index", "/indexx.html");
        redirect.put("/index.html", "/indexx.html");

    }


    public void parseRequest() {
        try {
            DataInputStream inputBuffer = new DataInputStream(connectionSocket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputBuffer));

            String topLine = bufferedReader.readLine();

            if(topLine != null) {
                String[] requestParams = topLine.split(" ");
                request.put("Method", requestParams[0]);
                request.put("Resource URL", requestParams[1]);
                request.put("Protocol", requestParams[2]);
            }
            
            String headerLine = bufferedReader.readLine(); 

            while(!headerLine.isEmpty()) {
                String[] headerParams = headerLine.split(":", 2);
                request.put(headerParams[0], headerParams[1].replaceFirst(" ", ""));
                headerLine = bufferedReader.readLine();
            }
        }
        catch(IOException e) { e.printStackTrace(); }
    }


    public void sendResponse() {
        try {
            DataOutputStream outputBuffer = new DataOutputStream(connectionSocket.getOutputStream());
            String requestedFileName = request.get("Resource URL").toString();
            File file = new File(".." + requestedFileName);

            if(redirect.get(requestedFileName) != null) {
                System.out.println("HELLO");
                outputBuffer.writeBytes("HTTP/1.1 301 Moved Permanently \n" + "Location: " + redirect.get(requestedFileName));
            }

            else if(!file.exists()) {

                System.out.println(file.toString());

                String http404Response = "HTTP/1.1 404 Not Found\r\n\r\n" + "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "    <title>TCP Server</title>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body><h1>\n" +
                    "404 Error: Page Not Found\n" +
                    "</h1></body>\n" +
                    "\n" +
                    "</html>";

                // Send the HTTP 404 response to the client using the UTF-8 encoding
                outputBuffer.write(http404Response.getBytes("UTF-8"));
            }

            else {
                FileInputStream fileInputStream = new FileInputStream(file);
                String contentType = Files.probeContentType(file.toPath());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                byte[] byteArr = new byte[(int)file.length()];
                outputBuffer.writeBytes("HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\n\r\n");
                bufferedInputStream.read(byteArr);
                outputBuffer.write(byteArr);
                outputBuffer.flush();
                outputBuffer.close();
            }

        }
        catch(IOException e) { e.printStackTrace(); }
    }


    public void run() {
        try {
            parseRequest();
            sendResponse();
            connectionSocket.close();
        }
        catch(IOException e) { e.printStackTrace(); }
    }
}
