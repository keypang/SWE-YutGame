package view;

import model.BoardType;

public interface GameView {

    /**
     * 아래와 같은 메소드를 정의한 이유
     * Controller 는 GameView 라는 인터페이스에 의존하고 있음(구체적인 클래스에 의존하는 게 아닌 추상적인 것에 의존)
     * 즉, SwingView 가 할당될 수도 있고, JAVAFXView 가 할당될 수도 있다는 것.
     * Controller 가 각기 다른 UI 에 대해 "컨트롤러 코드의 수정 없이" 작업을 수행해야함.
     * 게임 설정 화면을 예시로 들어보자.
     * swing 에서는 combobox 를 통해 사용자 입력을 받음. 근데. javafx 에서는 어떤 방식인지 모름
     * 그렇기 때문에 인터페이스에서 사용자 입력을 얻어오는 방식, 오류 메시지 표시 방식, 뷰를 닫는 방식 등을 추상화 해놓고
     * Swing 화면과 Javafx 화면을 제작할때 이 메소드들을 구체적으로 구현하는 것 .
     */

    // 사용자 입력 얻기
    int getSelectedPlayerCount();
    int getSelectedPieceCount();
    BoardType getSelectedBoardType();

    void setStartButtonListener(StartButtonListener listener);

    // 오류 메시지 표시
    void displayErrorMessage(String message);

    // 뷰 닫기
    void close();

    interface StartButtonListener {
        void onStartButtonClicked();
    }
}