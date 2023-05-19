import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TitlePanel extends JPanel {
    private JButton startButton, exitButton;

    public TitlePanel(MainFrame frame) {
        // レイアウト設定
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 100, 0));  // 背景色を深緑に設定
        GridBagConstraints constraints = new GridBagConstraints();

        // タイトルラベルの設定
        JLabel titleLabel = new JLabel("Java Online Poker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50)); // 文字サイズを設定
        titleLabel.setForeground(Color.WHITE); // 文字の色の設定
        constraints.gridx = 0; 
        constraints.gridy = 0; 
        constraints.insets = new Insets(40,0,0,0);
        add(titleLabel, constraints);

        // スタートボタンの設定
        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ゲーム画面に移動する
                frame.showGame();
            }
        });
        constraints.insets = new Insets(80,0,0,0);
        constraints.gridy = 1;  // ボタンの位置を設定
        add(startButton, constraints);

        // 終了ボタンの設定
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 50));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ゲームを終了
                System.exit(0);
            }
        });
        constraints.gridy = 2;  // ボタンの位置を設定
        constraints.insets = new Insets(10,0,0,0);
        add(exitButton, constraints);
    }
}
