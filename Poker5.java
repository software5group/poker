import java.util.Random;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

public class Poker{
    public static void main(String[] args){
    Deck deck=new Deck();
    deck.Reset();
    Hand player1= new Hand();
    Hand player2= new Hand();
    player1.MakeHand(deck);
    player2.MakeHand(deck);
    Arrays.sort(player1.hand);
    player1.MarkCounter();
    player1.NumberCounter();
    player1.CheckRole();
    player2.MarkCounter();
    player2.NumberCounter();
    player2.CheckRole();
    System.out.println("あなたのターンです。");
    System.out.println("あなたの手札は");
    player1.ShowHand();
    player1.ChangeHand(deck);
    Arrays.sort(player1.hand);
    player1.MarkCounter();
    player1.NumberCounter();
    player1.CheckRole();
    player1.ShowHand();
    player1.Bet();

    System.out.println("相手のターンです。");
    System.out.println("相手の手札は");
    player2.ShowHand();
    player2.ChangeHand(deck);
    Arrays.sort(player2.hand);
    player2.MarkCounter();
    player2.NumberCounter();
    player2.CheckRole();
    player2.ShowHand();
    player2.Bet();

    player1.getKicker(player1);
    player2.getKicker(player2);
    System.out.println("結果");
    System.out.println("あなたの手札は");
    player1.ShowHand();
    System.out.println();
    System.out.println("相手の手札は");
    player2.ShowHand();
    String m=player1.Winlose(player1, player2);
    System.out.println();
    System.out.println("あなたのチップは" + player1.chip + "になりました。");
    System.out.println("相手のチップは" + player2.chip + "になりました。");
    }
}

class Hand{
    int hand[]= new int[5];
    int mark[]= new int[4];
    int number[]= new int[15];
    int rank=100;
    int Kicker[]=new int[5];
    int AKQJ;
    int chip =500; //持ち金
    int betchip;
    int copycounter;

    //手札生成// 
    public void MakeHand(Deck deck){
        for(int i=0;i<=4;i++){
            hand[i]=deck.Draw();
        }
    }
   //手札交換//
    public void ChangeHand(Deck deck){
        Scanner scanner = new Scanner(System.in);
        System.out.println("交換する枚数を教えて");
        int changenumber=scanner.nextInt();
        if(changenumber==0){
            return;
        }else{
           for(int i=1;i<=changenumber;i++){
            System.out.println("交換したい手札の番号教えて");
            int n = scanner.nextInt();
            hand[n-1]= deck.Draw();
        } 
        }
    }
    
    //手札と役の表示//
    public void ShowHand(){
        //System.out.println("あなたの手札は");
        for(int i=0;i<=4;i++){
            System.out.print((i+1)+"番は");
            if(hand[i]<=13){
                if(hand[i]==13){System.out.println("スペードの"+ hand[i]+"です。");}
                else{
                System.out.println("スペードの"+ (hand[i]%13)+"です。");}}
            else if(13<hand[i]&&hand[i]<=26){
                if(hand[i]==26){System.out.println("ハートの"+ (hand[i]-13)+"です。");}
                else{System.out.println("ハートの"+ (hand[i]%13)+"です。");}}
            else if(26<hand[i]&&hand[i]<=39){
                if(hand[i]==39){System.out.println("ダイヤの"+ (hand[i]-26)+"です。");}
                else{System.out.println("ダイヤの"+ (hand[i]%13)+"です。");}}
            else if(39<hand[i]&&hand[i]<=52){
                if(hand[i]==52){System.out.println("スペードの"+ (hand[i]-39)+"です。");}
                else{System.out.println("クラブの"+ (hand[i]%13)+"です。");}}        
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
        for(int i=0;i<=3;i++){
            mark[i]=0;
        }    
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
    
    // 数字ごとに枚数をカウント
    public void NumberCounter(){
        for(int i=0;i<=14;i++){
            number[i]=0;
        }
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

    // 役を判断 //
    public void CheckRole(){
        rank=100;
        AKQJ=number[1]*number[10]*number[11]*number[12]*number[13];
        for(int i=0;i<=3;i++){
            if(mark[i]==5){
                int Straight;
                if(AKQJ==1){
                    if(rank>1){
                        rank=1;}
                }
                for(int j=1;j<=9;j++){
                Straight=number[j]*number[j+1]*number[j+2]*number[j+3]*number[j+4];
                if(Straight==1){
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
                        int Straight2=number[j]*number[j+1]*number[j+2]*number[j+3]*number[j+4];
                    if(Straight2==1||AKQJ==1){
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
    }

    //勝敗表示//
    public String Winlose(Hand player1,Hand player2){
        if(player1.rank<player2.rank){
            player1.chip = player1.chip + player1.betchip + player2.betchip;
            System.out.print("あなたの勝利です");
            return "あなたの勝利です";
        }else if(player1.rank>player2.rank){
            player2.chip = player2.chip + player1.betchip + player2.betchip;
            System.out.println("相手の勝利です");
            return "相手の勝利です";

        }else{
            for(int a=0;a<=4;a++){
                if(player1.Kicker[a]>player2.Kicker[a]){
                    player1.chip = player1.chip + player1.betchip + player2.betchip;
                    System.out.print("あなたの勝利です");
                    return "あなたの勝利です";
                }else if(player1.Kicker[a]<player2.Kicker[a]){
                    player2.chip = player2.chip + player1.betchip + player2.betchip;
                    System.out.print("相手の勝利です");
                    return "相手の勝利です";
                }
                if(a==5){
                    if(player1.Kicker[a]==player2.Kicker[a]){
                        player1.chip = player1.chip + player1.betchip;
                        player2.chip = player2.chip + player2.betchip;
                        System.out.println("引き分けです");
                        return "引き分けです";
                    }
                }
            }
        }
        return "エラー";
    }
    
    // 賭け金を決定 //
    public void Bet(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("現在のあなたのチップは"+ chip + "です。");
        System.out.print("賭け金を入力：");
        betchip = scanner.nextInt();
        chip = chip - betchip;
    }
    
    public void SetHand(Hand player) {
        this.hand=player.hand;
        this.copycounter++;
    }

    //キッカーの取得//
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
                            check5++;
                            if(check5==5){break;}
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
                            check9++;
                            if(check9==4){break;}
                    }
                }
                for(int s=14;s>=2;s--){
                    if(number[s]==2){
                        player.Kicker[0]=s;
                        }
                    }
                }
                break;
            case 10:
                for(int s=14;s>=2;s--){
                    if(number[s]==1){
                        if(player.Kicker[check5]==0){
                            player.Kicker[check5]=s;
                            check5++;
                            if(check5==5){break;}
                        }
                    }
                }
                break;
        }
    }
}

class Deck{
    final int totalcard=52;// カードの総数
    private List<String> cardlist;

    // 山札の初期化 //
    public void Reset(){
        cardlist = new ArrayList<>();
        for(int i=1;i<=52;i++){
            String a=String.valueOf(i);   
            cardlist.add(a);
           }
    }

    // カードを1枚引く //
    public int Draw(){
      int number;
      String Renumber;
      do{    
        Random rand =new Random();
        number =rand.nextInt(totalcard)+1;
        Renumber=String.valueOf(number);
       }while(!cardlist.contains(Renumber));
       cardlist.remove(Renumber);
       return number;
    }

    public void CopyDeck(Deck deck){
        this.cardlist=deck.cardlist;
    }
}
