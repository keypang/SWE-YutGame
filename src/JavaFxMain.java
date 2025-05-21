
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class JavaFxMain extends Application {

  public static void main(String[] args) {
    launch(args); // JavaFX 앱 시작
  }

  @Override
  public void start(Stage primaryStage) {
    Label label = new Label("Hello, JavaFX!");
    StackPane root = new StackPane(label);
    Scene scene = new Scene(root, 400, 300);

    primaryStage.setTitle("JavaFX Basic Window");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}

