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

  /**
   * 게임 시작 화면 컨트롤러를 생성합니다.
   *
   * @param view 게임 설정 화면 뷰
   * @param gameManager 게임 진행 로직을 관리하는 모델
   */
  public StartScreenController(GameConfigView view, GameManager gameManager) {
    this.view = view;
    this.gameManger = gameManager;

    initController();
  }

  /**
   * 게임 설정 화면의 컨트롤러 초기화 메서드입니다.
   * "시작" 버튼이 클릭되었을 때 {@link #process()} 메서드를 호출하도록 리스너를 등록합니다.
   * 이를 통해 사용자 입력 처리 로직이 실행됩니다.
   */
  private void initController() {

    view.setStartButtonListener(new GameConfigView.StartButtonListener() {
      @Override
      public void onStartButtonClicked() {
        process();
      }
    });
  }

  /**
   * 게임 시작을 위한 사용자 입력을 처리하는 메서드입니다.
   * 플레이어 수, 말 개수, 보드 타입을 입력받아 유효성을 검사하고,
   * 올바른 경우 게임을 초기화하고 시작합니다.
   * 입력이 유효하지 않으면 에러 메시지를 출력합니다.
   */
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

  /**
   * 게임 매니저에 설정 정보가 올바르게 저장되었는지 확인하기 위한 테스트 메서드입니다.
   */
  private void check() {
    gameManger.test();
  }

  /**
   * 게임을 시작하는 메서드입니다.
   * 설정 화면을 닫고, 사용된 설정 화면의 타입(Swing 또는 JavaFX)에 따라 해당하는 게임 화면을 실행합니다.
   */
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

  /**
   * JavaFX 게임 화면을 초기화하고 실행합니다.
   * 게임 설정 정보를 기반으로 화면을 구성하며, 컨트롤러를 연결합니다.
   */
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

  /**
   * Swing 기반 게임 화면을 초기화하고 실행합니다.
   * 설정 정보를 바탕으로 Swing 게임 화면을 생성하고, GameScreenController를 통해 뷰와 모델을 연결합니다.
   */
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

  // setter
  public void setGameManger(GameManager gameManger) {
    this.gameManger = gameManger;
  }
}