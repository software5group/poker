import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Random;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Server8{
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        List<Socket> clientSockets = new ArrayList<>();
        Deck deck = new Deck();
        deck.Reset();
        Hand player1= new Hand();
        Hand player2= new Hand();
        int threadcounter = 0;

        while (true) {
            Socket clientSocket = serverSocket.accept();
            clientSockets.add(clientSocket);
            if (clientSockets.size() >= 2) {
                for (Socket socket : clientSockets) {
                    new ServerThread(socket,deck,player1,player2,threadcounter).start();
                    threadcounter++;
                }
                clientSockets.clear();
            }
        }
    }
}

class ServerThread extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Lock lock = new ReentrantLock();
    private static Deck deck =new Deck();
    private Hand player1;
    private Hand player2;
    private int threadcounter;
    private static int convert; // スレッドを交互に実行するスイッチ
    private static int betcounter; // 親は最初ベットしか出来ないのでその場合分けに使う
    private static int allow; // 賭け金統一の際の終了条件
    private static int gamecounter=0; // ゲームの実行回数

    public ServerThread(Socket socket,Deck deck,Hand player1,Hand player2,int threadcounter) {
        this.clientSocket = socket;
        this.deck=deck;
        this.player1=player1;
        this.player2=player2;
        this.threadcounter=threadcounter;
    }

    public synchronized void run() {
        
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //ルールを表示
            PokerRules rules = new PokerRules();
            String Rules = rules.showRules();
            out.println(Rules);
            //out.println();

            while (true) {
                if(threadcounter != 0 && threadcounter != 1){
                    player1.copycounter=0;
                    player2.copycounter=0;
                }

                //送受信用のハンド
                Hand player =new Hand();
                Deck deck1 = new Deck();

                player1.fold=false; player2.fold=false;
                player1.betchip=0; player2.betchip=0;
                ServerThread.convert=0 + ServerThread.gamecounter%2;//ゲーム毎に親を交代;
                ServerThread.allow=0;
                ServerThread.betcounter=0;
                
                //スレッドの同期
                synchronized (this) {
                    if (Thread.currentThread().getName().equals("Thread-1")) {
                        Thread.sleep(1000L); 
                    }
                    //手札の作成
                    //deck1.CopyDeck(deck);
                    lock.lock();
                    try{
                    deck1.CopyDeck(deck);    
                    player.MakeHand(deck1);
                    deck.CopyDeck(deck1);
                    }finally{
                        lock.unlock();
                    }
                    //deck.CopyDeck(deck1);
                    player.SortCards();
                    for(int i=0;i<=4;i++){
                        out.println(player.hand[i].Toint());//手札を送る
                    }


                    int n = Integer.parseInt(in.readLine());//交換する手札の枚数
                    System.out.println(n);
                    lock.lock();
                    try{
                    deck1.CopyDeck(deck);
                    if(n != 0){//交換する枚数が0枚だったら終わり
                        int change;//何枚目のカードを交換するかの変数
                        for(int i=1; i<=n; i++){
                            change = Integer.parseInt(in.readLine());//クライアントが送ってきたものを読み込んでchangeに入れる
                            player.hand[change-1] = deck1.Draw();//新しく引いたカードに置き換える
                        }
                    }
                    deck.CopyDeck(deck1);
                    }finally{
                        lock.unlock();
                    }
                    
                    player.SortCards();
                    System.out.println("最後の手札");
                    /*player.MarkCounter();
                    player.NumberCounter();
                    player.CheckRole();*/
                    player.ShowHand();

                    for(int i=0;i<=4;i++){
                        out.println(player.hand[i].Toint());//手札を送りなおす
                    }

                    while(player1.copycounter==0||player2.copycounter==0){
                        Thread.sleep(1000L);    //1秒待機        
                        if (threadcounter % 2 == 0) {
                            if(player1.copycounter==0){
                                player1.SetHand(player);
                                System.out.println("player1の手札");
                                player1.MarkCounter();
                                player1.NumberCounter();
                                player1.CheckRole();
                                player1.ShowHand();
                                System.out.println(player1.copycounter+"と"+player2.copycounter);
                                System.out.println("Player1 Rank: " + player1.rank);//ランクを出してスレッド0だったらplayer1に入れる
                            }
                        }else{
                            if(player2.copycounter==0){
                                player2.SetHand(player);
                                System.out.println("player2の手札");
                                player2.MarkCounter();
                                player2.NumberCounter();
                                player2.CheckRole();
                                player2.ShowHand();
                                System.out.println(player1.copycounter+"と"+player2.copycounter);
                                System.out.println("Player2 Rank: " + player2.rank);//スレッド0以外だとplayer2に
                            }
                        }
                    }

                    // 賭け金の統一 //
                    while(true){
                        if(threadcounter % 2 == 0) {
                            while(ServerThread.convert==1){
                                Thread.sleep(1000L);    //相手がスイッチを切替えるまで待機
                            }
                            if(ServerThread.allow==1){  //終了条件
                                out.println("finish!!");
                                ServerThread.convert = 1;
                                player1.chip -= player1.betchip;//スイッチを切り替えて、持ち金を減らして終了
                                break;
                            }else{
                                out.println("");
                            }
                            out.println(ServerThread.betcounter);
                            out.println(player2.betchip);
                            n = Integer.parseInt(in.readLine());//上乗せ額を受け取る
                            if(n==-1){
                                player1.fold = true;//-1ならフォールドのフラグを立てる
                            }else{
                                player1.betchip = player2.betchip + n;
                            }
                            ServerThread.betcounter++;
                            ServerThread.allow = Integer.parseInt(in.readLine());
                            System.out.println("allow=" + ServerThread.allow);
                            ServerThread.convert = 1;//スイッチの切り替え
                        }else{
                            while(ServerThread.convert==0){
                                Thread.sleep(3000L);    //相手がスイッチを切替えるまで待機
                            }
                            if(ServerThread.allow==1){  //終了条件
                                out.println("finish!!");
                                ServerThread.convert = 0;
                                player2.chip -= player2.betchip;//スイッチを切り替えて、持ち金を減らして終了
                                break;
                            }else{
                                out.println("");
                            }
                            out.println(ServerThread.betcounter);
                            out.println(player1.betchip);
                            n = Integer.parseInt(in.readLine());//上乗せ額を受け取る
                            if(n==-1){
                                player2.fold = true;//-1ならフォールドのフラグを立てる
                            }else{
                                player2.betchip = player1.betchip + n;
                            }
                            ServerThread.betcounter++;
                            ServerThread.allow = Integer.parseInt(in.readLine());
                            System.out.println("allow=" + ServerThread.allow);
                            ServerThread.convert = 0;//スイッチの切り替え
                        }
                    }
                    

                    player1.getKicker(player1);
                    player2.getKicker(player2);
                    //System.out.println(player1.Winlose(player1, player2));
                    
                    //勝者をクライアントに送る
                    if (threadcounter % 2 == 0) {
                        out.println(player1.Winlose(player1, player2));
                        out.println(player1.chip);
                        threadcounter += 2;
                    } else{
                        out.println(player1.Winlose(player2, player1));
                        out.println(player2.chip);
                        threadcounter += 2;
                    }

                    System.out.println();

                    while(player1.copycounter==1||player2.copycounter==1){
                        Thread.sleep(1000L);    //1秒待機        
                        if (threadcounter % 2 == 0) {
                            if(player1.copycounter==1){
                                player1.key = Integer.parseInt(in.readLine());
                                System.out.println(player1.key);
                                player1.count();
                                ServerThread.gamecounter++;//ゲームの実行回数を記録
                            }
                        }else{
                            if(player2.copycounter==1){
                                player2.key = Integer.parseInt(in.readLine());
                                System.out.println(player2.key);
                                player2.count();
                            }
                        }
                    }

                    if(player1.key == 1 || player2.key == 1){
                        out.println("1が選ばれたので終了します");
                        in.close();
                        out.close();
                        clientSocket.close();
                    }else{
                        out.println("次のゲームを開始します");
                    }

                }

            }

            //in.close();
            //out.close();
            //clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
