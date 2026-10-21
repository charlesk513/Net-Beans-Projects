package charlesk.com.jdbc;

import java.io.*;
import java.net.*;

/**
 *
 * @author nabaasa evarist and Charles k
 */
public class DailyAdviceClient {
        
    public void go(){
        try{
            Socket s = new Socket("127.0.0.1", 4242);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            String advice = reader.readLine();
            System.out.println("Today you should: " + advice);
            reader.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }// close go
    
    public static void main(String []args){
        DailyAdviceClient client = new DailyAdviceClient();
        client.go();
    }
}
