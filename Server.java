import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

import java.net.*;
public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br; //for reading
    PrintWriter out;   //for writing
    public Server() throws IOException {
        server = new ServerSocket(8000);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting...");
         socket=server.accept();

         br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out =  new PrintWriter(socket.getOutputStream());

         startReading();
         stratWriting();
    }
    public void startReading() throws IOException{
        // thread-read krke deta rahega
        Runnable r1 = ()->{
            System.out.println("reader started");
                try{
                while(true){

                    String msg=br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: "+msg);
                }
                }catch (Exception e){
                    //e.printStackTrace();
                    System.out.println("connection closed");
                }
        };
        new Thread(r1).start();
    }
    public void stratWriting(){
      //data user se lega and send krega client tak
       Runnable r2 = ()->{
           System.out.println("writer started");
             try{
           while(true && !socket.isClosed()){

                  BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                 String content = br1.readLine();
               out.println(content);
               out.flush();
                 if(content.equals("exit")){
                    socket.close();
                     break;
                 }
              }
                 System.out.println();
          }catch (Exception e){
                 //e.printStackTrace();
                 System.out.println("connection closed");
             }
          // System.out.println("connection is closed");
       };
       new Thread(r2).start();
    }
    public static void main(String[] args) throws IOException {
        System.out.println("this is server...going to start");
           new Server();
    }
}
