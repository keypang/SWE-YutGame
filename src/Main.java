import controller.StartScreenController;
import model.*;
import view.global.GameConfigView;
import view.swing.SwingConfigScreen;

public class Main {

  public static void main(String[] args) {
    GameManager gameManager = new GameManager();
    GameConfigView view = new SwingConfigScreen();
    StartScreenController startController = new StartScreenController(view, gameManager);

    // GameScreenController를 StartScreenController에서 접근할 수 있도록 제공
    startController.setGameManger(gameManager);
  }
}