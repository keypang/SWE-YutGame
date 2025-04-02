import controller.StartScreenController;
import model.Model;
import view.GameView;
import view.SwingView;

public class Main {
    public static void main(String[] args) {

        Model model = new Model();
        GameView view = new SwingView();
        StartScreenController controller = new StartScreenController(view, model);

    }
}