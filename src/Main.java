import controller.StartScreenController;
import model.GameManager;
import view.GameView;
import view.SwingView;

public class Main {
    public static void main(String[] args) {

        GameManager gameManager = new GameManager();
        GameView view = new SwingView();
        StartScreenController controller = new StartScreenController(view, gameManager);

    }
}