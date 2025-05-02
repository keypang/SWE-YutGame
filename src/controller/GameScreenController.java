package controller;

import model.GameManager;
import model.PositionDTO;
import model.Yut;
import model.YutResult;
import view.GameConfigView;
import view.GamePlayView;
import view.SwingConfigScreen;
import view.SwingPlayScreen;

import javax.swing.*;
import java.util.List;

public class GameScreenController {
    private GamePlayView gameView;
    private GameManager gameManager;
    private Yut yut;

    public GameScreenController(GamePlayView gameView, GameManager model){
        this.gameView = gameView;
        this.gameManager = model;

        initController();
    }


    // 컨트롤러 초기화
    private void initController() {
        // 랜덤 윷 던지기 리스너 설정
        gameView.setThrowButtonListener(new GamePlayView.ThrowButtonListener(){
            @Override
            public YutResult onThrowButtonClicked() {
                return RandomYutThrow();
            }
        });

        // 지정 윷 던지기 리스너 설정
        gameView.setFixedYutButtonListener(new GamePlayView.FixedYutButtonListener() {
            @Override
            public YutResult onFixedYutSelected(String yutType) {
                return FixedYutThrow(yutType);
            }
        });

        // 게임 종료 리스너 설정
        gameView.setGameEndListener(new GamePlayView.GameEndListener() {
            @Override
            public void onRestartGame() {
                restartGame();
            }

            @Override
            public void onNewGameSetup() {
                newGameSetup();
            }

            @Override
            public void onExitGame() {
                exitGame();
            }
        });

        // 초기 플레이어 설정 (1번 플레이어부터 시작)
        gameView.updateCurrentPlayer(1);

        // 초기 윷 결과 리스트 초기화 (빈 리스트)
        updateYutResultsInView();

        // 초기 버튼 상태 설정 (활성화)
        gameView.setThrowButtonEnabled(true);
        gameView.setStatusMessage("게임 시작! 윷을 던져주세요.");
    }

    // 랜덤 윷 던지는 버튼을 눌렀을 때
    public YutResult RandomYutThrow(){
        // 윷 결과 얻기
        YutResult yutResult = gameManager.throwYut();

        // 결과에 따라 버튼 상태 및 메시지 업데이트
        updateGameStateAfterYutThrow(yutResult);

        // 승리 여부 확인
        int winner = gameManager.checkWin();
        if (winner > 0) {
            // 승리자가 있는 경우 게임 종료 다이얼로그 표시
            gameView.showGameEndDialog(winner);
            return yutResult;
        }

        // 현재 턴 플레이어 확인 및 업데이트
        int currentPlayer = gameManager.checkPlayer();
        gameView.updateCurrentPlayer(currentPlayer);

        // 윷 결과 리스트 업데이트
        updateYutResultsInView();

        return yutResult;
    }

    // 윷 던진 후 게임 상태 업데이트
    private void updateGameStateAfterYutThrow(YutResult yutResult) {
        // 윷이나 모가 나온 경우 (추가 턴)
        if (yutResult.canRollAgain()) {
            gameView.setThrowButtonEnabled(true);
            gameView.setStatusMessage(yutResult.name() + "가 나왔습니다! 한 번 더 던지세요.");
        } else {
            // 추가 턴이 없는 경우
            gameView.setThrowButtonEnabled(false);
            gameView.setStatusMessage(yutResult.name() + "가 나왔습니다. 말을 선택하여 이동하세요.");
        }
    }

    // 윷 결과 리스트를 뷰에 표시
    private void updateYutResultsInView() {
        // GameManager에서 현재 가지고 있는 윷 결과 리스트 가져오기
        gameView.displayYutResultList(gameManager.getYutResults());
    }

    // 지정 윷 던지는 버튼을 눌렀을 때
    public YutResult FixedYutThrow(String getresult){
        // 지정된 윷 결과 얻기 (enum 값으로 변환)
        YutResult yutResult = gameManager.throwFixedYut(getresult);

        // 결과 바탕으로 버튼 상태 및 메시지 업데이트
        updateGameStateAfterYutThrow(yutResult);

        // 승리 여부 확인
        int winner = gameManager.checkWin();
        if (winner > 0) {
            // 승리자가 있는 경우 게임 종료 다이얼로그 표시
            gameView.showGameEndDialog(winner);
            return yutResult;
        }

        // 현재 턴 플레이어 확인g하고 업데이트
        int currentPlayer = gameManager.checkPlayer();
        gameView.updateCurrentPlayer(currentPlayer);

        // 윷 결과 리스트 업데이트
        updateYutResultsInView();

        return yutResult;
    }

    // 윷 결과 리스트 가져오기
    public List<YutResult> getYutResults() {
        return gameManager.getYutResults();
    }

    // 게임 재시작
    private void restartGame() {
        // 게임 매니저 초기화
        gameManager.initGM(gameManager.getStartInfo());

        // 뷰 초기화
        gameView.updateCurrentPlayer(1);

        // 윷 결과 리스트 초기화
        updateYutResultsInView();

        // 윷 이미지 초기화
        if (gameView instanceof SwingPlayScreen) {
            ((SwingPlayScreen) gameView).clearYutImage();
        }

        // 버튼 상태 초기화
        gameView.setThrowButtonEnabled(true);

        // 상태 메시지 초기화
        gameView.setStatusMessage("게임이 다시 시작되었습니다! 윷을 던져주세요.");
    }





    // 새 설정으로 게임 시작
    private void newGameSetup() {
        // 현재 게임 화면 닫기
        if (gameView instanceof JFrame) {
            ((JFrame) gameView).dispose();
        }

        // 설정 화면 생성 및 표시
        GameConfigView configView = new SwingConfigScreen();

        // 새 게임 관리자 생성
        GameManager newGameManager = new GameManager();

        // 설정 화면 컨트롤러 생성
        StartScreenController startController = new StartScreenController(configView, newGameManager);
    }

    // 윷 굴리기 이전 초기상태 설정 (턴 넘어 갔을 때)

    // 말을 선택했을 때
    public int[] PieceSelect(int selectpiece){
        return gameManager.findMovableCells(selectpiece);
        // 지금 생각해야 할게 selectpiece를 gameManager가 저장하고 있어야 하는 거 아닌가?

    }

    // 좌표 선택했을 때
    public void selectCoordinate(int coordinate){
        // 모델에서는 선택된 말이 무엇인지를 알고 있어야함.

        // 좌표값을 움직이는 칸수로 변환해야함. (현재 model에서 구현된 방식)
        // 잡혔을 때 extraturn이 바뀌어 있는지 확인해야함.

        if (gameManager.getExtraTurn()){
            // 윷버튼 활성화
        }
        else {
            if (gameManager.isYutResultsEmpty()) {
                // 턴넘기기
            }
            else {
                // 일단 전체 상태를 넘겨주고
                // 말을 선택할 수 있는 상태로 가야 하는데
            }
        }

    }
    // 게임 종료
    private void exitGame() {
        System.exit(0);
    }
}
