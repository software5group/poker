import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel2 extends JPanel {
    private MainFrame frame;
    private JLabel[] cardLabels; //カード表示用のラベル
    private JButton resultButton; //結果表示用ボタン

    public GamePanel2(MainFrame frame) {
        this.frame = frame;

        setLayout(new GridBagLayout());
        setBackground(new Color(0, 100, 0));  //背景色を深緑に設定

        GridBagConstraints constraints = new GridBagConstraints();

        //交換後のカード表示
        cardLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            ImageIcon cardImage = new ImageIcon("images/"+ (i + 1) + ".png");
            cardLabels[i] = new JLabel(cardImage);
            constraints.gridx = i;
            constraints.gridy = 0;
            add(cardLabels[i], constraints);
            constraints.gridy = 1;
        }

        //ボタン配置
        constraints.insets = new Insets(40,0,0,0);//空白

        resultButton = new JButton("Show Result");
        constraints.gridx = 2;
        constraints.gridy = 2;        
        add(resultButton, constraints);

        //リザルトボタンにリスナーを追加する
        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 結果表示パネルに移動する処理をここに書く
                frame.showResult();
            }
        });

        //周囲に余白を作成
        setBorder(BorderFactory.createEmptyBorder(30,30,30,30)); 
    }
}
