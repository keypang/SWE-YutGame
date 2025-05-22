import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import view.JavaFxConfigScreen;

public class JavaFxMain extends Application {

  public static void main(String[] args) {
    launch(args); // JavaFX 앱 시작
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("윷놀이 게임");

    // 시작 화면 구성
    VBox root = new VBox(20);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(30));
    root.setStyle("-fx-background-color: #fff5e6;");

    // 제목 라벨
    Label titleLabel = new Label("윷놀이 게임");
    titleLabel.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold;");

    // 게임 시작 버튼
    Button startButton = new Button("게임 설정 화면 열기");
    startButton.setPrefSize(200, 50);
    startButton.setStyle(
        "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14pt;");

    startButton.setOnAction(e -> {
      // 게임 설정 화면 실행
      openConfigScreen(primaryStage);
    });

    // 종료 버튼
    Button exitButton = new Button("종료");
    exitButton.setPrefSize(200, 50);
    exitButton.setStyle(
        "-fx-background-color: #c87878; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14pt;");

    exitButton.setOnAction(e -> {
      primaryStage.close();
    });

    root.getChildren().addAll(titleLabel, startButton, exitButton);

    Scene scene = new Scene(root, 400, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void openConfigScreen(Stage primaryStage) {
    try {
      // JavaFX 설정 화면 실행
      JavaFxConfigScreen configScreen = new JavaFxConfigScreen();
      configScreen.start(new Stage());
      primaryStage.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}