import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Random;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class PokerServer {
    public static final int PORT = 8080; // ポート番号を設定する．
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT); // ソケットを作成する
        try {
            Socket socket = s.accept(); // コネクション設定要求を待つ
            try {
                while (true) {
                    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // データ受信用バッファの設定
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); // 送信バッファ設定
                        PokerRules rules = new PokerRules();
                        String Rules = rules.showRules();
                        out.println(Rules);
                        //out.println();

                        Yamahuda yamuhuda = new Yamahuda();
                        for(int i=1;i<=52;i++){
                            String a = String.valueOf(i);   
                            yamuhuda.cardlist.add(a);
                        }

                        Hand player1= new Hand();
                        //out.println(player1);
                        Hand player2= new Hand();
                        int num;
                        String numkai;
                        int i;
                        
                        for(i=0;i<=4;i++){
                            do{    
                                Random rand = new Random();
                                num = rand.nextInt(52)+1;
                                numkai = String.valueOf(num);
                            }while(!yamuhuda.cardlist.contains(numkai));
                                yamuhuda.cardlist.remove(numkai);
                                player1.hand[i] = num;
                                out.println(player1.hand[i]);
                                System.out.println(player1.hand[i]);
                        }

                        for(i=0;i<=4;i++){
                            do{    
                                Random rand = new Random();
                                num = rand.nextInt(52)+1;
                                numkai = String.valueOf(num);
                            }while(!yamuhuda.cardlist.contains(numkai));
                                yamuhuda.cardlist.remove(numkai);
                                player2.hand[i] = num;
                                out.println(player2.hand[i]);
                                System.out.println(player2.hand[i]);
                        }

                        


                    }
                }
            } finally {
                socket.close();
            }
        } finally {
            s.close();
        }
    }
}


class Yamahuda{
    final int totalcard=52;
    List<String> cardlist = new ArrayList<>();
}


class Card{
    int number;
    int mark;
}

class Hand{
    int hand[]= new int[5];
    int mark[]= new int[4];
    int number[]= new int[15];/*[0]ha tukawanai  */
    String yaku;
    int rank=100;
    int Kicker[]=new int[5];
    int AKQJ;

    /*suuzi wo ma-ku gotoni bunnbetu */
    public void MarkCounter(){
        for(int i=0;i<=4;i++){
            if(hand[i]<=13){
                mark[0]++;}
            else if(13<hand[i]&&hand[i]<=26){
                mark[1]++;}
            else if(26<hand[i]&&hand[i]<=39){
                mark[2]++;}
            else if(39<hand[i]&&hand[i]<=52){
                mark[3]++;}        
        }
    }

    public void NumberCounter(){
        for(int i=0;i<=4;i++){
            int s=hand[i]%13;
            if(s==0){
            number[13]++;    
            }else {
            if(s==1){number[14]++;}    
            number[s]++;    
            }
        }
    }

    public int YakuHandann(){
        AKQJ=number[1]*number[10]*number[11]*number[12]*number[13];
        for(int i=0;i<=3;i++){
            if(mark[i]==5){
                /* hurassyu or sutore-to hurasssyu or roiyaru sutore-to hurassyu*/
                int rennbann;
                if(AKQJ==1){
                    if(rank>1){
                        rank=1;}
                }
                for(int j=1;j<=9;j++){
                rennbann=number[j]*number[j+1]*number[j+2]*number[j+3]*number[j+4];
                if(rennbann==1){
                    if(rank>2){
                    rank=2;}    
                    }else{
                    if(rank>5){
                    rank=5;}
                }   
                }
            }else {
                for(int n=1;n<=13;n++){
                if(number[n]==4){
                    if(rank>3){
                        rank=3;}
                }else if(number[n]==3){
                    for(int m=1;m<=13;m++){
                        if(number[m]==2){
                            if(rank>4){ 
                                rank=4;}    
                        }
                    }
                    if(rank>6){
                        rank=6;}
                }else if(number[n]==2){
                    for(int m=n+1;m<=13;m++){
                        if(number[m]==2){
                            if(rank>8){
                                rank=8;}
                        }  
                   }
                   if(rank>9){
                    rank=9;}

                }else{
                    for(int j=1;j<=9;j++){
                        int sutoreto=number[j]*number[j+1]*number[j+2]*number[j+3]*number[j+4];
                    if(sutoreto==1||AKQJ==1){
                        if(rank>7){
                            rank=7;}    
                    }
                }
                if(rank>10){
                    rank=10;}
                    }
                }
            }    
        }
        return rank;
    }


    public void Winlose(Hand player1,Hand player2){
        if(player1.rank<player2.rank){
            System.out.print("Player 1 is Winner");
        }else if(player1.rank>player2.rank){
            System.out.println("Player 2 is Winner");
        }else{
            for(int a=0;a<=4;a++){
                if(player1.Kicker[a]>player2.Kicker[a]){
                    System.out.print("Player 1 is Winner");
                    break;
                }else if(player1.Kicker[a]<player2.Kicker[a]){
                    System.out.print("Player 2 is Winner");
                    break;
                }
                if(a==5){
                    if(player1.Kicker[a]==player2.Kicker[a]){
                        System.out.println("Draw");
                    }
                }
            }
        }
    }

    public void getKicker(Hand player){
        int check5=0;
        int check9=1;
        switch(player.rank){
            case 2:
                for(int s=13;s>=1;s--){
                    if(number[s]==1){
                        player.Kicker[0]=s;
                        break;
                    }
                }
                break;
            case 3:
                for(int s=14;s>=2;s--){
                    if(number[s]==4){
                        player.Kicker[0]=s;
                    }
                }
                break;
            case 4:
                for(int s=14;s>=2;s--){
                    if(number[s]==3){
                        player.Kicker[0]=s;
                    }
                }   
                break;
            case 5:
                for(int s=14;s>=2;s--){
                    if(number[s]==1){
                        if(player.Kicker[check5]==0){
                            player.Kicker[check5]=s;
                            player.getKicker(player);
                            if(check5==5){break;}
                        }else {
                            check5++;
                        }
                    }
                }
                break;
            case 6:
                for(int s=14;s>=2;s--){
                    if(number[s]==3){
                        player.Kicker[0]=s;
                    }
                }   
                break;
            case 7:
                if(AKQJ==1){
                    player.Kicker[0]=14;
                    break;
                }
                for(int s=13;s>=1;s--){
                    if(number[s]==1){
                        player.Kicker[0]=s;
                        break;
                    }
                }
                break;
            case 8:
                for(int s=14;s>=2;s--){
                    if(number[s]==2){
                        if(player.Kicker[0]==0){
                            player.Kicker[0]=s;}
                        for(int ss=s-1;ss>=2;ss--){
                            if(number[ss]==2){
                                player.Kicker[1]=ss;
                            }
                        }    
                    }else if(number[s]==1){
                        player.Kicker[2]=s;
                    }
                }
                break;
            case 9:
                for(int ss=14;ss>=2;ss--){
                    if(number[ss]==1){
                        if(player.Kicker[check9]==0){
                            player.Kicker[check9]=ss;
                            player.getKicker(player);
                            if(check9==5){break;}
                        }else {
                            check9++;
                        }
                    }
                }
                for(int s=14;s>=2;s--){
                    if(number[s]==2){
                        player.Kicker[0]=s;
                    }
                }
                break;
            case 10:
                for(int s=14;s>=2;s--){
                    if(number[s]==1){
                        if(player.Kicker[check5]==0){
                            player.Kicker[check5]=s;
                            player.getKicker(player);
                            if(check5==5){break;}
                        }else {
                            check5++;
                        }
                    }
                }
                break;
        }
    }

}