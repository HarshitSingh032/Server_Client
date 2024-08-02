import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client extends JFrame{

      BufferedReader br;
      PrintWriter out;
    Socket socket;
    //declare components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    public client(){
        try{
            System.out.println("sending request to server");
            socket = new Socket("127.0.0.1",8000);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();
        }catch(Exception e){
          //e.printStackTrace();
        }
    }
    private void handleEvents(){

         messageInput.addKeyListener(new KeyListener() {
             @Override
             public void keyTyped(KeyEvent e) {

             }

             @Override
             public void keyPressed(KeyEvent e) {

             }

             @Override
             public void keyReleased(KeyEvent e) {
                 System.out.println("key released"+e.getKeyCode());
                  if(e.getKeyCode()==10){
                      //System.out.println("you have pressed enter button");
                      String contentToSend = messageInput.getText();
                      messageArea.append("Me :"+contentToSend+"\n");
                      out.println(contentToSend);
                      out.flush();
                      messageInput.setText("");
                      messageInput.requestFocus();
                  }
             }
         });
    }

      private void createGUI(){

        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
          heading.setFont(font);
          messageArea.setFont(font);
          messageInput.setFont(font);
           heading.setIcon(new ImageIcon("logo.png"));
           heading.setHorizontalTextPosition(SwingConstants.CENTER);
          heading.setHorizontalAlignment(SwingConstants.CENTER);
          heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
           messageArea.setEditable(false);
            messageInput.setHorizontalAlignment(SwingConstants.CENTER);
          //frame layout
          this.setLayout(new BorderLayout());
          //addint the compnent
          this.add(heading,BorderLayout.NORTH);
          JScrollPane jScrollPane = new JScrollPane(messageArea);
          this.add(messageArea,BorderLayout.CENTER);
          this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
      }
    //start reading
    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("reader started");
              try{
            while(true){

                     String msg = br.readLine();
                     if(msg.equals("exit")){
                         System.out.println("server terminated the chat");
                         JOptionPane.showConfirmDialog(this,"Server terminated the chat");
                         messageInput.setEnabled(false);
                         socket.close();
                         break;
                     }
                     //System.out.println("Server "+msg);
                messageArea.append("Server : "+ msg+"\n");
                 }
             }catch (Exception e){
                  //e.printStackTrace();
                  System.out.println("connetion closed");
              }
        };
        new Thread(r1).start();
    }
    //start wrirting
    public void startWriting(){
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
                  System.out.println("connection closed");
            }catch(Exception e){
                  e.printStackTrace();
              }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is client...");
         new client();
    }
}
