import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Client2 {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, PORT);

        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        try{
        
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // データ受信用バッファの設定
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); // 送信バッファ設定

            while (true) {
                String input = in.readLine();
                if(input.equals("finish!!")) break;
                System.out.println(input);
            }
            int i;
            Hand player1 = new Hand();
            //System.out.println(player1);
            for(i=0;i<=4;i++){
                String handinp1 = in.readLine();
                player1.hand[i] = Integer.parseInt(handinp1);
                //System.out.println(player1.hand[i]);
            }
    
            /*Hand player2 = new Hand();
            System.out.println(player2);
            for(i=0;i<=4;i++){
                String handinp2 = in.readLine();
                player2.hand[i] = Integer.parseInt(handinp2);
                //System.out.println(player2.hand[i]);
            }*/
    
            Arrays.sort(player1.hand);
            //Arrays.sort(player2.hand);
            
            System.out.println("プレイヤー1の手札は");
            for(i = 0; i <= 4; i++){
                System.out.println(player1.hand[i]);
            }
            /*System.out.println("プレイヤー2の手札は");
            for(i = 0; i <= 4; i++){
                System.out.println(player2.hand[i]);
            }*/
    
            int n;
            int[] change;
            LABEL: while(true){
                Scanner scanner = new Scanner(System.in);
                System.out.println("何枚のカードを交換しますか？");
                n = scanner.nextInt();
                if(n == 0){
                    change = new int[n];
                    break;
                }
                System.out.println("1～5番のうち交換する手札の番号を教えてください。");
                change = new int[n];
                for(i = 0; i < change.length; i++){
                    change[i] = scanner.nextInt();
                    if (change[i] >= 1 && change[i] <= 5){
                        //System.out.print(change[i] + " ");
                        if(i == change.length - 1){
                            //System.out.println();
                            break LABEL;
                        }
                    }else{
                        System.out.println("正しく入力してください。");
                        break;
                    }
                }
            }
    
            /*for(i = 0; i < n; i++){
                int j = change[i] - 1;
                player1.hand[j] = 0;
            }*/
    
            /*for(i = 0; i <= 4; i++){
                System.out.println(player1.hand[i]);
            }*/
    
            out.println(n);
            
            for(i = 0; i < change.length; i++){
                out.println(change[i]);
            }

            for(i = 0;i< n;i++){
                String handinp1 = in.readLine();
                player1.hand[change[i] - 1] = Integer.parseInt(handinp1);
                //System.out.println(player1.hand[i]);
            }

            Arrays.sort(player1.hand);
            for(i = 0; i <= 4; i++){
                System.out.println(player1.hand[i]);
            }

            player1.MarkCounter();
            player1.NumberCounter();
            player1.rank = player1.YakuHandann();
            out.println(player1.rank);
            

            String input2 = in.readLine();
            System.out.println(input2);

            //out.close();
            //in.close();
            //stdIn.close();
            //socket.close();
        
        }  finally {
            socket.close();
        }
    }
}