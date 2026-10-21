package charlesk.com.jdbc;

/**
 *
 * @author charles
 */
public class RandomTrial {
    //String [] adviceList = {"Go for hair cut", "Take small bites", "put on jeans"};
    public static void main(String[] args){
    int i; 
    for(i = 0; i < 10; i++){
    int random = (int)(Math.random() * 3);
    System.out.println(random);
}
    }
}