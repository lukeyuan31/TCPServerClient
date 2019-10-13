# Implementation of FTP client and server

**University of Florida**  
*Keyuan Lu*  
keyuan.lu@ufl.edu

## Description
In this project, you will implement a simple version of FTP client/server software. It
consists of two programs: ftpclient and ftpserver. First, the ftpserver is started on a
computer. It listens on a certain TCP port. Then, the ftpclient is executed on the same or
a different computer; the server’s address and port number are supplied in the command
line, for example, “ftpclient sand.cise.ufl.edu 5106”. The client will prompt for username
and password. After logon, the user can issue three commands at the client side: “dir” is
to retrieve the list of file names available at the server, “get <filename>” is to retrieve a
file from the server, and “upload < filename>” is to upload a file to the server.


The implementation does not have to conform to the FTP standard. Data and command
may use the same TCP connection or different connections. The server should support
multiple concurrent clients.

## How to run the programs

In the directory of FTPServer, use command

```command
javac FTPServer.java
java FTPServer
```
In the directory of FTPClient, use command

```command
javac FTPClient.java
java FTPClient
```

## Demo Test Processes
1) Start the server

2) Start client 1

3) Start client 2

4) Try an invalid command on one of the clients (other than "ftpclient <IP port>", "dir", "get <filename>" and "upload <filename>")

5) Try one of the valid commands "dir", "get <filename>" and "upload <filename>"

6) Command "ftpclient <IP port>" with wrong IP or port number

7) Command "ftpclient <IP port>" with correct IP and port number

8) Try one of the commands "ftpclient <IP port>", "dir", "get <filename>" and "upload <filename>"

9) Try logging in with the wrong username or password

10) Login with correct username and password

11) Try an invalid command on the client (other than "ftpclient <IP port>", "dir", "get <filename>" and "upload <filename>")

12) Try command "ftpclient <IP port>"

13) Try uploading a file that doesn’t exist

14) Command “upload” for a valid file from client 1 to server

15) Command “dir” from client 2

16) Try “get” wrong file name

17) Command “get” on client 2 for the file that client 1 uploaded to the server
