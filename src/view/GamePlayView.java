package view;

import model.YutResult;
import java.util.List;

public interface GamePlayView {

    interface ThrowButtonListener {
        YutResult onThrowButtonClicked();
    }

    // 지정 윷 던지기 리스너 추가
    interface FixedYutButtonListener {
        YutResult onFixedYutSelected(String yutType);
    }

    // 게임 종료 시 동작 리스너
    interface GameEndListener {
        void onRestartGame(); // 같은 설정으로 재시작
        void onNewGameSetup(); // 새 설정으로 게임 시작
        void onExitGame(); // 게임 종료
    }

    void setThrowButtonListener(ThrowButtonListener throwButtonListener);

    // 지정 윷 리스너 설정 메서드 추가
    void setFixedYutButtonListener(FixedYutButtonListener listener);

    // 게임 종료 리스너 설정
    void setGameEndListener(GameEndListener listener);

    // 윷 결과 리스트를 표시하는 메서드 추가
    void displayYutResultList(List<YutResult> results);

    // 현재 턴 플레이어를 표시하는 메서드 추가
    void updateCurrentPlayer(int playerNumber);

    // 윷 던지기 버튼 활성화/비활성화 설정 메서드 추가
    void setThrowButtonEnabled(boolean enabled);

    // 현재 게임 상태 메시지 표시 메서드 추가
    void setStatusMessage(String message);

    // 게임 종료 화면 표시
    void showGameEndDialog(int winnerPlayer);
}