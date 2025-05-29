package view.swing;

import model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.global.GameConfigView;

/**
 * SwingConfigScreen 클래스는 Swing 기반으로 구성된 윷놀이 게임 설정 화면임. 사용자는 이 화면에서 플레이어 수, 말 개수, 판 종류를 선택 할
 * 수 있으며, 설정된 정보는 GameConfigView 인터페이스를 통해 컨트롤러와 연동됨.
 */
public class SwingConfigScreen extends JFrame implements GameConfigView {

  /**
   * 게임 설정을 선택하는 콤보박스 (플레이어 수, 말 개수, 판 종류)
   */
  private JComboBox<Integer> playerCountCombo;
  private JComboBox<Integer> pieceCountCombo;
  private JComboBox<BoardType> boardTypeCombo;

  /**
   * 게임 시작 버튼
   */
  private JButton startButton;

  /**
   * 시작 버튼 클릭 시 동작할 리스너
   */
  private StartButtonListener startButtonListener;

  /**
   * StartButtonListener 는 GameView 내부에 속해 있는 인터페이스임 인터페이스이기 때문에 메소드를 필수적으로 구현해야하는데, 그 메소드는
   * onStartButtonClicked() 임. Controller 코드를 보면, 익명 클래스로 StartButtonListener 만들고
   * onStartButtonClicked() 메소드 구현까지 한번에 해서 매개변수로 넘기는 걸 볼 수 있음. 이 시점에서 어떤 정보를 알 수 있냐면, Controller
   * ---> View 로 StartButtonListener 를 넘겨주었고, View 에서 StartButtonListener 의 onStartButtonClicked() 가
   * 호출된다면 , Controller 의 process() 메소드가 호출된다는 것을 알 수 있음. 왜냐하면, startButton 를 클릭하면
   * startButton.addActionListener(new ActionListener() {
   *
   * @Override public void actionPerformed(ActionEvent e) { if (startButtonListener != null) {
   * startButtonListener.onStartButtonClicked(); } } }); actionPerformed 가 수행되면서, 위 코드가 실행되기 때문.
   * startButtonListener.onStartButtonClicked() 이 코드는 Controller 의 process() 코드와 동일하다고 보면 됨.
   */

  /**
   * Swing 기반 게임 설정 화면을 초기화합니다.
   * 기본 제목과 UI 구성을 설정합니다.
   */
  public SwingConfigScreen() {
    super("게임 설정");
    initUI();
  }

  /**
   * 게임 설정 UI를 초기화합니다.
   * 각 요소(플레이어 수, 말 개수, 판 종류, 설명, 시작 버튼)를 배치하고 이벤트를 설정합니다.
   */
  private void initUI() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 500);
    setLayout(new GridBagLayout());
    getContentPane().setBackground(new Color(255, 245, 230));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // 제목 라벨
    JLabel titleLabel = new JLabel("윷놀이 게임 설정");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(20, 10, 30, 10);
    add(titleLabel, gbc);

    // 인원 수
    JLabel playerLabel = new JLabel("인원 수", SwingConstants.CENTER);
    playerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(10, 40, 10, 40);
    gbc.gridx = 0;
    add(playerLabel, gbc);

    playerCountCombo = new JComboBox<>(new Integer[]{2, 3, 4});
    playerCountCombo.setSelectedIndex(2);
    playerCountCombo.setFont(new Font("SansSerif", Font.PLAIN, 16));
    gbc.gridy = 2;
    add(playerCountCombo, gbc);

    // 말 개수
    JLabel pieceLabel = new JLabel("말 개수", SwingConstants.CENTER);
    pieceLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
    gbc.gridx = 1;
    gbc.gridy = 1;
    add(pieceLabel, gbc);

    pieceCountCombo = new JComboBox<>(new Integer[]{2, 3, 4, 5});
    pieceCountCombo.setSelectedIndex(1);
    pieceCountCombo.setFont(new Font("SansSerif", Font.PLAIN, 16));
    gbc.gridy = 2;
    add(pieceCountCombo, gbc);

    // 판 종류
    JLabel boardLabel = new JLabel("판 종류", SwingConstants.CENTER);
    boardLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
    gbc.gridx = 2;
    gbc.gridy = 1;
    add(boardLabel, gbc);

    // BoardType 열거형 사용하여 콤보박스 생성
    boardTypeCombo = new JComboBox<>(BoardType.values());
    boardTypeCombo.setFont(new Font("SansSerif", Font.PLAIN, 16));
    gbc.gridy = 2;
    add(boardTypeCombo, gbc);

    // 게임 설명 패널
    JPanel descriptionPanel = createDescriptionPanel();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(30, 20, 20, 20);
    add(descriptionPanel, gbc);

    // 게임 시작 버튼
    startButton = new JButton("게임 시작");
    startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
    startButton.setPreferredSize(new Dimension(150, 40));
    startButton.setBackground(new Color(120, 200, 120));
    startButton.setForeground(Color.WHITE);
    startButton.setFocusPainted(false);

    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (startButtonListener != null) {
          startButtonListener.onStartButtonClicked();
        }
      }
    });

    gbc.gridy = 4;
    gbc.gridwidth = 3;
    gbc.insets = new Insets(10, 10, 20, 10);
    add(startButton, gbc);

    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * 게임 설명을 포함하는 설명 패널을 생성합니다.
   *
   * @return 설명이 포함된 JLPanel 객체
   */
  private JPanel createDescriptionPanel() {

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createTitledBorder("게임 설명"));
    panel.setBackground(new Color(250, 250, 250));

    JTextArea descriptionText = new JTextArea("윷놀이");
    descriptionText.setFont(new Font("SansSerif", Font.PLAIN, 14));
    descriptionText.setEditable(false);
    descriptionText.setLineWrap(true);
    descriptionText.setWrapStyleWord(true);
    descriptionText.setBackground(new Color(250, 250, 250));
    descriptionText.setMargin(new Insets(10, 10, 10, 10));

    panel.add(descriptionText);

    return panel;
  }

  /**
   * 사용자가 선택한 정보를 정수형 또는 BoardType으로 반환합니다.
   *
   * @return 선택된 플레이어 수, 말 개수, 판 종류
   */
  @Override
  public int getSelectedPlayerCount() {
    return (Integer) playerCountCombo.getSelectedItem();
  }

  @Override
  public int getSelectedPieceCount() {
    return (Integer) pieceCountCombo.getSelectedItem();
  }

  @Override
  public BoardType getSelectedBoardType() {
    return (BoardType) boardTypeCombo.getSelectedItem();
  }

  /**
   * GameConfigView 인터페이스를 구현(implement)하는 메서드입니다.
   * 사용자가 선택한 정보를 컨트롤러로 전달하는 역할을 수행합니다.
   *
   */
  @Override
  public void setStartButtonListener(StartButtonListener listener) {
    this.startButtonListener = listener;
  }

  /**
   * 사용자에게 오류 메세지를 팝업으로 표시합니다.
   *
   * @param message 표시할 오류 메시지
   */
  @Override
  public void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * 게임 설정 창을 닫습니다.
   */
  @Override
  public void close() {
    dispose();
  }
}