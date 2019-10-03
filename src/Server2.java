import java.awt.*;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Server2 {

    private static final int sPort = 8000;   //The server will be listening on this port number

    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(sPort);
        int clientNum = 1;
        try {
            while(true) {
                new Handler(listener.accept(),clientNum).start();
                System.out.println("Client "  + clientNum + " is connected!");
                clientNum++;
            }
        } finally {
            listener.close();
        }

    }

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for dealing with a single client's requests.
     */
    private static class Handler extends Thread {
        private String message;    //message received from the client
        private String MESSAGE="";    //uppercase message send to the client
        private Socket connection;
        private ObjectInputStream in;	//stream read from the socket
        private ObjectOutputStream out;    //stream write to the socket
        private int no;		//The index number of the client

        public Handler(Socket connection, int no) {
            this.connection = connection;
            this.no = no;
        }

        public boolean Login(String Username, String Password){
            if (Username.equals("user1")) {
                if (Password.equals("1")) {
                    return true;
                } else {
                    return false;
                }
            }else{
                System.out.println("Incorrect User");
                return false;
            }
        }

        public String[] getFilenames(){
            File file=new File(".");
            String [] names=file.list();
            for (String name : names) {
                System.out.println(name);
            }
            return names;
        }

        public void run() {
            String UserName;
            boolean loginStatus=false;
            String command;
            try{
                //initialize Input and Output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                out.flush();
                in = new ObjectInputStream(connection.getInputStream());
                try{
                    while(true)
                    {
                        //receive the message sent from the client
                        message = (String)in.readObject();
                        String[] temp;
                        temp=message.split(" ");
                        command=temp[0];
                        //System.out.println(command);
                        switch (command) {
                            case ("login"):
                                //System.out.println("recognize 1");
                                String message = "input username and password";
                                sendMessage("success");
                                loginStatus=true;
                                break;
                            case ("dir"): {
                                if (!loginStatus) {
                                    sendMessage("1");
                                    break;
                                } else {
                                    //System.out.println("dir test");
                                    //MESSAGE=MESSAGE+"\n"+MESSAGE;
                                    String[] result= getFilenames();
                                    for (String name:result){
                                       System.out.println(name);
                                       MESSAGE=MESSAGE+"\n"+name;
                                   }
                                    sendMessage(MESSAGE);
                                    break;
                                }
                            }
                            case ("get"): {
                                if (!loginStatus) {
                                    sendMessage("1");
                                    break;
                                } else {
                                    File dir=new File(".");
                                    String directory=dir.getCanonicalPath();
                                    //System.out.println("get test");
                                    String filename=temp[1];
                                    String filepath=directory+"/"+filename;
                                    System.out.println(filepath);
                                    sendMessage("get test");
                                    break;
                                }
                            }
                            case ("upload"): {
                                if (!loginStatus) {
                                    sendMessage("1");
                                    break;
                                } else {
                                    System.out.println("upload test");
                                    sendMessage("upload test");
                                    break;
                                }
                            }

                        }
                        //show the message to the user
                        System.out.println("Receive message: " + message + " from client " + no);
                        //Capitalize all letters in the message
                        //MESSAGE = message.toUpperCase();
                        //send MESSAGE back to the client
                        //sendMessage(MESSAGE);
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }
            catch(IOException ioException){
                System.out.println("Disconnect with Client " + no);
            }
            finally{
                //Close connections
                try{
                    in.close();
                    out.close();
                    connection.close();
                }
                catch(IOException ioException){
                    System.out.println("Disconnect with Client " + no);
                }
            }
        }

        //send a message to the output stream
        public void sendMessage(String msg)
        {
            try{
                out.writeObject(msg);
                out.flush();
                System.out.println("Send message: " + msg + " to Client " + no);
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }

    }

}
