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
import java.util.Map;

public class GameScreenController {
    private GamePlayView gameView;
    private GameManager gameManager;

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

        // 말 꺼내기 리스너 설정
        gameView.setTakeOutButtonListener(new GamePlayView.TakeOutButtonListener(){
            public List<PositionDTO> onTakeOutButtonClicked() {
                return gameManager.getAllPiecePos();
            }
        });

        // 말 선택 리스너 설정
        gameView.setPieceSelectionListener(new GamePlayView.PieceSelectionListener() {
            @Override
            public Map<Integer, Integer> onPieceSelected(int pieceId) {
                return PieceSelect(pieceId);
            }
        });

        // 셀 선택 리스너 설정
        gameView.setCellSelectionListener(new GamePlayView.CellSelectionListener() {
            @Override
            public void onCellSelected(int cellId) {
                selectCoordinate(cellId);
            }
        });

        // 전체상태 넘기기
        gameView.setTakeOutButtonListener(new GamePlayView.TakeOutButtonListener() {
            @Override
            public List<PositionDTO> onTakeOutButtonClicked() {
                return takeOutPiece();
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

        //////////////// 여기 부터 x
        // 승리 여부 확인
        int winner = gameManager.checkWin();
        if (winner > 0) {
            // 승리자가 있는 경우 게임 종료 다이얼로그 표시
            gameView.showGameEndDialog(winner);
            return yutResult;
        }

        // 윷 결과 리스트 업데이트
        updateYutResultsInView();

        return yutResult;
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

        // 윷 결과 리스트 업데이트
        updateYutResultsInView();

        return yutResult;
    }

    // 윷 던진 후 게임 상태 업데이트
    private void updateGameStateAfterYutThrow(YutResult yutResult) {
        // 윷이나 모가 나온 경우 (추가 턴)
        if (yutResult.canRollAgain()) {
            gameManager.setExtraTurn(true);
            gameView.setThrowButtonEnabled(true);
            gameView.setStatusMessage(yutResult.name() + "가 나왔습니다! 한 번 더 던지세요.");
        } else {
            // 추가 턴이 없는 경우
            gameManager.setExtraTurn(false);
            gameView.setThrowButtonEnabled(false);
            gameView.setStatusMessage(yutResult.name() + "가 나왔습니다. 말을 선택하여 이동하세요.");
            gameView.updateCurrentPlayer(gameManager.getCurrentPlayer());
            gameView.enableWaitingPieceSelection();
        }
        System.out.println("!!!!!현재 플레이어는" + gameManager.getCurrentPlayer());
    }

    // 윷 결과 리스트를 뷰에 표시
    private void updateYutResultsInView() {
        // GameManager에서 현재 가지고 있는 윷 결과 리스트 가져오기
        gameView.displayYutResultList(gameManager.getYutResults());
    }

    // 윷 결과 리스트 가져오기
    public List<YutResult> getYutResults() {
        return gameManager.getYutResults();
    }

    // 말을 선택했을 때
    public Map<Integer, Integer> PieceSelect(int selectpiece){

        gameManager.setSelectedpiece(selectpiece);
        Map<Integer, Integer> movable = gameManager.findMovableCells(gameManager.getselectedsellid());
        return movable;
    }

    // 좌표 선택했을 때
    public void selectCoordinate(int cellId){
        // 골라서 넘겨준 해시맵을 리스트에서 제거해야함. + 잡혔을 때 extraturn이 바뀌어 있는지 확인해야함.
        // 해시맵에서 선택했던 값만 추출
        Integer value = gameManager.getMovableMap().get(cellId);

        // 윷리스트에서 선택했던 값 삭제
        gameManager.removeYutResult(value);

        // 현재 extraTurn 상태 저장 (말을 잡았는지 확인용)
        boolean wasExtraTurn = gameManager.getExtraTurn();

        // 윷 결과 리스트를 뷰에 업데이트 하고 선택한 좌표로 말 이동
        updateYutResultsInView();
        gameManager.processYutResult(value);

        // 말 이동 후 extraTurn 상태가 true로 바뀌었다면 말을 잡았다는 의미
        boolean captureOccurred = !wasExtraTurn && gameManager.getExtraTurn();

        // 업데이트된 말 위치 화면 표시
        gameView.repaintAllPieces();

        // 말이 이동했으므로 파란색 테두리로 표시 제거
        gameView.clearPieceSelection();

        // 캡처 발생 시 메시지 표시
        if (captureOccurred) {
            gameView.setStatusMessage("말을 잡았습니다! 한 번 더 던지세요.");
        }

        // 승리 여부 확인 - 이 부분 추가
        int winner = gameManager.checkWin();
        if (winner > 0) {
            // 승리자가 있는 경우 게임 종료 다이얼로그 표시
            gameView.showGameEndDialog(winner);
            return;
        }

        if (gameManager.getExtraTurn()){
            // 윷버튼 활성화
            gameView.setThrowButtonEnabled(true);
        }
        else {
            if (gameManager.isYutResultsEmpty()) {
                // 현재 턴 플레이어 확인 및 업데이트
                int currentPlayer = gameManager.checkPlayer();
                gameView.updateCurrentPlayer(currentPlayer);

                // 윷버튼 활성화
                gameView.setThrowButtonEnabled(true);
            }
            else {
                // 말선택 페이즈로 이동
                gameView.enableWaitingPieceSelection();
            }
        }
    }

    // 게임 재시작
    private void restartGame() {
        // 게임 매니저 초기화
        gameManager.initGM(gameManager.getStartInfo());

        // 뷰 초기화
        gameView.updateCurrentPlayer(0);

        // 윷 결과 리스트 초기화
        updateYutResultsInView();

        // 윷 이미지 초기화
        gameView.clearYutImage();

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

    // 모든 말의 위치정보 가져오기
    public List<PositionDTO> takeOutPiece(){
        return gameManager.getAllPiecePos();
    }

    // 게임 종료
    private void exitGame() {
        System.exit(0);
    }
}