import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Client5 {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, PORT);

        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        try{
        
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // データ受信用バッファの設定
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); // 送信バッファ設定

            // PokerRulesが終わりの合図を送るまでループさせて表示 //
            while (true) {
                String input = in.readLine();
                if(input.equals("finish!!")) break;
                System.out.println(input);
            }

            Hand MyHand = new Hand();

            // サーバー側と同じカードを生成 //
            for(int i=0;i<=4;i++){
                int handinput = Integer.parseInt(in.readLine()); 
                MyHand.hand[i] = handinput;
            }
    
            Arrays.sort(MyHand.hand);
            MyHand.MarkCounter();
            MyHand.NumberCounter();
            MyHand.CheckRole();
            MyHand.ShowHand();//並び替えて役を調べて表示
    
            // 手札の交換 //
            Scanner scanner = new Scanner(System.in);
            System.out.print("何枚のカードを交換しますか？：");
            int n = scanner.nextInt();
            out.println(n);
            if(n!=0){
                int change;
                for(int i=1;i<=n;i++){
                    do{
                        System.out.print("何番目のカードを交換するか入力：");
                        change = scanner.nextInt();
                        if(change<1||change>5){
                            System.out.println("正しく入力してください");
                        }
                    }while(change<1||change>5);
                    out.println(change);
                } 
            }

            // 交換後の手札を更新 //
            for(int i=0;i<=4;i++){
                int handinput = Integer.parseInt(in.readLine());
                MyHand.hand[i]=handinput;
            }
    
            Arrays.sort(MyHand.hand);
            MyHand.MarkCounter();
            MyHand.NumberCounter();
            MyHand.CheckRole();
            MyHand.ShowHand();//並び替えて役を調べて表示

            //out.println(MyHand.rank);
            

            System.out.println(in.readLine());//勝敗を表示

            //out.close();
            //in.close();
            //stdIn.close();
            //socket.close();
        
        }  finally {
            socket.close();
        }
    }
}
