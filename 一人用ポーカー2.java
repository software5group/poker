import java.util.Random;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Collections;

public class Poker {
    
    public static void main(String[] args){
      Deck deck = new Deck();
      deck.Reset();
      //System.out.println(yamuhuda.cardlist); 
      Hand player1= new Hand();
      Hand player2= new Hand();

        for(int i=0;i<=4;i++){
          player1.hand[i] = deck.Draw();
        }

        player1.SortCards();
        player1.MarkCounter();
        player1.NumberCounter();
        player1.CheckRole();
        player1.Handname();
        player1.ChangeHand(deck);
        player1.SortCards();
        player1.Handname();

        //System.out.println(yamuhuda.cardlist);   

        for(int i=0;i<=4;i++){
            player2.hand[i] = deck.Draw();
          }

        //System.out.println(yamuhuda.cardlist);
        player2.SortCards();
        player2.MarkCounter();
        player2.NumberCounter();
        player2.CheckRole();
        player2.Handname();
        player2.ChangeHand(deck);
        player2.SortCards();
        player2.Handname();
        System.out.println();

        player1.getKicker(player1);
        player2.getKicker(player2);
        System.out.println("あなたの手札は");
        player1.Handname();
        System.out.println("相手の手札は");
        player2.Handname();
        player1.Winlose(player1, player2);
        }
}



class Hand{
    Card hand[]=new Card[5];
    int mark[]=new int[4];
    int number[]= new int[15];// [0]は使わない
    String yaku;
    int rank=100;
    int Kicker[]=new int[5];
    int AKQJ;

    // 並べ替え //
    public void SortCards(){
        for(int i=3;i>=0;i--){
            for(int j=0;j<=i;j++){
                if(hand[j].Toint()>hand[j+1].Toint()){
                    Card a = new Card();
                    a = hand[j];
                    hand[j] = hand[j+1];
                    hand[j+1] = a;
                }
            }
        }
    }

    // 手札を交換 //
    public void ChangeHand(Deck deck){
        Scanner scanner = new Scanner(System.in);
        System.out.println("交換する枚数を入力：");
        int changenumber=scanner.nextInt();
        if(changenumber==0){
            //scanner.close();
            return;
        }else{
           for(int i=1;i<=changenumber;i++){
            System.out.println("何番目のカードを交換するか入力：");
            int num = scanner.nextInt();
            hand[num-1] = deck.Draw();
            } 
        }
        //scanner.close();
    }

    // 手札と役を表示 //
    public void Handname(){
        System.out.println("あなたの手札は");
        for(int i=0;i<=4;i++){
            if(hand[i].mark==0){System.out.println("スペードの"+ hand[i].number);}        
            else if(hand[i].mark==1){System.out.println("ハートの"+ hand[i].number);} 
            else if(hand[i].mark==2){System.out.println("ダイヤの"+ hand[i].number);} 
            else if(hand[i].mark==3){System.out.println("クラブの"+ hand[i].number);} 
        }
        System.out.println("役は");
        switch(rank){
            case 1:
             System.out.println("ロイヤルストレートフラッシュです。");
             break;
            case 2:
             System.out.println("ストレートフラッシュです。");
             break;
            case 3:
             System.out.println("フォーカードです。");
             break; 
            case 4:
             System.out.println("フルハウスです。");
             break;
            case 5:
             System.out.println("フラッシュです。");
             break;
            case 6:
             System.out.println("スリーカードです。");
             break; 
            case 7:
             System.out.println("ストレートです。");
             break;
            case 8:
             System.out.println("ツーペアです。");
             break;
            case 9:
             System.out.println("ワンペアです。");
             break;
            case 10:
             System.out.println("ハイカードです。");
             break;
        }
    }

    // 役柄ごとに枚数をカウント //
    public void MarkCounter(){
        for(int i=0;i<=4;i++){
            mark[hand[i].mark]++;       
        }
    }

    // 数字ごとに枚数をカウント
    public void NumberCounter(){
        for(int i=0;i<=4;i++){
            if(hand[i].number==0){number[14]++;}    
            number[hand[i].number+1]++;
        }
    }

    // 役を判断 //
    public int CheckRole(){
        AKQJ=number[1]*number[10]*number[11]*number[12]*number[13];
        for(int i=0;i<=3;i++){
            if(mark[i]==5){
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

    // 勝敗を表示 //
    public void Winlose(Hand player1,Hand player2){
        if(player1.rank<player2.rank){
            System.out.print("Player 1 is Winner");
        }else if(player1.rank>player2.rank){
            System.out.println("Player 2 is Winner");
        }else{
            for(int a=0;a<=4;a++){
                if(player1.Kicker[a]>player2.Kicker[a]){
                    System.out.println("Player 1 is Winner");
                    break;
                }else if(player1.Kicker[a]<player2.Kicker[a]){
                    System.out.println("Player 2 is Winner");
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

    // キッカーを取得 //
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



class Card{
    int number;
    int mark;

    // 1~52のintに変換 //
    public int Toint(){ 
        return number + mark * 13 + 1;
    }
}



class Deck{
    final int totalcard=52; // カードの総数
    private List<String> cardlist;
    private int cardindex = 0; // 引いた数

    // 山札の初期化 //
    public void Reset(){
        cardlist = new ArrayList<>();
        for(int i=0;i<52;i++){
            String a = String.valueOf(i);  // cardlistに0~51を追加
            cardlist.add(a);
        }

        Collections.shuffle(cardlist); // カードをシャッフル
    }

    // カードを1枚引く //
    public Card Draw(){
        Card drawcard = new Card();
        int cardnum = Integer.parseInt(cardlist.get(cardindex));

        cardindex++; //引く場所を1つずらす

        if(cardindex>=totalcard){
            cardindex = 0; //引く場所を先頭に戻す
            Collections.shuffle(cardlist); // カードをシャッフル
        }

        drawcard.number = cardnum % 13 + 1;
        drawcard.mark = cardnum / 13;
        return drawcard;
    }
}
