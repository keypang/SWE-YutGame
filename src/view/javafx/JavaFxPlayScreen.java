package view.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BoardType;
import model.YutResult;
import model.PositionDTO;
import view.global.GamePlayView;

import java.util.*;

public class JavaFxPlayScreen extends Application implements GamePlayView {

  private Stage parentStage;
  private int playerCount;
  private int pieceCount;
  private BoardType boardType;

  private ThrowButtonListener throwButtonListener;
  private FixedYutButtonListener fixedYutButtonListener;
  private PieceSelectionListener pieceSelectionListener;
  private CellSelectionListener cellSelectionListener;
  private GameEndListener gameEndListener;
  private TakeOutButtonListener takeOutButtonListener;

  private Button testRollButton;
  private Button rollButton;

  private ImageView turnArrowLabel;
  private ArrayList<Image> playerIcons = new ArrayList<>();
  private ArrayList<ImageView> pieceLabels = new ArrayList<>();
  private ArrayList<ImageView> playerArrows = new ArrayList<>();
  private Label currentPlayerLabel;
  private Label statusMessageLabel;
  private ImageView yutImageLabel;
  private Label yutResultLabel;
  private VBox yutResultsPanel;
  private List<Label> yutResultLabels = new ArrayList<>();
  private List<Circle> movablePoints = new ArrayList<>();
  private List<Label> stackCountLabels = new ArrayList<>();

  private Pane boardPane;

  private int currentPlayerIndex = 0;
  private int selectedPieceIndex = 0;
  private boolean waitingPieceSelection = false;
  private int isCorrectPlayer = 1;

  private Map<String, ImageView> playerPiecesMap = new HashMap<>();
  private Map<Integer, HBox> playerPiecePanels = new HashMap<>();

  private Map<Integer, Point2D> squarePositionMap = new HashMap<>();
  private Map<Integer, Point2D> pentagonPositionMap = new HashMap<>();
  private Map<Integer, Point2D> hexagonPositionMap = new HashMap<>();

  public JavaFxPlayScreen(int playerCount, int pieceCount, BoardType boardType) {
    this.playerCount = playerCount;
    this.pieceCount = pieceCount;
    this.boardType = boardType;
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("게임 화면");
    parentStage = primaryStage;

    BorderPane mainPane = new BorderPane();
    mainPane.setStyle("-fx-background-color: #FFF5E6;");

    boardPane = createBoardPane();
    mainPane.setLeft(boardPane);

    VBox controlPane = createControlPane();
    mainPane.setCenter(controlPane);

    initializePositions();

    Scene scene = new Scene(mainPane, 1280, 800);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  private Pane createBoardPane() {
    Pane pane = new Pane();
    pane.setPrefSize(700, 720);
    pane.setStyle("-fx-background-color: #fff5e6;");

    // 윷놀이 판 이미지 추가 (클래스패스 리소스 로드)
    Image boardImage = new Image(getClass().getResource("/view/images/" + boardType.name() + ".png").toExternalForm());
    ImageView boardView = new ImageView(boardImage);

    if (boardType == BoardType.SQUARE) {
      boardView.setFitWidth(600);
      boardView.setFitHeight(600);
      boardView.setLayoutX(40);
      boardView.setLayoutY(80);
    } else if (boardType == BoardType.PENTAGON) {
      boardView.setFitWidth(700);
      boardView.setFitHeight(700);
      boardView.setLayoutX(1);
      boardView.setLayoutY(30);
    } else {
      boardView.setFitWidth(800);
      boardView.setFitHeight(800);
      boardView.setLayoutX(-50);
      boardView.setLayoutY(0);
    }

    pane.getChildren().add(boardView);

    // 완주 셀 추가 (클래스패스 리소스 로드)
    Image goalImage = new Image(getClass().getResource("/view/images/finishCell.png").toExternalForm());
    ImageView goalView = new ImageView(goalImage);

    if (boardType == BoardType.SQUARE) {
      goalView.setFitWidth(43);
      goalView.setFitHeight(43);
      goalView.setLayoutX(647);
      goalView.setLayoutY(602);
    } else if (boardType == BoardType.PENTAGON) {
      goalView.setFitWidth(43);
      goalView.setFitHeight(43);
      goalView.setLayoutX(601);
      goalView.setLayoutY(655);
    } else {
      goalView.setFitWidth(43);
      goalView.setFitHeight(43);
      goalView.setLayoutX(640);
      goalView.setLayoutY(623);
    }

    pane.getChildren().add(goalView);
    return pane;
  }

  private VBox createControlPane() {
    VBox pane = new VBox(5);
    pane.setPadding(new Insets(20));
    pane.setStyle("-fx-background-color: #fff5e6;");

    VBox innerBox = new VBox(15);
    innerBox.setAlignment(Pos.TOP_CENTER);

    VBox playerInfoBox = createPlayerInfoPane();
    TitledPane playerInfoTitledPane = new TitledPane("플레이어 정보", playerInfoBox);
    playerInfoTitledPane.setCollapsible(false);

    VBox currentPlayerBox = new VBox(10);
    currentPlayerBox.setPadding(new Insets(10));
    currentPlayerBox.setStyle(
        "-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

    currentPlayerLabel = new Label("현재 플레이어: 1");
    currentPlayerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    statusMessageLabel = new Label("게임 시작! 윷을 던져주세요.");
    statusMessageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: darkgreen;");

    currentPlayerBox.getChildren().addAll(currentPlayerLabel, statusMessageLabel);
    TitledPane statusTitledPane = new TitledPane("게임 상태", currentPlayerBox);
    statusTitledPane.setCollapsible(false);

    HBox resultListPane = createYutResultPane();
    TitledPane resultListTitledPane = new TitledPane("윷 결과", resultListPane);
    resultListTitledPane.setCollapsible(false);

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);

    testRollButton = new Button("지정윷 던지기");
    testRollButton.setPrefSize(150, 60);
    testRollButton.setStyle(
        "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
    testRollButton.setOnAction(e -> {
      setThrowButtonEnabled(false);
      displayResultSelect();
    });

    rollButton = new Button("윷 던지기");
    rollButton.setPrefSize(150, 60);
    rollButton.setStyle(
        "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
    rollButton.setOnAction(e -> {
      if (throwButtonListener != null) {
        YutResult yutResult = throwButtonListener.onThrowButtonClicked();
        displaySingleYutResult(yutResult);
      }
    });

    buttonBox.getChildren().addAll(testRollButton, rollButton);

    Button testEndGameButton = new Button("게임 종료 테스트");
    testEndGameButton.setPrefSize(200, 40);
    testEndGameButton.setStyle(
        "-fx-background-color: #ff6464; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
    testEndGameButton.setOnAction(e -> {
      showGameEndDialog(currentPlayerIndex + 1);
    });

    pane.getChildren()
        .addAll(playerInfoTitledPane, statusTitledPane, resultListTitledPane, buttonBox,
            testEndGameButton);

    return pane;
  }

  private VBox createPlayerInfoPane() {
    VBox pane = new VBox(4);
    pane.setStyle(
        "-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

    // 턴 화살표 이미지 로드 (클래스패스 리소스)
    Image turnArrowImage = new Image(getClass().getResource("/view/images/turnArrow.png").toExternalForm());

    for (int i = 0; i < playerCount; i++) {
      int playerId = i + 1;

      VBox playerBox = new VBox(5);
      playerBox.setStyle("-fx-background-color: #fff5e6;");

      HBox infoRow = new HBox(10);
      infoRow.setAlignment(Pos.CENTER_LEFT);
      infoRow.setPadding(new Insets(5));
      infoRow.setStyle("-fx-background-color: #fff5e6;");

      ImageView arrow = new ImageView();
      arrow.setFitHeight(30);
      arrow.setFitWidth(30);

      if (i == 0) {
        arrow.setImage(turnArrowImage);
        turnArrowLabel = arrow;
      }

      playerArrows.add(arrow);

      Label nameLabel = new Label("PLAYER" + playerId);
      nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

      // 플레이어 말 아이콘 로드 및 크기 조정 (클래스패스 리소스)
      Image pieceImage = new Image(getClass().getResource("/view/images/플레이어" + playerId + "말.png").toExternalForm());
      ImageView pieceImageView = new ImageView(pieceImage);
      pieceImageView.setFitHeight(40);
      pieceImageView.setFitWidth(40);
      playerIcons.add(pieceImage);
      pieceLabels.add(pieceImageView);

      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);

      infoRow.getChildren().addAll(arrow, nameLabel, spacer, pieceImageView);

      HBox piecesRow = new HBox(5);
      piecesRow.setAlignment(Pos.CENTER_LEFT);
      piecesRow.setStyle("-fx-background-color: #fff5e6;");

      for (int j = 0; j < pieceCount; j++) {
        int pieceId = j;

        ImageView pieceView = new ImageView(pieceImage);
        pieceView.setId("piece_" + playerId + "_" + pieceId);
        pieceView.setStyle("-fx-padding: 2 2 2 2;");
        pieceView.setFitHeight(40);
        pieceView.setFitWidth(40);
        pieceView.setCursor(Cursor.HAND);

        pieceView.setOnMouseClicked(e -> {
          if (!waitingPieceSelection) {
            showAlert("먼저 윷을 던져주세요!");
            return;
          }
          if (playerId != currentPlayerIndex + 1) {
            showAlert(playerId + "번 플레이어의 말이 아닙니다!");
            return;
          }
          waitingPieceSelection = false;
          onPieceSelected(pieceId);
        });

        piecesRow.getChildren().add(pieceView);
        playerPiecesMap.put(playerId + "_" + pieceId, pieceView);
      }

      playerPiecePanels.put(playerId, piecesRow);

      playerBox.getChildren().addAll(infoRow, piecesRow);
      pane.getChildren().add(playerBox);
    }

    return pane;
  }

  private HBox createYutResultPane() {
    HBox pane = new HBox(20);
    pane.setPadding(new Insets(10));
    pane.setStyle(
        "-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

    VBox currentResultBox = new VBox(10);
    currentResultBox.setPadding(new Insets(5));
    currentResultBox.setAlignment(Pos.TOP_LEFT);
    currentResultBox.setPrefSize(120, 120);
    currentResultBox.setStyle("-fx-background-color: #fff5e6;");

    yutResultLabel = new Label("윷 결과: ");
    yutResultLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

    yutImageLabel = new ImageView();
    yutImageLabel.setFitHeight(40);
    yutImageLabel.setFitWidth(100);

    currentResultBox.getChildren().addAll(yutResultLabel, yutImageLabel);

    VBox resultsListBox = new VBox();
    resultsListBox.setPrefSize(150, 120);
    resultsListBox.setMaxWidth(Double.MAX_VALUE);
    resultsListBox.setStyle(
        "-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

    yutResultsPanel = new VBox(5);
    yutResultsPanel.setStyle("-fx-background-color: #fff5e6; -fx-padding: 5;");

    ScrollPane scrollPane = new ScrollPane(yutResultsPanel);
    scrollPane.setFitToWidth(true);
    scrollPane.setStyle("-fx-background-color: transparent;");

    resultsListBox.getChildren().add(scrollPane);

    TitledPane yutListTitled = new TitledPane("가능한 이동", resultsListBox);
    yutListTitled.setCollapsible(false);

    HBox.setHgrow(yutListTitled, Priority.ALWAYS);
    yutListTitled.setMaxWidth(Double.MAX_VALUE);

    pane.getChildren().addAll(currentResultBox, yutListTitled);
    return pane;
  }

  private void onPieceSelected(int pieceId) {
    Map<Integer, Integer> availableCells = null;
    if (pieceSelectionListener != null) {
      selectedPieceIndex = pieceId;
      availableCells = pieceSelectionListener.onPieceSelected(pieceId);

      ImageView selectedPiece = playerPiecesMap.get((currentPlayerIndex + 1) + "_" + pieceId);
      if (selectedPiece != null) {
        selectedPiece.setStyle("-fx-effect: dropshadow(gaussian, blue, 10, 0.5, 0, 0);");
      }
    }
    repaintAllPieces();
    showMovablePoints(availableCells);
  }

  public void showMovablePoints(Map<Integer, Integer> availableCells) {
    for (Circle point : movablePoints) {
      boardPane.getChildren().remove(point);
    }
    movablePoints.clear();

    if (availableCells == null || availableCells.isEmpty()) {
      showAlert("이동 가능한 위치가 없습니다.");
      return;
    }

    for (Map.Entry<Integer, Integer> entry : availableCells.entrySet()) {
      int cellId = entry.getKey();
      Point2D target = getPositionByBoardType(cellId);

      if (target == null) {
        System.out.println("알 수 없는 셀 ID: " + cellId);
        continue;
      }

      Circle movablePoint = new Circle();
      movablePoint.setRadius(15);
      movablePoint.setFill(Color.LIGHTGREEN);
      movablePoint.setStroke(Color.DARKGREEN);
      movablePoint.setStrokeWidth(2);
      movablePoint.setCursor(Cursor.HAND);
      movablePoint.setOpacity(0.8);
      movablePoint.setCenterX(target.getX());
      movablePoint.setCenterY(target.getY());

      movablePoints.add(movablePoint);

      final int finalCellId = cellId;
      movablePoint.setOnMouseClicked(e -> {
        System.out.println("말 옮기기 전 전달될 cell ID :" + finalCellId);
        if (cellSelectionListener != null) {
          cellSelectionListener.onCellSelected(finalCellId);
        }

        for (Circle targetPoint : movablePoints) {
          boardPane.getChildren().remove(targetPoint);
        }
        movablePoints.clear();
      });

      movablePoint.setOnMouseEntered(e -> {
        movablePoint.setRadius(18);
        movablePoint.setFill(Color.LIME);
      });

      movablePoint.setOnMouseExited(e -> {
        movablePoint.setRadius(15);
        movablePoint.setFill(Color.LIGHTGREEN);
      });

      boardPane.getChildren().add(movablePoint);
    }
  }

  private void displayResultSelect() {
    String[] yutResult = {"백도", "도", "개", "걸", "윷", "모"};

    Stage dialog = new Stage();
    dialog.initOwner(parentStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("지정윷 선택");

    HBox buttonContainer = new HBox(20);
    buttonContainer.setAlignment(Pos.CENTER);
    buttonContainer.setPadding(new Insets(30));
    buttonContainer.setStyle("-fx-background-color: #fff5e6;");

    for (String result : yutResult) {
      Button button = new Button(result);
      button.setStyle(
          "-fx-font-size: 18px; -fx-background-color: #78c878; -fx-text-fill: white; -fx-font-weight: bold;");
      button.setPrefSize(100, 70);

      button.setOnAction(e -> {
        dialog.close();
        if (fixedYutButtonListener != null) {
          YutResult yutResultEnum = fixedYutButtonListener.onFixedYutSelected(result);
          displaySingleYutResult(yutResultEnum);
        }
      });

      buttonContainer.getChildren().add(button);
    }

    Scene scene = new Scene(buttonContainer, 800, 200);
    dialog.setScene(scene);
    dialog.centerOnScreen();
    dialog.showAndWait();
  }

  private void displaySingleYutResult(YutResult result) {
    String resultText = result.name();
    String imageName = getYutImageName(result.getMove());

    yutResultLabel.setText("윷 결과: " + resultText);

    if (imageName != null) {
      Image yutImage = new Image(getClass().getResource("/view/images/" + imageName + ".png").toExternalForm(), false);
      yutImageLabel.setImage(yutImage);

      Platform.runLater(() -> showYutResultPopup(imageName, resultText));
    }
  }

  private String getYutImageName(int move) {
    switch (move) {
      case -1:
        return "빽도";
      case 1:
        return "도";
      case 2:
        return "개";
      case 3:
        return "걸";
      case 4:
        return "윷";
      case 5:
        return "모";
      default:
        return null;
    }
  }

  private void showYutResultPopup(String imageName, String resultText) {
    Stage resultDialog = new Stage();
    resultDialog.initOwner(parentStage);
    resultDialog.initModality(Modality.APPLICATION_MODAL);
    resultDialog.setTitle("윷 결과: " + resultText);

    VBox container = new VBox(20);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(20));
    container.setStyle("-fx-background-color: #fff5e6;");

    Image popupImage = new Image(getClass().getResource("/view/images/" + imageName + ".png").toExternalForm(), false);
    ImageView imageView = new ImageView(popupImage);
    imageView.setFitWidth(300);
    imageView.setFitHeight(300);

    Button confirmButton = new Button("확인");
    confirmButton.setStyle(
        "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-weight: bold;");
    confirmButton.setOnAction(e -> resultDialog.close());

    container.getChildren().addAll(imageView, confirmButton);

    Scene scene = new Scene(container, 400, 450);
    resultDialog.setScene(scene);
    resultDialog.centerOnScreen();

    javafx.animation.Timeline timeline = new javafx.animation.Timeline(
        new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2), e -> resultDialog.close())
    );
    timeline.play();

    resultDialog.showAndWait();

    if (resultText.equals("윷") || resultText.equals("모")) {
      showAlert("윷을 한 번 더 던지세요!");
    } else {
      showAlert("이동할 말을 선택하세요!");
    }
  }

  private Point2D getPositionByBoardType(int cellId) {
    if (boardType == BoardType.SQUARE) {
      return squarePositionMap.get(cellId);
    } else if (boardType == BoardType.PENTAGON) {
      return pentagonPositionMap.get(cellId);
    } else if (boardType == BoardType.HEXAGON) {
      return hexagonPositionMap.get(cellId);
    } else {
      return null;
    }
  }

  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("알림");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void initializePositions() {
    if (boardType == BoardType.SQUARE) {
      initializeSquarePositions();
    } else if (boardType == BoardType.PENTAGON) {
      initializePentagonPositions();
    } else {
      initializeHexagonPositions();
    }
  }

  private void initializeSquarePositions() {
    squarePositionMap.put(-1, new Point2D(667 + 4, 602 + 20));
    squarePositionMap.put(0, new Point2D(568 + 13, 604 + 15));
    squarePositionMap.put(1, new Point2D(574 + 4, 494 + 20));
    squarePositionMap.put(2, new Point2D(574 + 4, 405 + 20));
    squarePositionMap.put(3, new Point2D(574 + 4, 320 + 20));
    squarePositionMap.put(4, new Point2D(574 + 4, 234 + 20));
    squarePositionMap.put(5, new Point2D(574 + 4, 131 + 10));
    squarePositionMap.put(6, new Point2D(465 + 4, 127 + 20));
    squarePositionMap.put(7, new Point2D(379 + 4, 127 + 20));
    squarePositionMap.put(8, new Point2D(293 + 4, 127 + 20));
    squarePositionMap.put(9, new Point2D(208 + 4, 127 + 20));
    squarePositionMap.put(10, new Point2D(91 + 4, 131 + 10));
    squarePositionMap.put(11, new Point2D(99 + 4, 234 + 20));
    squarePositionMap.put(12, new Point2D(99 + 4, 320 + 20));
    squarePositionMap.put(13, new Point2D(99 + 4, 405 + 20));
    squarePositionMap.put(14, new Point2D(99 + 4, 494 + 20));
    squarePositionMap.put(15, new Point2D(90 + 4, 604 + 10));
    squarePositionMap.put(16, new Point2D(208 + 4, 602 + 20));
    squarePositionMap.put(17, new Point2D(293 + 4, 602 + 20));
    squarePositionMap.put(18, new Point2D(379 + 4, 602 + 20));
    squarePositionMap.put(19, new Point2D(465 + 4, 602 + 20));
    squarePositionMap.put(20, new Point2D(568 + 13, 602 + 15));
    squarePositionMap.put(50, new Point2D(488 + 4, 212 + 20));
    squarePositionMap.put(55, new Point2D(423 + 4, 277 + 20));
    squarePositionMap.put(100, new Point2D(185 + 4, 213 + 20));
    squarePositionMap.put(110, new Point2D(250 + 4, 277 + 20));
    squarePositionMap.put(150, new Point2D(185 + 4, 515 + 20));
    squarePositionMap.put(165, new Point2D(250 + 4, 450 + 20));
    squarePositionMap.put(200, new Point2D(488 + 4, 516 + 20));
    squarePositionMap.put(220, new Point2D(423 + 4, 450 + 20));
    squarePositionMap.put(1000, new Point2D(330 + 11, 370 + 10));
  }

  private void initializePentagonPositions() {
    pentagonPositionMap.put(-1, new Point2D(622, 678));
    pentagonPositionMap.put(0, new Point2D(521, 665));
    pentagonPositionMap.put(1, new Point2D(557, 583));
    pentagonPositionMap.put(2, new Point2D(578, 522));
    pentagonPositionMap.put(3, new Point2D(595, 462));
    pentagonPositionMap.put(4, new Point2D(614, 402));
    pentagonPositionMap.put(5, new Point2D(624, 316));
    pentagonPositionMap.put(6, new Point2D(561, 254));
    pentagonPositionMap.put(7, new Point2D(515, 221));
    pentagonPositionMap.put(8, new Point2D(468, 190));
    pentagonPositionMap.put(9, new Point2D(421, 157));
    pentagonPositionMap.put(10, new Point2D(351, 120));
    pentagonPositionMap.put(11, new Point2D(279, 155));
    pentagonPositionMap.put(12, new Point2D(233, 189));
    pentagonPositionMap.put(13, new Point2D(186, 222));
    pentagonPositionMap.put(14, new Point2D(140, 254));
    pentagonPositionMap.put(15, new Point2D(80, 315));
    pentagonPositionMap.put(16, new Point2D(88, 402));
    pentagonPositionMap.put(17, new Point2D(107, 462));
    pentagonPositionMap.put(18, new Point2D(125, 522));
    pentagonPositionMap.put(19, new Point2D(144, 581));
    pentagonPositionMap.put(20, new Point2D(179, 663));
    pentagonPositionMap.put(21, new Point2D(265, 670));
    pentagonPositionMap.put(22, new Point2D(322, 669));
    pentagonPositionMap.put(23, new Point2D(381, 669));
    pentagonPositionMap.put(24, new Point2D(437, 669));
    pentagonPositionMap.put(25, new Point2D(520, 665));
    pentagonPositionMap.put(50, new Point2D(532, 346));
    pentagonPositionMap.put(55, new Point2D(445, 379));
    pentagonPositionMap.put(100, new Point2D(350, 223));
    pentagonPositionMap.put(110, new Point2D(351, 311));
    pentagonPositionMap.put(150, new Point2D(171, 348));
    pentagonPositionMap.put(165, new Point2D(257, 378));
    pentagonPositionMap.put(200, new Point2D(233, 580));
    pentagonPositionMap.put(220, new Point2D(293, 501));
    pentagonPositionMap.put(250, new Point2D(459, 580));
    pentagonPositionMap.put(275, new Point2D(406, 499));
    pentagonPositionMap.put(1000, new Point2D(351, 413));
  }

  private void initializeHexagonPositions() {
    hexagonPositionMap.put(-1, new Point2D(661, 645));
    hexagonPositionMap.put(0, new Point2D(613, 551));
    hexagonPositionMap.put(1, new Point2D(611, 480));
    hexagonPositionMap.put(2, new Point2D(612, 434));
    hexagonPositionMap.put(3, new Point2D(612, 386));
    hexagonPositionMap.put(4, new Point2D(612, 340));
    hexagonPositionMap.put(5, new Point2D(611, 266));
    hexagonPositionMap.put(6, new Point2D(551, 219));
    hexagonPositionMap.put(7, new Point2D(505, 193));
    hexagonPositionMap.put(8, new Point2D(466, 169));
    hexagonPositionMap.put(9, new Point2D(421, 145));
    hexagonPositionMap.put(10, new Point2D(350, 119));
    hexagonPositionMap.put(11, new Point2D(278, 147));
    hexagonPositionMap.put(12, new Point2D(236, 169));
    hexagonPositionMap.put(13, new Point2D(192, 193));
    hexagonPositionMap.put(14, new Point2D(148, 218));
    hexagonPositionMap.put(15, new Point2D(88, 265));
    hexagonPositionMap.put(16, new Point2D(88, 340));
    hexagonPositionMap.put(17, new Point2D(88, 386));
    hexagonPositionMap.put(18, new Point2D(88, 432));
    hexagonPositionMap.put(19, new Point2D(88, 479));
    hexagonPositionMap.put(20, new Point2D(87, 553));
    hexagonPositionMap.put(21, new Point2D(155, 585));
    hexagonPositionMap.put(22, new Point2D(196, 609));
    hexagonPositionMap.put(23, new Point2D(239, 632));
    hexagonPositionMap.put(24, new Point2D(282, 655));
    hexagonPositionMap.put(25, new Point2D(349, 689));
    hexagonPositionMap.put(26, new Point2D(421, 657));
    hexagonPositionMap.put(27, new Point2D(463, 633));
    hexagonPositionMap.put(28, new Point2D(505, 607));
    hexagonPositionMap.put(29, new Point2D(547, 585));
    hexagonPositionMap.put(30, new Point2D(613, 551));
    hexagonPositionMap.put(300, new Point2D(522, 505));
    hexagonPositionMap.put(330, new Point2D(449, 463));
    hexagonPositionMap.put(50, new Point2D(525, 314));
    hexagonPositionMap.put(55, new Point2D(448, 354));
    hexagonPositionMap.put(100, new Point2D(349, 217));
    hexagonPositionMap.put(110, new Point2D(349, 299));
    hexagonPositionMap.put(150, new Point2D(175, 316));
    hexagonPositionMap.put(165, new Point2D(251, 355));
    hexagonPositionMap.put(200, new Point2D(178, 500));
    hexagonPositionMap.put(220, new Point2D(254, 460));
    hexagonPositionMap.put(250, new Point2D(350, 600));
    hexagonPositionMap.put(275, new Point2D(350, 519));
    hexagonPositionMap.put(1000, new Point2D(349, 407));
  }

  @Override
  public void setThrowButtonListener(ThrowButtonListener throwButtonListener) {
    this.throwButtonListener = throwButtonListener;
  }

  @Override
  public void setFixedYutButtonListener(FixedYutButtonListener listener) {
    this.fixedYutButtonListener = listener;
  }

  @Override
  public void setPieceSelectionListener(PieceSelectionListener listener) {
    this.pieceSelectionListener = listener;
  }

  @Override
  public void setCellSelectionListener(CellSelectionListener listener) {
    this.cellSelectionListener = listener;
  }

  @Override
  public void setGameEndListener(GameEndListener listener) {
    this.gameEndListener = listener;
  }

  @Override
  public void setTakeOutButtonListener(TakeOutButtonListener takeOutButtonListener) {
    this.takeOutButtonListener = takeOutButtonListener;
  }

  @Override
  public void displayYutResultList(List<YutResult> results) {
    yutResultsPanel.getChildren().clear();
    yutResultLabels.clear();

    if (results.isEmpty()) {
      Label emptyLabel = new Label("던진 윷 없음");
      emptyLabel.setStyle("-fx-alignment: center;");
      yutResultsPanel.getChildren().add(emptyLabel);
      yutResultLabels.add(emptyLabel);
    } else {
      for (YutResult result : results) {
        String resultText = result.name() + " (" + result.getMove() + "칸)";
        Label resultLabel = new Label(resultText);
        resultLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5 10 5 10;");
        yutResultsPanel.getChildren().add(resultLabel);
        yutResultLabels.add(resultLabel);
      }
    }
  }

  @Override
  public String showYutSelectPanel(YutResult[] yutResult) {
    final String[] selectedResult = {null};

    Stage dialog = new Stage();
    dialog.initOwner(parentStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("소모할 윷을 선택하세요!");

    HBox buttonContainer = new HBox(20);
    buttonContainer.setAlignment(Pos.CENTER);
    buttonContainer.setPadding(new Insets(30));
    buttonContainer.setStyle("-fx-background-color: #fff5e6;");

    for (YutResult result : yutResult) {
      Button button = new Button(result.name());
      button.setStyle(
          "-fx-font-size: 18px; -fx-background-color: #78c878; -fx-text-fill: white; -fx-font-weight: bold;");
      button.setPrefSize(100, 70);

      button.setOnAction(e -> {
        selectedResult[0] = result.name();
        dialog.close();
      });

      buttonContainer.getChildren().add(button);
    }

    Scene scene = new Scene(buttonContainer, 800, 200);
    dialog.setScene(scene);
    dialog.centerOnScreen();
    dialog.showAndWait();

    return selectedResult[0];
  }

  @Override
  public void updateCurrentPlayer(int playerNumber) {
    currentPlayerLabel.setText("현재 플레이어: " + playerNumber);
    currentPlayerIndex = playerNumber - 1;

    Image turnArrowImage = new Image(getClass().getResource("/view/images/turnArrow.png").toExternalForm());

    for (int i = 0; i < playerArrows.size(); i++) {
      ImageView arrow = playerArrows.get(i);
      arrow.setImage(null);
    }

    if (playerNumber > 0 && playerNumber <= playerArrows.size()) {
      ImageView currentArrow = playerArrows.get(playerNumber - 1);
      currentArrow.setImage(turnArrowImage);
    }

    if (isCorrectPlayer == playerNumber) {
      if (isCorrectPlayer > 4) {
        isCorrectPlayer = 1;
      }
      isCorrectPlayer++;
      showAlert(playerNumber + "번 플레이어의 턴입니다!");
    }
  }

  @Override
  public void setThrowButtonEnabled(boolean enabled) {
    rollButton.setDisable(!enabled);
    testRollButton.setDisable(!enabled);

    if (enabled) {
      rollButton.setStyle(
          "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
      testRollButton.setStyle(
          "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
    } else {
      rollButton.setStyle(
          "-fx-background-color: #c8c8c8; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
      testRollButton.setStyle(
          "-fx-background-color: #c8c8c8; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
    }
  }

  @Override
  public void enableWaitingPieceSelection() {
    this.waitingPieceSelection = true;
  }

  @Override
  public void disablePieceSelection() {
    showAlert("먼저 윷을 던져주세요!");
    clearPieceSelection();
    this.waitingPieceSelection = false;
  }

  @Override
  public void setStatusMessage(String message) {
    statusMessageLabel.setText(message);
  }

  @Override
  public void repaintAllPieces() {
    if (takeOutButtonListener == null) {
      return;
    }

    List<PositionDTO> currentPositions = takeOutButtonListener.onTakeOutButtonClicked();

    for (Map.Entry<String, ImageView> entry : playerPiecesMap.entrySet()) {
      ImageView pieceView = entry.getValue();
      if (boardPane.getChildren().contains(pieceView)) {
        boardPane.getChildren().remove(pieceView);
      }
    }

    for (Label label : stackCountLabels) {
      if (boardPane.getChildren().contains(label)) {
        boardPane.getChildren().remove(label);
      }
    }
    stackCountLabels.clear();

    for (PositionDTO dto : currentPositions) {
      if (dto.getCellId() == -1) {
        int playerId = dto.getPlayerId();
        int pieceId = dto.getPieceId();

        ImageView pieceView = playerPiecesMap.get(playerId + "_" + pieceId);
        if (pieceView != null) {
          if (boardPane.getChildren().contains(pieceView)) {
            boardPane.getChildren().remove(pieceView);
          }

          HBox currentPiecePanel = playerPiecePanels.get(playerId);
          if (currentPiecePanel != null && !currentPiecePanel.getChildren().contains(pieceView)) {
            currentPiecePanel.getChildren().add(pieceView);
          }
        }
      }
    }

    Map<Integer, List<PositionDTO>> cellPieces = new HashMap<>();
    for (PositionDTO dto : currentPositions) {
      int cellId = dto.getCellId();
      boolean shouldDisplay = false;
      if (boardType == BoardType.SQUARE && cellId != -1 && cellId != 21) {
        shouldDisplay = true;
      } else if (boardType == BoardType.PENTAGON && cellId != -1 && cellId != 26) {
        shouldDisplay = true;
      } else if (boardType == BoardType.HEXAGON && cellId != -1 && cellId != 31) {
        shouldDisplay = true;
      }

      if (shouldDisplay) {
        if (!cellPieces.containsKey(cellId)) {
          cellPieces.put(cellId, new ArrayList<>());
        }
        cellPieces.get(cellId).add(dto);
      }
    }

    for (Map.Entry<Integer, List<PositionDTO>> entry : cellPieces.entrySet()) {
      int cellId = entry.getKey();
      List<PositionDTO> piecesAtCell = entry.getValue();
      Point2D pos = getPositionByBoardType(cellId);

      if (pos == null) {
        continue;
      }

      if (piecesAtCell.size() == 1) {
        PositionDTO dto = piecesAtCell.get(0);
        ImageView pieceView = playerPiecesMap.get(dto.getPlayerId() + "_" + dto.getPieceId());

        if (pieceView != null) {
          HBox parentPanel = playerPiecePanels.get(dto.getPlayerId());
          if (parentPanel != null && parentPanel.getChildren().contains(pieceView)) {
            parentPanel.getChildren().remove(pieceView);
          }

          pieceView.setLayoutX(pos.getX() - 20);
          pieceView.setLayoutY(pos.getY() - 35);
          boardPane.getChildren().add(pieceView);
        }
      } else if (piecesAtCell.size() > 1) {
        PositionDTO mainDto = piecesAtCell.get(0);
        ImageView mainPieceView = playerPiecesMap.get(
            mainDto.getPlayerId() + "_" + mainDto.getPieceId());

        if (mainPieceView != null) {
          HBox mainParentPanel = playerPiecePanels.get(mainDto.getPlayerId());
          if (mainParentPanel != null && mainParentPanel.getChildren().contains(mainPieceView)) {
            mainParentPanel.getChildren().remove(mainPieceView);
          }

          mainPieceView.setLayoutX(pos.getX() - 20);
          mainPieceView.setLayoutY(pos.getY() - 35);
          boardPane.getChildren().add(mainPieceView);

          Label stackCountLabel = new Label("+" + (piecesAtCell.size() - 1));
          stackCountLabel.setStyle(
              "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");
          stackCountLabel.setLayoutX(pos.getX() + 15);
          stackCountLabel.setLayoutY(pos.getY() - 35);
          boardPane.getChildren().add(stackCountLabel);
          stackCountLabels.add(stackCountLabel);

          for (int i = 1; i < piecesAtCell.size() && i < 5; i++) {
            PositionDTO dto = piecesAtCell.get(i);
            ImageView pieceView = playerPiecesMap.get(dto.getPlayerId() + "_" + dto.getPieceId());

            if (pieceView != null) {
              HBox stackParentPanel = playerPiecePanels.get(dto.getPlayerId());
              if (stackParentPanel != null && stackParentPanel.getChildren().contains(pieceView)) {
                stackParentPanel.getChildren().remove(pieceView);
              }

              pieceView.setLayoutX(pos.getX() - 20 + (i * 5));
              pieceView.setLayoutY(pos.getY() - 35 - (i * 5));
              boardPane.getChildren().add(pieceView);
            }
          }
        }
      }
    }
  }

  @Override
  public void showGameEndDialog(int winnerPlayer) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("게임 종료");
    alert.setHeaderText("플레이어 " + winnerPlayer + " 승리!");
    alert.setContentText("축하합니다!");

    ButtonType newSetupButton = new ButtonType("새 설정으로 게임 시작");
    ButtonType restartButton = new ButtonType("같은 설정으로 재시작");
    ButtonType exitButton = new ButtonType("종료");

    alert.getButtonTypes().setAll(newSetupButton, restartButton, exitButton);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent()) {
      if (result.get() == newSetupButton) {
        if (gameEndListener != null) {
          gameEndListener.onNewGameSetup();
        }
      } else if (result.get() == restartButton) {
        if (gameEndListener != null) {
          gameEndListener.onRestartGame();
        }
      } else if (result.get() == exitButton) {
        if (gameEndListener != null) {
          gameEndListener.onExitGame();
        }
      }
    }
  }

  @Override
  public void clearPieceSelection() {
    for (Map.Entry<String, ImageView> entry : playerPiecesMap.entrySet()) {
      ImageView pieceView = entry.getValue();
      pieceView.setStyle("-fx-effect: null;");
    }
    this.selectedPieceIndex = -1;
  }

  @Override
  public void clearYutImage() {
    yutResultLabel.setText("윷 결과: ");
    yutImageLabel.setImage(null);
  }

  public void closeStage() {
    if (parentStage != null) {
      parentStage.close();
    }
  }
}
