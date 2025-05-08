package view;

import model.YutResult;
import model.PositionDTO;

import java.util.ArrayList;
import java.util.Map;
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

    // 말 꺼내기 시 동작 리스너
    interface TakeOutButtonListener{
        List<PositionDTO> onTakeOutButtonClicked();
    }
    
    // 말 선택 시 정보 전달 리스너
    interface PieceSelectionListener {
        Map<Integer, Integer> onPieceSelected(int pieceId);
    }

    // 이동할 셀 선택 시 정보 전달 리스너
    interface CellSelectionListener {
        void onCellSelected(int cellId);
    }

    void setThrowButtonListener(ThrowButtonListener throwButtonListener);

    // 지정 윷 리스너 설정 메서드 추가
    void setFixedYutButtonListener(FixedYutButtonListener listener);

    // 말 선택 리스너 설정 메서드 추가
    void setPieceSelectionListener(PieceSelectionListener listener);

    void setCellSelectionListener(CellSelectionListener listener);

    // 게임 종료 리스너 설정
    void setGameEndListener(GameEndListener listener);

    // 윷 결과 리스트를 표시하는 메서드 추가
    void displayYutResultList(List<YutResult> results);

    // 윷 선택하는 패널(팝업) 생성 메서드
    void showYutSelectPanel(YutResult[] yutResult);

    // 새로운 말 꺼내기
    void setTakeOutButtonListener(TakeOutButtonListener takeOutButtonListener);

    // 현재 턴 플레이어를 표시하는 메서드 추가
    void updateCurrentPlayer(int playerNumber);

    // 윷 던지기 버튼 활성화/비활성화 설정 메서드 추가
    void setThrowButtonEnabled(boolean enabled);

    void enableWaitingPieceSelection();

    void disablePieceSelection();

    // 현재 게임 상태 메시지 표시 메서드 추가
    void setStatusMessage(String message);

    // 말 전체 다시 그리기 메서드
    void repaintAllPieces();

    // 게임 종료 화면 표시
    void showGameEndDialog(int winnerPlayer);


    // 말이 윷판에 놓였을때 테두리 쳐지는 문제 해결
    void clearPieceSelection();

    // 윷 이미지 초기화
    void clearYutImage();

}
