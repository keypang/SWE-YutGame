package view.global;

import model.BoardType;

/**
 * 아래와 같은 메소드를 정의한 이유 Controller 는 GameView 라는 인터페이스에 의존하고 있음(구체적인 클래스에 의존하는 게 아닌 추상적인 것에 의존) 즉,
 * SwingView 가 할당될 수도 있고, JAVAFXView 가 할당될 수도 있다는 것. Controller 가 각기 다른 UI 에 대해 "컨트롤러 코드의 수정 없이" 작업을
 * 수행해야함. 게임 설정 화면을 예시로 들어보자. swing 에서는 combobox 를 통해 사용자 입력을 받음. 근데. javafx 에서는 어떤 방식인지 모름 그렇기 때문에
 * 인터페이스에서 사용자 입력을 얻어오는 방식, 오류 메시지 표시 방식, 뷰를 닫는 방식 등을 추상화 해놓고 Swing 화면과 Javafx 화면을 제작할때 이 메소드들을
 * 구체적으로 구현하는 것 .
 */

public interface GameConfigView {

  /**
   * 사용자가 선택한 플레이어 수를 반환합니다.
   *
   * @return 선택된 플레이어 수 (일반적으로 2-4)
   */
  int getSelectedPlayerCount();

  /**
   * 사용자가 선택한 플레이어당 말 개수를 반환합니다.
   *
   * @return 선택된 플레이어당 말 개수
   */
  int getSelectedPieceCount();

  /**
   * 사용자가 선택한 보드 유형을 반환합니다.
   *
   * @return 선택된 보드 유형 (SQUARE, PENTAGON, HEXAGON)
   */
  BoardType getSelectedBoardType();

  /**
   * 시작 버튼 클릭 이벤트 리스너를 설정합니다.
   *
   * @param listener 시작 버튼이 클릭될 때 통보받을 리스너
   */
  void setStartButtonListener(StartButtonListener listener);

  /**
   * 사용자에게 오류 메시지를 표시합니다.
   *
   * @param message 표시할 오류 메시지
   */
  void displayErrorMessage(String message);

  /**
   * 설정 화면을 닫습니다. 이 메서드는 설정 과정이 완료되었거나 다른 이유로 화면을 닫아야 할 때 호출됩니다.
   */
  void close();

  /**
   * 시작 버튼 클릭 이벤트를 처리하기 위한 인터페이스. 설정 화면에서 시작 버튼을 클릭했을 때 실행될 작업
   */
  interface StartButtonListener {

    /**
     * 시작 버튼이 클릭되었을 때 호출됩니다. 이 메서드는 컨트롤러에 의해 구현되어 사용자가 구성된 설정으로 게임을 시작하려는 요청을 처리
     */
    void onStartButtonClicked();
  }
}