import controller.StartScreenController;
import model.*;
import view.global.GameConfigView;
import view.swing.SwingConfigScreen;

/**
 * Swing 기반 윷놀이 게임의 진입점 클래스임. GameManager와 SwingConfigScreen을 초기화하고, StartScreenController에 연결하여 게임
 * 설정 화면을 실행함.
 */
public class Main {

  /**
   * Swing 기반 프로그램의 진입점으로 게임 매니저 및 설정 화면을 초기화하고,
   * 시작 컨트롤러를 통해 전체 게임 흐름을 관리한다.
   *
   * @param args 커맨드라인 인자
   */
  public static void main(String[] args) {
    GameManager gameManager = new GameManager();
    GameConfigView view = new SwingConfigScreen();
    StartScreenController startController = new StartScreenController(view, gameManager);

    // GameScreenController를 StartScreenController에서 접근할 수 있도록 제공
    startController.setGameManger(gameManager);
  }
}