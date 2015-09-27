/**
 *	TCP Server Program
 *	Listens on a TCP port
 *	Receives a line of input from a TCP client
 *	Returns an upper case version of the line to the client
 *
 *	@author: Michael Yuan
@	version: 2.0
 */
import java.io.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.System;
import java.net.*;
import java.lang.*;
import java.net.InetAddress;

class TCPchat {
    public static Socket greenSocket, yellowSocket = null;
    public static InetAddress green, yellow = null;

    public static void main(String argv[]) throws Exception
    {
        ServerSocket welcomeSocket = null;

        try
        {
            welcomeSocket = new ServerSocket(6789);
        }

        catch(Exception e)
        {
            System.out.println("Failed to open socket connection");
            System.exit(0);
        }


        while(greenSocket == null || yellowSocket == null) {
            // Get green and get yellow to connect
            Socket connectionSocket = welcomeSocket.accept();
            String helloMessage = getString(connectionSocket);
            System.out.println( helloMessage + "\n - " + connectionSocket.getInetAddress().toString());
            if(helloMessage.substring(0,3).equals("HELO")){
                if(green == null && !connectionSocket.getInetAddress().equals(yellow)){
                    System.out.println(connectionSocket.getInetAddress().toString() + " identified as Green Client");
                    green = connectionSocket.getInetAddress();
                    greenSocket = connectionSocket;
                    String toGreen100 = "100 Welcome Green Client. Waiting for Yellow Client...";
                    System.out.println("Sending to Green: " + toGreen100);
                    sendString(toGreen100, greenSocket);
                } else if (yellow == null && !connectionSocket.getInetAddress().equals(green)){
                    System.out.println(connectionSocket.getInetAddress().toString() + " identified as Yellow Client");
                    yellow = connectionSocket.getInetAddress();
                    yellowSocket = connectionSocket;
                    String toYellow200 = "200 Welcome Yellow Client. Waiting for Green Client to send the first message...";
                    String toGreen200 = "200 Tellow Client connected! You get to send the first message.";
                    System.out.println("Sending to Yellow: " + toYellow200);
                    System.out.println("Sending to Green: " + toGreen200);
                    sendString(toYellow200, yellowSocket);
                    sendString(toGreen200, greenSocket);
                } else {
                    System.out.println( connectionSocket.getInetAddress().toString() + " sent a duplicate HELO message\n");
                }
            }

            while(greenSocket != null && yellowSocket != null){ //At this point, both clients are connected.
                String toYellow, toGreen = null;
                toYellow = getString(greenSocket);
                sendString(toYellow, yellowSocket);
                if(toYellow.startsWith("300")){//Goodbye message
                    break;
                }

                toGreen = getString(yellowSocket);
                sendString(toGreen, greenSocket);
                if(toGreen.startsWith("300")){//Goodbye message
                    break;
                }
            }
        }
        greenSocket.close();
        yellowSocket.close();
    }

    public static boolean sendString(String str, Socket socket) throws IOException{
        if(socket != null) {
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            outToClient.writeBytes(str);
            return true;
        } else {
            return false;
        }
    }

    public static String getString(Socket socket) throws IOException{
        if(socket != null){
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return inFromClient.readLine();
        } else {
            return "404 Socket not found";
        }
    }
}