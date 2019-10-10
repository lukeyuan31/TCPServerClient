package Client;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;


public class Client2 {
    Socket requestSocket;           //socket connect to the server
    ObjectOutputStream out;         //stream write to the socket
    ObjectInputStream in;          //stream read from the socket
    String message;                //message send to the server
    String MESSAGE;                //capitalized message read from the server
    boolean loginStatus=false;
    boolean connectionStatus=false;

    public void Client() {}

    void run()
    {
        try{
            //create a socket to connect to the server
            //requestSocket = new Socket("localhost", 8000);
            //System.out.println("Connected to localhost in port 8000");
            //initialize inputStream and outputStream
           // out = new ObjectOutputStream(requestSocket.getOutputStream());
            //out.flush();
            //in = new ObjectInputStream(requestSocket.getInputStream());
            /*
            FileInputStream fis=new FileInputStream("/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Client/test.txt");
            byte[] bytes = new byte[1024];
            int data;
            while((data = fis.read(bytes))!= -1){
                out.write(bytes, 0, data);
            }
            fis.close();
            */

            //get Input from standard input
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(true)
            {
                /*
                if(requestSocket==null){
                    requestSocket = new Socket("localhost",8000);
                    out = new ObjectOutputStream(requestSocket.getOutputStream());
                    out.flush();
                    in = new ObjectInputStream(requestSocket.getInputStream());
                }
                else {
                    */

                    //System.out.print("Hello, please input a sentence: ");
                    System.out.println("\nPlease input one of the command \nftpclient [ip] [port]\nlogin\ndir\nget [filename]\nupload [filename]"  );
                    //read a sentence from the standard input
                    message = bufferedReader.readLine();
                    String[] inputline=message.split(" ");
                    String command=inputline[0];
                    //Send the sentence to the server
                    //sendMessage(message);
                    switch (command) {
                        case ("ftpclient"):{
                            if (connectionStatus == true){
                                System.out.println("Already connected");
                            }else {
                                String ip = inputline[1];
                                String host = inputline[2];
                                if (ip.equals("localhost") && host.equals("8000")) {
                                    requestSocket = new Socket("localhost", 8000);
                                    out = new ObjectOutputStream(requestSocket.getOutputStream());
                                    out.flush();
                                    in = new ObjectInputStream(requestSocket.getInputStream());
                                    connectionStatus = true;
                                    break;
                                } else {
                                    System.out.println("Wrong ip or host!");
                                    break;
                                }
                            }
                        }
                        case ("login"): {
                            if (connectionStatus == true) {
                                if (loginStatus == false) {
                                    sendMessage("login");
                                    System.out.println("Please input your username");
                                    String username = bufferedReader.readLine();
                                    sendMessage(username);
                                    System.out.println("Please input yout password");
                                    String password = bufferedReader.readLine();
                                    sendMessage(password);
                                    String response = (String) in.readObject();
                                    if (response.equals("success")) {
                                        System.out.println("Login successfully!");
                                        loginStatus = true;
                                    } else if (response.equals("fail")) {
                                        System.out.println("Unmatched username and password");
                                    }
                                    break;
                                } else {
                                    System.out.println("You are already logged in");
                                    break;
                                }
                            }else {
                                System.out.println("You have not connect to the server");
                                break;
                            }
                        }
                        case ("dir"): {
                            if (loginStatus == true) {
                                sendMessage("dir");
                                String response = (String) in.readObject();
                                System.out.println(response);
                                break;
                            } else {
                                System.out.println("You need to login first");
                                break;
                            }
                        }
                        case ("get"): {
                            if (loginStatus == false) {
                                System.out.println("You need to login first");
                                break;
                            } else {
                                sendMessage("get");
                                //System.out.println("Input the name of the file you want to get");
                                //String filename = bufferedReader.readLine();
                                String filename=inputline[1];
                                sendMessage(filename);
                                String response = (String) in.readObject();
                                if (response.equals("true")) {
                                    File dir = new File(filename);
                                    String directory =dir.getAbsolutePath();
                                    //FileOutputStream fos = new FileOutputStream("/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Client/test2.txt");
                                    FileOutputStream fos = new FileOutputStream(directory);
                                    InputStream is = requestSocket.getInputStream();
                                    //System.out.println("file aquired!");
                                    byte[] bytes = new byte[1024];
                                    int data;
                                    data = is.read(bytes);
                                    //System.out.println(data);
                                    fos.write(bytes,0,data);
                                    fos.flush();
                                    break;
                                }
                                else {
                                    System.out.println("There is no such file on server!");
                                    break;
                                }
                            }
                        }
                        case ("upload"): {
                            if (loginStatus == false) {
                                System.out.println("You need to login first");
                                break;
                            } else {

                                //System.out.println("Input the name of the file you want to upload");
                                String filename = inputline[1];
                                File dir = new File(filename);
                                if (dir.exists()){
                                    System.out.println("file exists!");
                                    sendMessage("upload");
                                    sendMessage(filename);
                                    String status = (String) in.readObject();
                                if (status.equals("ready")) {
                                    String directory = dir.getAbsolutePath();
                                    System.out.println(directory);
                                    OutputStream os = requestSocket.getOutputStream();
                                    //directory = "/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Client/test.txt";
                                    //FileInputStream fis = new FileInputStream("/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Client/test.txt");
                                    FileInputStream fis = new FileInputStream(directory);
                                    byte[] bytes = new byte[1024];
                                    int data;
                                    while ((data = fis.read(bytes)) != -1) {
                                        os.write(bytes, 0, data);
                                        // System.out.println(data);
                                    }
                                    //os.write(-1);
                                    os.flush();
                                    //requestSocket.close();
                                    //requestSocket=null;
                                    //fis.close();
                                    //os.flush();
                                    //requestSocket.shutdownInput();
                                    //requestSocket.shutdownOutput();

                                    //requestSocket=null;
                                    break;
                                }
                            }
                                else {
                                    System.out.println("File not exist!");
                                    break;
                                }
                            }
                        }
                        default:{
                            System.out.println("Unknown command!");
                        }


                    }
                    //Receive the upperCase sentence from the server
                    //MESSAGE = (String)in.readObject();
                    //show the message to the user
                /*
                if (MESSAGE.equals("1")){
                    System.out.println("you need to login");
                }else {
                    String[] temp=message.split(" ");
                    String command=temp[0];
                    switch (command){
                        case ("get"):{
                            System.out.println("into get");
                            break;
                        }
                        case ("upload"):{
                            System.out.println("into upload");

                            String filename=temp[1];
                            File dir=new File(filename);
                            String directory=dir.getAbsolutePath();
                            //System.out.println(directory);
                            FileInputStream fis = new FileInputStream("/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Client/test.txt");
                            System.out.println(directory);
                            byte[] bytes = new byte[1024];
                            int data;
                            while((data = fis.read(bytes))!= -1){
                                out.write(bytes, 0, data);
                            }
                            fis.close();
                            System.out.println("Upload complete!");
                            //client.close();

                            break;
                        }
                    }

                    System.out.println("Receive message: " + MESSAGE);
                }

                 */

            }
        }
        catch (ConnectException e) {
            System.err.println("Connection refused. You need to initiate a server first.");
        }
        catch ( ClassNotFoundException e ) {
            System.err.println("Class not found");
        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        } finally{
            //Close connections
            try{
                in.close();
                out.close();
                requestSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    //send a message to the output stream
    void sendMessage(String msg)
    {
        try{
            //stream write the message
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //main method
    public static void main(String args[])
    {
        Client2 client = new Client2();
        client.run();
    }

}