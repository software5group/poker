import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Random;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Random;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class Server5{
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        List<Socket> clientSockets = new ArrayList<>();
        Deck deck = new Deck();
        deck.Reset();
        Hand player1= new Hand();
        Hand player2= new Hand();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            clientSockets.add(clientSocket);
            if (clientSockets.size() >= 2) {
                for (Socket socket : clientSockets) {
                    new ServerThread(socket,deck,player1,player2).start();
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
    private static final Object lock = new Object();
    private Deck deck;
    private Hand player;
    private Hand player1;
    private Hand player2;

    public ServerThread(Socket socket,Deck deck,Hand player1,Hand player2) {
        this.clientSocket = socket;
        this.deck=deck;
        this.player1=player1;
        this.player2=player2;
    }

    public synchronized void run() {
         //山札として52枚のカードを作成
        
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //ルールを表示
            PokerRules rules = new PokerRules();
            String Rules = rules.showRules();
            out.println(Rules);
            //out.println();

            while (true) {
                //送受信用のハンド
                Hand player =new Hand();
                Deck deck1 = new Deck();
                
                //スレッドの同期
                synchronized (this) {
                    //手札の作成
                    deck1.CopyDeck(deck);
                    player.MakeHand(deck);
                    deck.CopyDeck(deck1);

                    Arrays.sort(player.hand);
                    for(int i=0;i<=4;i++){
                        out.println(player.hand[i]);//手札を送る
                    }


                    int n = Integer.parseInt(in.readLine());//交換する手札の枚数
                    System.out.println(n);

                    deck1.CopyDeck(deck);
                    if(n != 0){//交換する枚数が0枚だったら終わり
                        int change;//何枚目のカードを交換するかの変数
                        for(int i=1; i<=n; i++){
                            change = Integer.parseInt(in.readLine());//クライアントが送ってきたものを読み込んでchangeに入れる
                            player.hand[change-1] = deck.Draw();//新しく引いたカードに置き換える
                        }
                        //notifyAll();
                    }
                    deck.CopyDeck(deck1);
                    
                    Arrays.sort(player.hand);
                    System.out.println("最後の手札");
                    player.MarkCounter();
                    player.NumberCounter();
                    player.CheckRole();
                    player.ShowHand();

                    for(int i=0;i<=4;i++){
                        out.println(player.hand[i]);//手札を送りなおす
                    }

                    while(player1.copycounter==0||player2.copycounter==0){
                        Thread.sleep(1000L);    //1秒待機        
                        if (Thread.currentThread().getName().equals("Thread-0")) {
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

                    
                    player1.getKicker(player1);
                    player2.getKicker(player2);
                    System.out.println(player1.Winlose(player1, player2));
                    
                    //勝者をクライアントに送る
                    if (Thread.currentThread().getName().equals("Thread-0")) {
                        out.println(player1.Winlose(player1, player2));}
                    else{
                        out.println(player1.Winlose(player2, player1));
                    }
                    System.out.println();

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
