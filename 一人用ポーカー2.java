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
        player1.Bet();

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
        player2.Bet();
        System.out.println();

        player1.getKicker(player1);
        player2.getKicker(player2);
        System.out.println("あなたの手札は");
        player1.Handname();
        System.out.println("相手の手札は");
        player2.Handname();
        player1.Winlose(player1, player2);
        System.out.println("あなたのチップは" + player1.chip + "になりました。");
    }
}



class Hand{
    Card hand[]=new Card[5];
    int mark[]=new int[4];
    int number[]= new int[15]; // [0]は使わない
    String role;
    int rank=100;
    int Kicker[]=new int[5];
    int chip = 500; // 持ち金
    int betchip; //賭け金

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
        System.out.print("交換する枚数を入力：");
        int changenumber=scanner.nextInt();
        if(changenumber==0){
            //scanner.close();
            return;
        }else{
           for(int i=1;i<=changenumber;i++){
            System.out.print("何番目のカードを交換するか入力：");
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
        System.out.println("役は" + role + "です。");
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
            if(hand[i].number==1){number[14]++;} // 14もエースとしてカウント
            number[hand[i].number]++;
        }
    }

    // 役を判断 //
    public int CheckRole(){
        int AKQJ=number[1]*number[10]*number[11]*number[12]*number[13];
        for(int i=0;i<=3;i++){
            if(mark[i]==5){
                if(AKQJ==1){
                    if(rank>1){ rank=1; role="ロイヤルストレートフラッシュ";}
                }
                for(int j=1;j<=9;j++){
                    int straight=number[j]*number[j+1]*number[j+2]*number[j+3]*number[j+4];
                    if(straight==1){
                        if(rank>2){rank=2; role="ストレートフラッシュ";}    
                    }else{
                        if(rank>5){rank=5; role="フラッシュ";}
                    }   
                }
            }else {
                for(int n=1;n<=13;n++){
                    if(number[n]==4){
                        if(rank>3){rank=3; role="フォーカード";}
                    }else if(number[n]==3){
                        for(int m=1;m<=13;m++){
                            if(number[m]==2){
                                if(rank>4){rank=4; role="フルハウス";}    
                            }
                        }
                        if(rank>6){rank=6; role="スリーカード";}
                    }else if(number[n]==2){
                        for(int m=n+1;m<=13;m++){
                            if(number[m]==2){
                                if(rank>8){rank=8; role="ツーペア";}
                            }  
                        }
                        if(rank>9){rank=9; role="ワンペア";}
                    }else{
                        for(int j=1;j<=10;j++){
                            int straight=number[j]*number[j+1]*number[j+2]*number[j+3]*number[j+4];
                            if(straight==1){
                                if(rank>7){rank=7; role="ストレート";}
                            }
                        }
                        if(rank>10){rank=10; role="ハイカード";}
                    }
                }
            }    
        }
        return rank;
    }

    // 賭け金を決定 //
    public void Bet(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("現在のあなたのチップは"+ chip + "です。");
        System.out.print("賭け金を入力：");
        betchip = scanner.nextInt();
        chip = chip - betchip;
        //scanner.close();
    }

    // 勝敗を表示 //
    public void Winlose(Hand player1,Hand player2){
        if(player1.rank<player2.rank){
            System.out.println("あなたの勝利です");
            player1.chip = player1.chip + player1.betchip + player2.betchip;
        }else if(player1.rank>player2.rank){
            System.out.println("相手の勝利です");
            player2.chip = player2.chip + player1.betchip + player2.betchip;
        }else{
            for(int a=0;a<=4;a++){
                if(player1.Kicker[a]>player2.Kicker[a]){
                    System.out.println("あなたの勝利です");
                    player1.chip = player1.chip + player1.betchip + player2.betchip;
                    break;
                }else if(player1.Kicker[a]<player2.Kicker[a]){
                    System.out.println("相手の勝利です");
                    player2.chip = player2.chip + player1.betchip + player2.betchip;
                    break;
                }
                if(a==5){
                    if(player1.Kicker[a]==player2.Kicker[a]){
                        System.out.println("引き分けです");
                        player1.chip = player1.chip + player1.betchip;
                        player2.chip = player2.chip + player2.betchip;
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
                for(int s=14;s>=1;s--){
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
    private int cardindex = 0; // カードを引く箇所

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
