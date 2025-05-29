package view.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.BoardType;
import view.global.GameConfigView;

/**
 * JavaFx 기반의 게임 설정 화면을 구현한 클래스임. 사용자는 UI를 통하여 플레이어 수, 말 개수, 판 종류를 선택할 수 있으며, 게임 시작 버튼을
 * 클릭하여 사용자가 선택한 정보를 컨트롤러에 전달하는 방식임. GameConfigView 인터페이스를 구현하여 컨트롤러와 연결되도록 함.
 */

public class JavaFxConfigScreen extends Application implements GameConfigView {

  /**
   * 게임 설정을 선택하는 콤보박스 (플레이어 수, 말 개수, 판 종류)
   */
  private ComboBox<Integer> playerCountCombo;
  private ComboBox<Integer> pieceCountCombo;
  private ComboBox<BoardType> boardTypeCombo;

  /**
   * 게임 시작 버튼
   */
  private Button startButton;

  /**
   * 현재 JavaFx Stage로 해당 화면 자체를 나타냅니다.
   */
  private Stage primaryStage;

  /**
   * 시작 버튼 클릭 이벤트 리스너 (컨트롤러와 연결)
   */
  private StartButtonListener startButtonListener;

  /**
   * JavaFx UI의 시작 지점
   * 설정 화면 UI를 구성하고 표시합니다.
   *
   * @param primaryStage 현재 스테이지
   */
  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
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

    startButton.setOnAction(e -> {
      // 컨트롤러의 리스너 호출
      if (startButtonListener != null) {
        startButtonListener.onStartButtonClicked();
      }
    });

    // 컨테이너에 요소 추가
    mainContainer.getChildren().addAll(titleLabel, settingsGrid, descriptionBox, startButton);
    mainContainer.setAlignment(Pos.CENTER);

    // 씬 설정 및 표시
    Scene scene = new Scene(mainContainer, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  /**
   * 게임 설정을 위헌 GridPane을 구성하고 생성합니다.
   *
   * @return 구성된 GridPane(패널)
   */
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

  /**
   * 게임 설명을 보여주는 VBox를 생성합니다.
   *
   * @return 게임 설명 VBox
   */
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

  /**
   * GameConfigView 인터페이스를 구현(implement)함.
   */

  /**
   * 사용자가 선택한 플레이어 수를 반환합니다.
   *
   * @return 선택된 플레이어 수
   */
  @Override
  public int getSelectedPlayerCount() {
    return playerCountCombo.getValue();
  }

  /**
   * 사용자가 선택한 말 개수를 반환합니다.
   *
   * @return 선택된 말 개수
   */
  @Override
  public int getSelectedPieceCount() {
    return pieceCountCombo.getValue();
  }

  /**
   * 사용자가 선택한 판 종류를 반환합니다.
   *
   * @return 선택된 판 종류
   */
  @Override
  public BoardType getSelectedBoardType() {
    return boardTypeCombo.getValue();
  }

  /**
   * 시작 버튼 리스너를 생성(설정)합니다.
   *
   * @param listener 시작 버튼이 클릭될 때 통보받을 리스너
   */
  @Override
  public void setStartButtonListener(StartButtonListener listener) {
    this.startButtonListener = listener;
  }

  /**
   * 오류 메세지를 팝업으로 사용자에게 표시합니다.
   *
   * @param message 표시할 오류 메시지
   */
  @Override
  public void displayErrorMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("오류");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * 설정 화면을 닫습니다.
   */
  @Override
  public void close() {
    if (primaryStage != null) {
      primaryStage.close();
    }
  }

  /**
   * JavaFx UI 실행을 위한 메인(엔트리) 메서드입니다.
   *
   * @param args 명령줄 인자
   */
  public static void main(String[] args) {
    launch(args);
  }
}