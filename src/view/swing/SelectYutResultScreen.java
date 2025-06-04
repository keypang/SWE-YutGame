package view.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Swing 기반의 지점 윷 선택 팝업 화면 클래스임. 지정 윷 던지기 또는 사용자가 제공된 윷 결과 중 하나를 버튼 클릭으로 선택할 수 있도록 함
 * 선택 결과는 YutSelectListener 인터페이스를 통해 컨트롤러로 전달됨.
 */

public class SelectYutResultScreen extends JDialog {

  /**
   * 윷 선택 결과를 처리하기 위한 리스너 인터페이스입니다.
   * 윷 결과가 선택되었을 때 호출되어, 선택 결과를 컨트롤러로 전달합니다.
   */
  public interface YutSelectListener {

    void onYutSelected(String result);
  }

  private YutSelectListener listener;

  /**
   * 윷 선택 팝업을 생성하고 UI를 초기화합니다.
   *
   * @param parent 메인 프레임
   * @param listener 윷 선택 결과를 전달할 리스너
   * @param yutResult 사용자에게 보여줄 윷 결과 목록
   */
  public SelectYutResultScreen(JFrame parent, YutSelectListener listener, String[] yutResult) {
    super(parent, "지정윷 선택", true);
    this.listener = listener;
    InitUI(yutResult);
  }

  /**
   * 지정된 윷 결과 목록을 기반으로 버튼 UI를 구성하고 팝업을 표시합니다.
   *
   * @param yutResult 표시할 윷 결과 목록
   */
  private void InitUI(String[] yutResult) {
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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

      /**
       * 버튼이 클리기되면 선택된 윷 결과를 리스너에 전달하고 팝업을 종료합니다.
       */
      button.addActionListener(e -> {
        // 선택된 결과를 리스너에 전달
        dispose();
        if (listener != null) {
          listener.onYutSelected(result);
        }
        System.out.println("선택된 윷 결과: " + result);
      });

      gbc.gridx = i;
      gbc.gridy = 0;
      add(button, gbc);
    }

    setLocationRelativeTo(null);
    setVisible(true);
  }
}