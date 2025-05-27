package controller;

import model.*;
import view.GameConfigView;
import view.GamePlayView;
import view.SwingConfigScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameScreenController {

  private GamePlayView gameView;
  private GameManager gameManager;

  /**
   * GameScreenController 생성자입니다.
   * 사용자가 선택한 게임 화면(View)과 게임 로직을 처리하는 모델(Model)을 연결합니다.
   *
   * @param gameView 사용자에게 보여질 게임 화면(View) 객체
   * @param model 게임 진행 상태 및 로직을 관리하는 GameManager 객체
   */
  public GameScreenController(GamePlayView gameView, GameManager model) {
    this.gameView = gameView;
    this.gameManager = model;

    initController();
  }

  /**
   * 컨트롤러를 초기화 시켜주는 메서드 입니다.
   * view에서 어떤 버튼을 눌르면 어떤 동작을 해야하는지 설정해 두었습니다.
   */
  private void initController() {
    // 랜덤 윷 던지기 리스너 설정
    gameView.setThrowButtonListener(new GamePlayView.ThrowButtonListener() {
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
    gameView.setTakeOutButtonListener(new GamePlayView.TakeOutButtonListener() {
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

  /**
   * 랜덤 윷을 굴리는 메서드 입니다.
   * @return 랜덤하게 굴린 윷 결과를 반환해주는 메서드 입니다.
   */
  public YutResult RandomYutThrow() {
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

    // 윷 결과 리스트 업데이트
    updateYutResultsInView();

    return yutResult;
  }

  /**
   * 지정된 윷을 굴리는 메서드입니다.
   * @param result view 에서 고른 문자열 형태의 윷 결과
   * @return 처리된 지정된 윷 결과를 반환해줍니다.
   */
  public YutResult FixedYutThrow(String result) {
    // 지정된 윷 결과 얻기 (enum 값으로 변환)
    YutResult yutResult = gameManager.throwFixedYut(result);

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

  /**
   * 윷 던진 후 말을 선택하기 이전에 게임 상태 업데이트하는 메서드
   * @param yutResult 굴렸던 윷 결과
   */
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

  /**
   * 윷 결과 리스트를 view에 표시해 주는 메서드
   * MODEL에서 현재 가지고 있는 윷 결과 리스트를 가져옵니다.
   */
  private void updateYutResultsInView() {
    // GameManager에서 현재 가지고 있는 윷 결과 리스트 가져오기
    gameView.displayYutResultList(gameManager.getYutResults());
  }

  /**
   * 모델로 부터 윷 결과 리스트를 가져오는 메서드
   * @return 윷 결과 리스트를 반환해 줍니다.
   */
  public List<YutResult> getYutResults() {
    return gameManager.getYutResults();
  }

  /**
   * 말을 선택 했을 때 모델로 부터 갈 수 있는 지점들을 받아오는 메서드
   * @param selectedPiece 고른 말의 번호
   * @return 움직일 수 있는 좌표(<이동 가능한 지점, 그 지점으로 가기 위해 필요한 칸 수>)들을 반환해줍니다.
   */
  public Map<Integer, Integer> PieceSelect(int selectedPiece) {

    gameManager.setSelectedPiece(selectedPiece);
    Map<Integer, Integer> movable = gameManager.findMovableCells(gameManager.getselectedsellid());

    return movable;
  }

  /**
   * 사용자가 말이 위치한 좌표(cellId)를 선택했을 때 호출되는 메서드입니다.
   * 선택된 좌표에 따라 이동할 수 있는 윷 결과를 제거하고, 말을 이동시키며,
   * 캡처(상대 말을 잡는 경우), 승리 여부, 턴 전환 등 게임의 상태를 갱신합니다.
   * 추가적으로 -1일 경우, 도착 지점(goal)에 도달 가능한 말 중에서 유저가 이동할 말을 선택하게 됩니다.
   *
   * @param cellId 선택된 말의 위치를 나타내는 셀 ID.
   */
  public void selectCoordinate(int cellId) {
    // 골라서 넘겨준 해시맵을 리스트에서 제거해야함. + 잡혔을 때 extraturn이 바뀌어 있는지 확인해야함.
    int value;
    if (cellId == -1) {
      ArrayList<YutResult> goalList = gameManager.getGoalPossibleYutList();
      if (goalList.size() >= 2) {
        YutResult[] yutArray = goalList.toArray(new YutResult[0]);
        String selectedYut = gameView.showYutSelectPanel(yutArray);
        // 읽어온 윷을 다시 변환하는 과정
        YutResult selectedEnum = YutResult.valueOf(selectedYut);
        value = selectedEnum.getMove();
      } else {
        value = gameManager.getMovableMap().get(cellId);
      }
    } else {
      value = gameManager.getMovableMap().get(cellId);
    }

    // 현재 extraTurn 상태 저장 (말을 잡았는지 확인용)
    boolean wasExtraTurn = gameManager.getExtraTurn();

    // 윷리스트에서 선택했던 값 삭제
    gameManager.removeYutResult(value);
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
    System.out.println("-------------------" + captureOccurred);
    System.out.println("-------------------" + wasExtraTurn);
    System.out.println("-------------------" + captureOccurred);
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

    if (gameManager.getExtraTurn()) {
      // 윷버튼 활성화
      gameView.setThrowButtonEnabled(true);
    } else {
      if (gameManager.isYutResultsEmpty()) {
        // 현재 턴 플레이어 확인 및 업데이트
        int currentPlayer = gameManager.checkPlayer();
        gameView.updateCurrentPlayer(currentPlayer);

        // 윷버튼 활성화
        gameView.setThrowButtonEnabled(true);
      } else {
        // 말선택 페이즈로 이동
        gameView.enableWaitingPieceSelection();
      }
    }
  }

  /**
   * 게임을 재시작할 때 호출되는 메서드입니다.
   * 게임 매니저와 뷰를 초기화하고, 현재 플레이어, 윷 결과, 이미지, 버튼 상태,
   * 안내 메시지 등을 모두 초기 상태로 되돌립니다.
   */
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

  /**
   * 새 게임을 시작하기 위한 초기 설정을 수행합니다.
   * 현재 게임 화면을 닫고, 설정 화면과 새로운 게임 매니저 및 컨트롤러를 생성합니다.
   */
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

  /**
   * 게임 보드에 있는 모든 말의 현재 위치 정보를 가져옵니다.
   * 각 말의 위치는 PositionDTO 객체로 표현되며,
   * 반환된 리스트를 통해 말의 상태를 확인할 수 있습니다.
   *
   * @return 말들의 위치 정보를 담은 PositionDTO 리스트
   */
  public List<PositionDTO> takeOutPiece() {
    return gameManager.getAllPiecePos();
  }

  /**
   * 게임 종료 하는 메서드입니다.
   */
  private void exitGame() {
    System.exit(0);
  }
}