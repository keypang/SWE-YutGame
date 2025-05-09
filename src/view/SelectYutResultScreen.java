package view;

import javax.swing.*;
import java.awt.*;

public class SelectYutResultScreen extends JFrame {

  // 선택 결과를 전달할 리스너 인터페이스
  public interface YutSelectListener {

    void onYutSelected(String result);
  }

  private YutSelectListener listener;

  public SelectYutResultScreen(YutSelectListener listener, String[] yutResult) {
    super("지정윷 선택");
    this.listener = listener;
    InitUI(yutResult);
  }

  private void InitUI(String[] yutResult) {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(800, 200);
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(20, 20, 10, 10);
    getContentPane().setBackground(new Color(255, 245, 230));

    for (int i = 0; i < yutResult.length; i++) {
      JButton button = new JButton(yutResult[i]);
      final String result = yutResult[i];
      button.setFont(new Font("SansSerif", Font.BOLD, 18));
      button.setBackground(new Color(120, 200, 120));
      button.setForeground(Color.WHITE);
      button.setFocusPainted(false);
      button.setPreferredSize(new Dimension(100, 70));
      button.addActionListener(e -> {
        // 선택된 결과를 리스너에 전달
        if (listener != null) {
          listener.onYutSelected(result);
        }
        System.out.println("선택된 윷 결과: " + result);
        dispose();
      });

      gbc.gridx = i;
      gbc.gridy = 0;
      add(button, gbc);
    }

    setLocationRelativeTo(null);
    setVisible(true);
  }
}