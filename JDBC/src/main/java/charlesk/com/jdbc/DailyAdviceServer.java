package charlesk.com.jdbc;

import java.io.*;
import java.net.*;
/**
 *
 * @author nabaasa evarist
 */
public class DailyAdviceServer {
    String [] adviceList = {"Go for hair cut", "Take small bites", "put on jeans"};
    
    public void go(){
        try{
            ServerSocket serverSock = new ServerSocket(4242);
            
            while(true){
                Socket sock = serverSock.accept();
                PrintWriter writer = new PrintWriter(sock.getOutputStream());
                String advice = getAdvice();
                writer.println(advice);
                writer.close();
                
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }// close go
    
    private String getAdvice(){
        int random = (int) (Math.random() * adviceList.length);
        return adviceList[random];
    }
    
    public static void main(String []args){
        DailyAdviceServer server = new DailyAdviceServer();
        server.go();
    }
}