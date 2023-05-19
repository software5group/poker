import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {
    private MainFrame frame;
    private Hand player; //メインフレームから受け取るHandインスタンスを格納
    private JLabel rule; // ルール説明用のラベル
    private JLabel[] cardLabels; //カード表示用のラベル
    private JCheckBox[] checkBoxes; //カード交換選択用のチェックボックス
    private JButton exchangeButton; //カード交換用ボタン
    private int checkCount; //交換する枚数
    private boolean[] changeFlag; //各カードを交換するかどうか
    private int[] change; //交換するカード番号(1 - 5)

    public GamePanel(MainFrame frame) {
        this.frame = frame;

        setLayout(new GridBagLayout());
        setBackground(new Color(0, 100, 0));  // 背景色を深緑に設定

        GridBagConstraints constraints = new GridBagConstraints();

        // 説明表示用のパネル（ーーー未実装ーーー）

        // カード表示用のパネル
        cardLabels = new JLabel[5];
        checkBoxes = new JCheckBox[5];
        for (int i = 0; i < 5; i++) {
            // メインフレームからHandインスタンスを受け取る
            player = frame.player;
            // カードを表示
            //ImageIcon cardImage = new ImageIcon("images/"+ player.hand[i] + ".png");
            ImageIcon cardImage = new ImageIcon("images/"+ (i + 1) + ".png");
            cardLabels[i] = new JLabel(cardImage);
            constraints.gridx = i;
            constraints.gridy = 0;
            add(cardLabels[i], constraints);
            
            // 交換用チェックボックスを表示
            checkBoxes[i] = new JCheckBox("Change");
            checkBoxes[i].setPreferredSize(new Dimension(20, 20)); // チェックボックスの大きさを設定
            constraints.gridy = 1;
            add(checkBoxes[i], constraints);
        }

        // ボタン配置用のパネル
        exchangeButton = new JButton("Exchange Cards");
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.insets = new Insets(40,0,0,0);//空白
        add(exchangeButton, constraints);

        // ボタンにリスナーを追加
        exchangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //交換する枚数とカード番号の更新
                changeFlag = new boolean[5];
                for (int i = 0; i < 5; i++) {
                    if (checkBoxes[i].isSelected()) {
                       checkCount++;
                       changeFlag[i] = true;        
                    } 
                }
                change = new int[checkCount];
                int n = 0;
                for (int i = 0; i < 5; i++) {
                    if (changeFlag[i]) {
                        change[n++] = i;
                    }
                }
                //交換後の画面に移動
                frame.showGame2();
            }
        });

        // 画面全体を使わずに周囲に余白を作成
        setBorder(BorderFactory.createEmptyBorder(30,30,30,30)); 
    }

    public int getCheckcount() {
        return checkCount;
    }

    public int[] getChange() {
        return change;
    }

}
