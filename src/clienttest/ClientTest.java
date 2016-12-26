package clienttest;

import java.io.BufferedReader;
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
    private static String serverURL = "localhost" ;
    private static int serverPort = 9000 ;

    public static void main(String[] args) {
        
        System.out.println("Starting client...");      
        
        String serverMsg = "No message :(" ;
        
        try {
            Socket s = new Socket(serverURL, serverPort);
            
            System.out.println("Socket conection created. Getting data...");

            // Try fancy DataWriter to be able to distinguish between what's being sent
            DataOutputStream dOut ;
            
            PrintWriter outp = null;
            BufferedReader inp = null;
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
                
                //outp = new PrintWriter(s.getOutputStream(), true);
                // Receive message from server
                inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String line = "" ;
                while ((line = inp.readLine()) != null) {
                    serverMsg += line + "\n" ;
                }
                
                // Close the socket
                dOut.close() ;
            } catch (IOException e) {
                System.out.println("Error getting input/output stream:");
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.out.println("Error creating socket:");
            e.printStackTrace() ;
            return ;
        }
        
       
            System.out.println();
            System.out.println();
            System.out.println("Server message: " + serverMsg);
       
    }
}
