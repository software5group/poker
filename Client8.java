import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Client8{
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

            while(true){
                Hand MyHand = new Hand();

                // サーバー側と同じカードを生成 //
                for(int i=0;i<=4;i++){
                    int handinput = Integer.parseInt(in.readLine()); 
                    MyHand.hand[i] = new Card();
                    MyHand.hand[i].number = (handinput - 1) % 13 + 1;
                    MyHand.hand[i].mark = (handinput - 1) / 13;
                }
        
                MyHand.SortCards();
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

                // サーバー側と同じカードを生成 //
                for(int i=0;i<=4;i++){
                    int handinput = Integer.parseInt(in.readLine()); 
                    MyHand.hand[i].number = (handinput - 1) % 13 + 1;
                    MyHand.hand[i].mark = (handinput - 1) / 13;
                }
        
                MyHand.SortCards();
                MyHand.MarkCounter();
                MyHand.NumberCounter();
                MyHand.CheckRole();
                MyHand.ShowHand();//並び替えて役を調べて表示

                //out.println(MyHand.rank);

                // 賭け金の統一 //
                int otherbetchip,mybetchip;
                while(true){
                    if(in.readLine().equals("finish!!")) break; //終了条件
                    int betcounter = Integer.parseInt(in.readLine());
                    otherbetchip = Integer.parseInt(in.readLine());
                    if(betcounter==0){
                        System.out.println("あなたは親です。参加費として50チップを支払いました。");
                        mybetchip = 50;
                        out.println(mybetchip);
                    }else{
                            System.out.println("相手の賭け金は" + otherbetchip + "です。");
                            System.out.println("フォールドなら-1、コールなら0、レイズなら上乗せする金額を入力してください:");
                            mybetchip = scanner.nextInt();
                            out.println(mybetchip);
                    }
                    if(mybetchip > 0 || betcounter==0){  //レイズなら終了しない
                        out.println(0);
                    }else{
                        out.println(1);
                    }
                }
                

                System.out.println(in.readLine());//勝敗を表示
                System.out.println("あなたのチップは" + in.readLine() + "になりました。");

                //out.close();
                //in.close();
                //stdIn.close();
                //socket.close();
                int key;
                while(true){
                    System.out.println("終了するなら1を、続けるなら0を入力してください:");
                    key = scanner.nextInt();
                    if(key == 1){
                        out.println(key);
                        break;
                    }
                    else if(key == 0){
                        out.println(key);
                        break;
                    } else{
                        System.out.println("1か0を入力してください");
                    }
                }
                System.out.println(in.readLine());
                if(key == 1) break;

            }
        
        }  finally {
            socket.close();
        }
    }
}
