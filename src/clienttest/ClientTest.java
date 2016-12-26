package clienttest;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Code for the client side, to connect to the server
 * 
 * @author M Thomas
 * @since 25/2/16
 */
public class ClientTest {
    
//    private static String serverURL = "thonners.ddns.net" ; 
//    private static int serverPort = 8080 ;
    private final static String serverURL = "localhost" ;
    private final static int serverPort = 9000 ;
    
    private final static int SEND = 1 ;
    private final static int RECEIVE = -1 ;

    /**
     * Main method - run when the project is executed. 
     * Provided an integer is passed, will create an instance of this class and 
     * either send or receive data to/from the server.
     * @param args Command-line arguments passed - should be an in, either '1' or '-1', for a send or receive type client connection respectively.
     */
    public static void main(String[] args) {
        // Try to parse the input, and create an instance if successful
        int clientType = SEND ;
        try {
            if (args.length == 0) {
                System.out.println("Warning: no arguement given, so assuming SEND client type.");
            } else {
                // Parse the input
                clientType = Integer.parseInt(args[0]) ;
                
                // Create a client instance to manage the rest of the show
                ClientTest client = new ClientTest(clientType) ;
            }
        } catch (Exception e) {
            System.out.println("Error parsing int. Illegal arguement given. Exiting.");    
            return ;
        }
    }
    
    /**
     * @param clientType 1 will start a 'send' client connection, to send data to the server, whilst '-1' will receive the data from the server.
     */
    public ClientTest(int clientType) {
        // Create socket connection
        switch(clientType) {
            case SEND: 
                initialiseSendClient() ;
                break;
            case RECEIVE: 
                initialiseReceiveClient() ;
                break ;
            default :
                
        }
        
    }
    
    /**
     * Creates a socket connection to the server and sends a series of messages.
     * TODO: Include feedback from server as to whether the messages have been received without error.
     */
    private void initialiseSendClient() {
        System.out.println("Starting send client...");     
        try {
            Socket s = new Socket(serverURL, serverPort);            
            System.out.println("Socket conection created. Sending data...");

            // DataOutputStream lets us be able to distinguish between what's being sent
            DataOutputStream dOut ;
            
            try {
                dOut = new DataOutputStream(s.getOutputStream());
                // Write the data
                
                // Send first message
                dOut.writeByte(1);
                dOut.writeUTF("First Message.");
                dOut.flush(); // Send off the data
                
                // Send second message
                dOut.writeByte(2);
                dOut.writeUTF("Second Message.");
                dOut.flush(); // Send off the data
                
                // Send third message
                dOut.writeByte(3);
                dOut.writeUTF("Third , and ");
                dOut.writeUTF("final message.");
                dOut.flush(); // Send off the data
                
                // Stop it listening for more
                dOut.writeByte(-1);
                dOut.flush();
                
                // Close the stream & socket                
                dOut.close() ;
                s.close();
            } catch (IOException e) {
                System.out.println("Error getting output stream:");
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.out.println("Error creating socket:");
            e.printStackTrace() ;
            return ;
        }
    }
    
    /**
     * Creates a socket connection to the server to receive messages.
     */
    private void initialiseReceiveClient() {        
        System.out.println("Starting receive client...");      
        // Initialise String to hold server's message
        String serverMsg = "No message from server :(" ;
        
        try {
            Socket s = new Socket(serverURL, serverPort);            
            System.out.println("Socket conection created. Sending data...");

            // DataOutputStream is required so we can tell the server we want some info back
            DataOutputStream dOut ;
            // DattaInputStream will be used to read the data from the server
            DataInputStream dIn ;
            
            try {
                dOut = new DataOutputStream(s.getOutputStream());
                dIn = new DataInputStream(s.getInputStream());
                
                // Tell the server we're expecting some data back
                dOut.writeByte(RECEIVE) ;
                dOut.flush() ;
                
                // Receive the response
                
                boolean done = false;
                while(!done) {
                    byte messageType = dIn.readByte();
                    System.out.println("Byte from server: " + messageType);
                    int messageCount = 0 ;
                    serverMsg = "" ;
                    switch(messageType) {
                        case RECEIVE: // Data received from server
                            String line = dIn.readUTF() ;
                            serverMsg += line +"\n" ;
                            System.out.println("String from server: " + line);
                            messageCount++ ;
                            done = true;
                            break;
                        default:
                            done = true;
                    }
                    serverMsg += messageCount + " messages received from the server in this instance" ;
                }

                // Close the streams & socket
                dOut.close() ;
                dIn.close();
                s.close();
            } catch (IOException e) {
                System.out.println("Error getting output stream:");
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.out.println("Error creating socket:");
            e.printStackTrace() ;
            return ;
        }
        
        
        System.out.println("");
        System.out.println("");
        System.out.println("Complete message from server: " + serverMsg);
    }
}
