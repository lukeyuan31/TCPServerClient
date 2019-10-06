package Server;

import java.awt.*;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.List;

class User{
    public String username;
    public String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
public class Server2 {

    private static final int sPort = 8000;   //The server will be listening on this port number
    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(sPort);
        int clientNum = 1;
        try {
            while(true) {
                new Handler(listener.accept(),clientNum).start();
                //System.out.println("Client "  + clientNum + " is connected!");
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
        public static List<User> UserList = new ArrayList<User>();

        public List<User> addUser(List<User> userList, String username, String password){
            User newUser=new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            userList.add(newUser);
            return userList;
        }

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
            //String UserName;
            boolean loginStatus=false;
            String command;
            addUser(UserList,"user1","test1");
            addUser(UserList,"user2","test2");
            addUser(UserList,"user3","test3");
            try{
                //initialize Input and Output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                out.flush();
                in = new ObjectInputStream(connection.getInputStream());
                /*
                FileOutputStream fos = new FileOutputStream("/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Server/test.txt");
               // System.out.println(directory);
                System.out.println("file aquired!");
                byte[] bytes = new byte[1024];
                int data;
                while ((data = in.read(bytes)) != -1) {
                    fos.write(bytes, 0, data);
                }
                fos.close();

                 */
                try{
                    while(true)
                    {
                        //receive the message sent from the client
                        message = (String)in.readObject();
                        String[] temp;
                        temp=message.split(" ");
                        command=temp[0];
                        //System.out.println(command);
                        switch (message) {
                            case ("login"):
                                //System.out.println("recognize 1");
                                //String message = "input username and password";
                                String username=(String)in.readObject();
                                String password=(String)in.readObject();
                                for(User u:UserList){
                                    //System.out.println(u.username+u.password);
                                    //System.out.println(username+password);
                                    if (u.getUsername().equals(username)&& u.getPassword().equals(password)){
                                        sendMessage("success");
                                        loginStatus=true;
                                        break;
                                    }
                                }
                                if (loginStatus==false){
                                    sendMessage("fail");
                                }
                                //sendMessage("fail");
                                //loginStatus=true;
                                break;
                            case ("dir"): {
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
                            case ("upload"):{
                                String filename = (String)in.readObject();
                                //System.out.println(filename);
                                File dir=new File(filename);
                                sendMessage("ready");
                                String directory=dir.getAbsolutePath();
                                //System.out.println(directory);
                                FileOutputStream fos = new FileOutputStream("/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Server/test.txt");
                                System.out.println(directory);
                                InputStream is=connection.getInputStream();
                                //System.out.println("file aquired!");
                                byte[] bytes = new byte[1024];
                                int data;
                                while ((data = is.read(bytes)) != -1) {
                                    //System.out.println(data);
                                    fos.write(bytes,0,data);
                                }
                                //fos.close();
                                System.out.println("Upload complete!");
                                break;
                            }
                            case ("get"):{
                                String filename = (String)in.readObject();
                                FileInputStream fis=new FileInputStream("/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Server/test1.txt");
                                byte[] bytes = new byte[1024];
                                int data;
                                OutputStream os = connection.getOutputStream();
                                while((data = fis.read(bytes))!= -1){
                                    os.write(bytes, 0, data);
                                    // System.out.println(data);
                                }
                                connection.close();
                                fis.close();
                                break;
                            }
                            /*
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
                                    //System.out.println("upload test");
                                    sendMessage("upload test");
                                    //InputStream is = accept.getInputStream();
                                    String filename="test.txt";
                                    File dir=new File(filename);
                                    String directory=dir.getAbsolutePath();
                                    FileOutputStream fos = new FileOutputStream("/Users/lukeyuan/IdeaProjects/TCPServerClient/src/Server/test.txt");
                                    System.out.println(directory);
                                    System.out.println("file aquired!");
                                    byte[] bytes = new byte[1024];
                                    int data;
                                    while ((data = in.read(bytes)) != -1) {
                                        fos.write(bytes, 0, data);
                                    }
                                    fos.close();
                                    System.out.println("Upload complete!");
                                    sendMessage("upload test");
                                    break;
                                }
                            }
                            */

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
                //System.out.println("Disconnect with Client " + no);
            }
            finally{
                //Close connections
                try{
                    in.close();
                    out.close();
                    connection.close();
                }
                catch(IOException ioException){
                    //System.out.println("Disconnect with Client " + no);
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
