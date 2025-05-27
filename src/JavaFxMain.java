import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import view.javafx.JavaFxConfigScreen;
import controller.StartScreenController;
import model.GameManager;

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
      // 게임 설정 화면을 컨트롤러와 함께 실행
      openConfigScreenWithController(primaryStage);
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

  private void openConfigScreenWithController(Stage primaryStage) {
    try {
      // 게임 매니저 생성
      GameManager gameManager = new GameManager();

      // JavaFX 설정 화면 생성
      JavaFxConfigScreen configScreen = new JavaFxConfigScreen();

      // 컨트롤러 생성 및 연결
      StartScreenController startController = new StartScreenController(configScreen, gameManager);

      // 설정 화면 시작
      Stage configStage = new Stage();
      configScreen.start(configStage);

      // 메인 창 닫기
      primaryStage.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}