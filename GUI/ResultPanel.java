import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultPanel extends JPanel {
    private MainFrame frame;
    private JLabel[] enemyCardLabels; //相手のカード表示用のラベル
    private JLabel[] myCardLabels; //自分のカード表示用のラベル
    private JLabel enemyRole; // 相手の役の表示用
    private JLabel myRole; // 自分の役の表示用
    private JLabel resultLabel; // 結果表示用
    private JButton exitButton; // 終了ボタン

    public ResultPanel(MainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(new Color(0, 100, 0));  // 背景色を深緑に設定

        // 結果表示ラベルの作成
        resultLabel = new JLabel("Result will be displayed here", SwingConstants.CENTER);
        add(resultLabel, BorderLayout.CENTER);

        // 終了ボタンの作成
        exitButton = new JButton("Exit");
        add(exitButton, BorderLayout.SOUTH);

        // 終了ボタンにアクションリスナーを追加
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // プログラムを終了
            }
        });
    }

    // 結果を設定するメソッド
    public void setResult(String result) {
        resultLabel.setText(result);
    }
}