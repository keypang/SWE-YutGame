package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.BoardType;

public class JavaFxConfigScreen extends Application {

  private ComboBox<Integer> playerCountCombo;
  private ComboBox<Integer> pieceCountCombo;
  private ComboBox<BoardType> boardTypeCombo;
  private Button startButton;

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("윷놀이 게임 설정");

    // 메인 컨테이너 생성
    VBox mainContainer = new VBox(20);
    mainContainer.setPadding(new Insets(20));
    mainContainer.setStyle("-fx-background-color: #fff5e6;");

    // 제목 라벨
    Label titleLabel = new Label("윷놀이 게임 설정");
    titleLabel.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");

    // 설정 그리드 생성
    GridPane settingsGrid = createSettingsGrid();

    // 게임 설명 패널
    VBox descriptionBox = createDescriptionBox();

    // 시작 버튼
    startButton = new Button("게임 시작");
    startButton.setPrefSize(150, 40);
    startButton.setStyle(
        "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14pt;");

    // 컨테이너에 요소 추가
    mainContainer.getChildren().addAll(titleLabel, settingsGrid, descriptionBox, startButton);
    mainContainer.setAlignment(Pos.CENTER);

    // 씬 설정 및 표시
    Scene scene = new Scene(mainContainer, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  private GridPane createSettingsGrid() {
    GridPane grid = new GridPane();
    grid.setHgap(40);
    grid.setVgap(15);
    grid.setPadding(new Insets(20));
    grid.setAlignment(Pos.CENTER);

    // 인원 수 설정
    Label playerLabel = new Label("인원 수");
    playerLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold;");
    playerCountCombo = new ComboBox<>();
    playerCountCombo.getItems().addAll(2, 3, 4);
    playerCountCombo.setValue(4); // 기본값 설정
    playerCountCombo.setPrefWidth(100);

    // 말 개수 설정
    Label pieceLabel = new Label("말 개수");
    pieceLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold;");
    pieceCountCombo = new ComboBox<>();
    pieceCountCombo.getItems().addAll(2, 3, 4, 5);
    pieceCountCombo.setValue(3); // 기본값 설정
    pieceCountCombo.setPrefWidth(100);

    // 판 종류 설정
    Label boardLabel = new Label("판 종류");
    boardLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold;");
    boardTypeCombo = new ComboBox<>();
    boardTypeCombo.getItems().addAll(BoardType.values());
    boardTypeCombo.setValue(BoardType.SQUARE); // 기본값 설정
    boardTypeCombo.setPrefWidth(150);

    // 그리드에 요소 배치
    grid.add(playerLabel, 0, 0);
    grid.add(playerCountCombo, 0, 1);
    grid.add(pieceLabel, 1, 0);
    grid.add(pieceCountCombo, 1, 1);
    grid.add(boardLabel, 2, 0);
    grid.add(boardTypeCombo, 2, 1);

    return grid;
  }

  private VBox createDescriptionBox() {
    VBox box = new VBox(10);
    box.setPadding(new Insets(15));
    box.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ccc; -fx-border-radius: 5;");

    Label title = new Label("게임 설명");
    title.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;");

    TextArea description = new TextArea(
        "윷놀이는 한국의 전통 보드게임입니다. 윷을 던져 나온 결과에 따라 말을 이동시키며, " +
            "모든 말이 완주하면 승리합니다. 상대방의 말을 잡으면 추가 턴을 얻게 됩니다.\n\n" +
            "게임을 시작하기 전에 인원 수, 말 개수, 판 종류를 선택해 주세요."
    );
    description.setWrapText(true);
    description.setEditable(false);
    description.setPrefHeight(100);
    description.setStyle("-fx-control-inner-background: #fafafa;");

    box.getChildren().addAll(title, description);
    return box;
  }

  public static void main(String[] args) {
    launch(args);
  }
}