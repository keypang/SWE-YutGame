package controller;

import javafx.stage.Stage;
import model.BoardType;
import model.GameManager;
import model.StartInfo;
import view.global.GameConfigView;
import view.global.GamePlayView;
import view.javafx.JavaFxPlayScreen;
import view.javafx.JavaFxConfigScreen;
import view.swing.SwingPlayScreen;
import view.swing.SwingConfigScreen;

public class StartScreenController {

  private GameConfigView view; // 게임 설정화면
  private GameManager gameManger;

  public StartScreenController(GameConfigView view, GameManager gameManager) {
    this.view = view;
    this.gameManger = gameManager;

    initController();
  }

  // 컨트롤러 초기화
  private void initController() {

    view.setStartButtonListener(new GameConfigView.StartButtonListener() {
      @Override
      public void onStartButtonClicked() {
        process();
      }
    });
  }

  private void process() {

    // 뷰에서 사용자 입력 따오기
    int playerCount = view.getSelectedPlayerCount();
    int pieceCount = view.getSelectedPieceCount();
    BoardType boardType = view.getSelectedBoardType();

    // 사용자 입력에 대해 유효성 검사 진행
    if (gameManger.validate(playerCount, pieceCount, boardType)) {

      StartInfo startInfo = new StartInfo(playerCount, pieceCount, boardType);
      gameManger.initGM(startInfo);

      // 모델에 정보 제대로 담겼는지 확인
      check();
      // 게임시작 추가
      startGame();
    } else {
      // 유효성 검사 실패한 경우  , 화면에 에러메시지 띄우기
      view.displayErrorMessage("제대로 입력해라.");
    }
  }

  // 모델에 정보 제대로 담겼는지 확인
  private void check() {
    gameManger.test();
  }

  public void startGame() {
    // 게임 설정 화면 닫기
    view.close();

    // 설정 화면의 타입에 따라 적절한 게임 화면 실행
    if (view instanceof JavaFxConfigScreen) {
      // JavaFX 설정 화면 → JavaFX 게임 화면
      startJavaFxGame();
    } else if (view instanceof SwingConfigScreen) {
      // Swing 설정 화면 → Swing 게임 화면
      startSwingGame();
    }
  }

  // JavaFX 게임 화면 시작
  private void startJavaFxGame() {
    try {
      System.out.println("JavaFX 게임 화면을 시작합니다.");

      // JavaFx 게임 화면 생성 및 시작
      JavaFxPlayScreen playScreen = new JavaFxPlayScreen(
          gameManger.getStartInfo().getPlayerCount(),
          gameManger.getStartInfo().getPieceCount(),
          gameManger.getStartInfo().getBoardType()
      );

      Stage playStage = new Stage();
      playScreen.start(playStage);

      // 게임화면 컨트롤러 생성 및 연결
      GameScreenController gameController = new GameScreenController(playScreen, gameManger);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Swing 게임 화면 시작
  private void startSwingGame() {
    try {
      System.out.println("Swing 게임 화면을 시작합니다.");

      // Swing 게임 화면 생성
      GamePlayView gameView = new SwingPlayScreen(
          gameManger.getStartInfo().getPlayerCount(),
          gameManger.getStartInfo().getPieceCount(),
          gameManger.getStartInfo().getBoardType()
      );

      // 게임화면 컨트롤러 생성 및 연결
      GameScreenController gameController = new GameScreenController(gameView, gameManger);

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Swing 게임 화면 실행 실패: " + e.getMessage());
    }
  }

  // getter
  public GameConfigView getView() {
    return view;
  }

  // getter
  public GameManager getModel() {
    return gameManger;
  }

  public void setGameManger(GameManager gameManger) {
    this.gameManger = gameManger;
  }
}