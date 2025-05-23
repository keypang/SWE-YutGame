package view;

import javafx.application.Application;
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
import javafx.stage.WindowEvent;
import model.BoardType;
import model.YutResult;
import model.PositionDTO;

import java.io.File;
import java.util.*;

public class JavaFxPlayScreen extends Application implements GamePlayView {

  private Stage parentStage;

  private int playerCount;
  private int pieceCount;
  private BoardType boardType;

  // 리스너들
  private ThrowButtonListener throwButtonListener;
  private FixedYutButtonListener fixedYutButtonListener;
  private PieceSelectionListener pieceSelectionListener;
  private CellSelectionListener cellSelectionListener;
  private GameEndListener gameEndListener;
  private TakeOutButtonListener takeOutButtonListener;

  // 버튼
  private Button testRollButton;
  private Button rollButton;

  // UI 요소들
  private ImageView turnArrowLabel;
  private ArrayList<Image> playerIcons = new ArrayList<>();
  private ArrayList<ImageView> pieceLabels = new ArrayList<>();
  private ArrayList<ImageView> playerArrows = new ArrayList<>(); // 각 플레이어의 화살표 ImageView
  private Label currentPlayerLabel;
  private Label statusMessageLabel;
  private ImageView yutImageLabel;
  private Label yutResultLabel;
  private VBox yutResultsPanel;
  private List<Label> yutResultLabels = new ArrayList<>();
  private List<Circle> movablePoints = new ArrayList<>();
  private List<Label> stackCountLabels = new ArrayList<>();

  // 게임 상태
  private int currentPlayerIndex = 0;
  private int selectedPieceIndex = 0;
  private boolean waitingPieceSelection = false;
  private int isCorrectPlayer = 1;

  // 말 관리
  private Map<String, ImageView> playerPiecesMap = new HashMap<>();
  private Map<Integer, HBox> playerPiecePanels = new HashMap<>();

  // 좌표 맵
  private Map<Integer, Point2D> squarePositionMap = new HashMap<>();
  private Map<Integer, Point2D> pentagonPositionMap = new HashMap<>();
  private Map<Integer, Point2D> hexagonPositionMap = new HashMap<>();

  // 보드 패널 참조
  private Pane boardPane;

  public JavaFxPlayScreen(int playerCount, int pieceCount, BoardType boardType) {
    this.playerCount = playerCount;
    this.pieceCount = pieceCount;
    this.boardType = boardType;
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("게임 화면");
    parentStage = primaryStage;

    // 전체 패널 생성 (메인 컨테이너)
    BorderPane mainPane = new BorderPane();
    mainPane.setStyle("-fx-background-color: #FFF5E6;");

    // 게임 판 패널 (왼쪽 영역)
    boardPane = createBoardPane();
    mainPane.setLeft(boardPane);

    // 게임 정보 및 컨트롤 패널 (오른쪽 영역)
    VBox controlPane = createControlPane();
    mainPane.setCenter(controlPane);

    // 좌표 초기화
    initializePositions();

    Scene scene = new Scene(mainPane, 1280, 800);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  // 게임 판 패널 생성
  private Pane createBoardPane() {
    Pane pane = new Pane();
    pane.setPrefSize(700, 720);
    pane.setStyle("-fx-background-color: #fff5e6;");

    // 윷놀이 판 이미지 추가
    String boardImagePath = "src/view/images/" + boardType.name() + ".png";
    File boardImageFile = new File(boardImagePath);
    Image boardImage = new Image(boardImageFile.toURI().toString());
    ImageView boardView = new ImageView(boardImage);

    // 보드 타입에 따라 크기 및 위치 조정
    if (boardType == BoardType.SQUARE) {
      boardView.setFitWidth(600);
      boardView.setFitHeight(600);
      boardView.setLayoutX(40);
      boardView.setLayoutY(80);
    } else if (boardType == BoardType.PENTAGON) {
      boardView.setFitWidth(700);
      boardView.setFitHeight(700);
      boardView.setLayoutX(50);
      boardView.setLayoutY(30);
    } else { // HEXAGON
      boardView.setFitWidth(800);
      boardView.setFitHeight(800);
      boardView.setLayoutX(10);
      boardView.setLayoutY(0);
    }

    pane.getChildren().add(boardView);

    // 완주 셀 추가
    String goalImagePath = "src/view/images/finishCell.png";
    File finishCellFile = new File(goalImagePath);
    Image goalImage = new Image(finishCellFile.toURI().toString());
    ImageView goalView = new ImageView(goalImage);

    // 보드 타입에 따라 크기 및 위치 조정
    if (boardType == BoardType.SQUARE) {
      goalView.setFitWidth(43);
      goalView.setFitHeight(43);
      goalView.setLayoutX(647);
      goalView.setLayoutY(602);
    } else if (boardType == BoardType.PENTAGON) {
      goalView.setFitWidth(43);
      goalView.setFitHeight(43);
      goalView.setLayoutX(658);
      goalView.setLayoutY(570);
    } else { // HEXAGON
      goalView.setFitWidth(43);
      goalView.setFitHeight(43);
      goalView.setLayoutX(658);
      goalView.setLayoutY(570);
    }

    pane.getChildren().add(goalView);

    return pane;
  }

  // 게임 정보 및 컨트롤 패널 생성
  private VBox createControlPane() {
    VBox pane = new VBox(5);
    pane.setPadding(new Insets(20));
    pane.setStyle("-fx-background-color: #fff5e6;");

    VBox innerBox = new VBox(15);
    innerBox.setAlignment(Pos.TOP_CENTER);

    // 플레이어 정보 패널
    VBox playerInfoBox = createPlayerInfoPane();
    TitledPane playerInfoTitledPane = new TitledPane("플레이어 정보", playerInfoBox);
    playerInfoTitledPane.setCollapsible(false);

    // 현재 플레이어 정보 패널
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

    // 윷 결과 표시 패널
    HBox resultListPane = createYutResultPane();
    TitledPane resultListTitledPane = new TitledPane("윷 결과", resultListPane);
    resultListTitledPane.setCollapsible(false);

    // 버튼 패널
    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);

    // 지정윷 던지기 버튼
    testRollButton = new Button("지정윷 던지기");
    testRollButton.setPrefSize(150, 60);
    testRollButton.setStyle(
        "-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
    testRollButton.setOnAction(e -> {
      setThrowButtonEnabled(false);
      displayResultSelect();
    });

    // 윷 던지기 버튼
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

    // 게임 종료 테스트 버튼
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

  // 플레이어 정보 패널 생성
  private VBox createPlayerInfoPane() {
    VBox pane = new VBox(4);
    pane.setStyle(
        "-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

    // 턴 화살표 이미지 로드
    String turnArrowPath = "src/view/images/turnArrow.png";
    File turnArrowFile = new File(turnArrowPath);
    Image turnArrowImage = new Image(turnArrowFile.toURI().toString());

    // 각 플레이어 정보 행 패널 생성
    for (int i = 0; i < playerCount; i++) {
      int playerId = i + 1;

      // 플레이어 패널(전체)
      VBox playerBox = new VBox(5);
      playerBox.setStyle("-fx-background-color: #fff5e6;");

      // 플레이어 정보 행 (이름과 화살표)
      HBox infoRow = new HBox(10);
      infoRow.setAlignment(Pos.CENTER_LEFT);
      infoRow.setPadding(new Insets(5));
      infoRow.setStyle("-fx-background-color: #fff5e6;");

      ImageView arrow = new ImageView();
      arrow.setFitHeight(30);
      arrow.setFitWidth(30);

      // 첫 번째 플레이어에게만 화살표 표시
      if (i == 0) {
        arrow.setImage(turnArrowImage);
        turnArrowLabel = arrow; // 이전 코드 호환성을 위해 유지
      }

      // 모든 플레이어의 화살표 ImageView를 리스트에 저장
      playerArrows.add(arrow);

      // 플레이어 이름
      Label nameLabel = new Label("PLAYER" + playerId);
      nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

      // 플레이어 말 아이콘 로드 및 크기 조정
      String pieceImagePath = "src/view/images/플레이어" + playerId + "말.png";
      File pieceImageFile = new File(pieceImagePath);
      Image pieceImage = new Image(pieceImageFile.toURI().toString());
      ImageView pieceImageView = new ImageView(pieceImage);

      pieceImageView.setFitHeight(40);
      pieceImageView.setFitWidth(40);
      playerIcons.add(pieceImage);
      pieceLabels.add(pieceImageView);

      // 여백용 Region
      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);

      infoRow.getChildren().addAll(arrow, nameLabel, spacer, pieceImageView);

      // 말 행 패널
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

  // 윷 결과 표시 패널 생성
  private HBox createYutResultPane() {
    HBox pane = new HBox(20);
    pane.setPadding(new Insets(10));
    pane.setStyle(
        "-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

    // 최근 윷 결과 표시 영역(왼쪽)
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

    // 윷 결과 리스트 표시 영역 (오른쪽)
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

  // 말 선택 시 처리 메서드
  private void onPieceSelected(int pieceId) {
    Map<Integer, Integer> availableCells = null;
    if (pieceSelectionListener != null) {
      selectedPieceIndex = pieceId;
      System.out.println("말 선택한 후 전달될 piece ID :" + pieceId);
      availableCells = pieceSelectionListener.onPieceSelected(pieceId);

      ImageView selectedPiece = playerPiecesMap.get((currentPlayerIndex + 1) + "_" + pieceId);
      if (selectedPiece != null) {
        selectedPiece.setStyle("-fx-effect: dropshadow(gaussian, blue, 10, 0.5, 0, 0);");
      }
    }
    repaintAllPieces();
    showMovablePoints(availableCells);
  }

  // 말 옮기는 지점 표시 메서드
  public void showMovablePoints(Map<Integer, Integer> availableCells) {
    // 기존 이동 가능 지점들 제거
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

      // JavaFX Circle을 사용하여 정확한 중앙 정렬
      Circle movablePoint = new Circle();
      movablePoint.setRadius(15); // 원의 반지름
      movablePoint.setFill(Color.LIGHTGREEN); // 연한 녹색 채우기
      movablePoint.setStroke(Color.DARKGREEN); // 진한 녹색 테두리
      movablePoint.setStrokeWidth(2); // 테두리 두께
      movablePoint.setCursor(Cursor.HAND);

      // 반투명 효과로 보드 위의 요소들이 보이도록
      movablePoint.setOpacity(0.8);

      // 원의 중앙을 정확히 좌표에 맞춤
      movablePoint.setCenterX(target.getX());
      movablePoint.setCenterY(target.getY());

      movablePoints.add(movablePoint);

      final int finalCellId = cellId;
      movablePoint.setOnMouseClicked(e -> {
        System.out.println("말 옮기기 전 전달될 cell ID :" + finalCellId);
        if (cellSelectionListener != null) {
          cellSelectionListener.onCellSelected(finalCellId);
        }

        // 이동 가능 지점들 제거
        for (Circle targetPoint : movablePoints) {
          boardPane.getChildren().remove(targetPoint);
        }
        movablePoints.clear();
      });

      // 마우스 호버 효과 추가
      movablePoint.setOnMouseEntered(e -> {
        movablePoint.setRadius(18); // 호버 시 크기 증가
        movablePoint.setFill(Color.LIME); // 더 밝은 색으로 변경
      });

      movablePoint.setOnMouseExited(e -> {
        movablePoint.setRadius(15); // 원래 크기로 복원
        movablePoint.setFill(Color.LIGHTGREEN); // 원래 색으로 복원
      });

      boardPane.getChildren().add(movablePoint);
    }
  }

  // 지정 윷을 정하는 화면 표시하는 메서드
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

  // 단일 윷 결과를 표시
  private void displaySingleYutResult(YutResult result) {
    String resultText = result.name();
    String imageName = getYutImageName(result.getMove());

    yutResultLabel.setText("윷 결과: " + resultText);

    if (imageName != null) {
      String yutImagePath = "src/view/images/" + imageName + ".png";
      File yutImageFile = new File(yutImagePath);
      Image yutImage = new Image(yutImageFile.toURI().toString());
      yutImageLabel.setImage(yutImage);

      // 팝업 창에 이미지 표시
      showYutResultPopup(imageName, resultText);
    }
  }

  // 윷 이미지 이름 반환
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

  // 윷 결과 팝업 표시
  private void showYutResultPopup(String imageName, String resultText) {
    Stage resultDialog = new Stage();
    resultDialog.initOwner(parentStage);
    resultDialog.initModality(Modality.APPLICATION_MODAL);
    resultDialog.setTitle("윷 결과: " + resultText);

    VBox container = new VBox(20);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(20));
    container.setStyle("-fx-background-color: #fff5e6;");

    String popupImagePath = "src/view/images/" + imageName + ".png";
    File popupImageFile = new File(popupImagePath);
    Image popupImage = new Image(popupImageFile.toURI().toString());
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

    // 2초 후 자동으로 닫기
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

  // 보드에 따른 좌표 반환 메서드
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

  // Alert 표시
  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("알림");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  // 좌표 초기화
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
    squarePositionMap.put(-1, new Point2D(667, 602));
    squarePositionMap.put(0, new Point2D(568, 604));
    squarePositionMap.put(1, new Point2D(574, 494));
    squarePositionMap.put(2, new Point2D(574, 405));
    squarePositionMap.put(3, new Point2D(574, 320));
    squarePositionMap.put(4, new Point2D(574, 234));
    squarePositionMap.put(5, new Point2D(574, 131));
    squarePositionMap.put(6, new Point2D(465, 127));
    squarePositionMap.put(7, new Point2D(379, 127));
    squarePositionMap.put(8, new Point2D(293, 127));
    squarePositionMap.put(9, new Point2D(208, 127));
    squarePositionMap.put(10, new Point2D(91, 131));
    squarePositionMap.put(11, new Point2D(99, 234));
    squarePositionMap.put(12, new Point2D(99, 320));
    squarePositionMap.put(13, new Point2D(99, 405));
    squarePositionMap.put(14, new Point2D(99, 494));
    squarePositionMap.put(15, new Point2D(90, 604));
    squarePositionMap.put(16, new Point2D(208, 602));
    squarePositionMap.put(17, new Point2D(293, 602));
    squarePositionMap.put(18, new Point2D(379, 602));
    squarePositionMap.put(19, new Point2D(465, 602));
    squarePositionMap.put(20, new Point2D(568, 602));
    squarePositionMap.put(50, new Point2D(488, 212));
    squarePositionMap.put(55, new Point2D(423, 277));
    squarePositionMap.put(100, new Point2D(185, 213));
    squarePositionMap.put(110, new Point2D(250, 277));
    squarePositionMap.put(150, new Point2D(185, 515));
    squarePositionMap.put(165, new Point2D(250, 450));
    squarePositionMap.put(200, new Point2D(488, 516));
    squarePositionMap.put(220, new Point2D(423, 450));
    squarePositionMap.put(1000, new Point2D(330, 370));
  }

  private void initializePentagonPositions() {
    pentagonPositionMap.put(-1, new Point2D(588, 602));
    pentagonPositionMap.put(0, new Point2D(491, 588));
    pentagonPositionMap.put(1, new Point2D(527, 520));
    pentagonPositionMap.put(2, new Point2D(541, 469));
    pentagonPositionMap.put(3, new Point2D(558, 419));
    pentagonPositionMap.put(4, new Point2D(575, 367));
    pentagonPositionMap.put(5, new Point2D(582, 296));
    pentagonPositionMap.put(6, new Point2D(530, 240));
    pentagonPositionMap.put(7, new Point2D(490, 210));
    pentagonPositionMap.put(8, new Point2D(450, 184));
    pentagonPositionMap.put(9, new Point2D(408, 155));
    pentagonPositionMap.put(10, new Point2D(350, 130));
    pentagonPositionMap.put(11, new Point2D(287, 157));
    pentagonPositionMap.put(12, new Point2D(248, 184));
    pentagonPositionMap.put(13, new Point2D(208, 212));
    pentagonPositionMap.put(14, new Point2D(170, 240));
    pentagonPositionMap.put(15, new Point2D(115, 292));
    pentagonPositionMap.put(16, new Point2D(123, 366));
    pentagonPositionMap.put(17, new Point2D(139, 417));
    pentagonPositionMap.put(18, new Point2D(155, 470));
    pentagonPositionMap.put(19, new Point2D(171, 522));
    pentagonPositionMap.put(20, new Point2D(202, 594));
    pentagonPositionMap.put(21, new Point2D(275, 598));
    pentagonPositionMap.put(22, new Point2D(324, 596));
    pentagonPositionMap.put(23, new Point2D(375, 595));
    pentagonPositionMap.put(24, new Point2D(425, 597));
    pentagonPositionMap.put(50, new Point2D(503, 320));
    pentagonPositionMap.put(55, new Point2D(430, 345));
    pentagonPositionMap.put(100, new Point2D(347, 212));
    pentagonPositionMap.put(110, new Point2D(349, 285));
    pentagonPositionMap.put(150, new Point2D(195, 318));
    pentagonPositionMap.put(165, new Point2D(268, 344));
    pentagonPositionMap.put(200, new Point2D(250, 517));
    pentagonPositionMap.put(220, new Point2D(300, 450));
    pentagonPositionMap.put(250, new Point2D(441, 520));
    pentagonPositionMap.put(275, new Point2D(395, 446));
    pentagonPositionMap.put(1000, new Point2D(350, 375));
  }

  private void initializeHexagonPositions() {
    hexagonPositionMap.put(-1, new Point2D(613, 474));
    hexagonPositionMap.put(0, new Point2D(534, 477));
    hexagonPositionMap.put(1, new Point2D(538, 416));
    hexagonPositionMap.put(2, new Point2D(538, 381));
    hexagonPositionMap.put(3, new Point2D(538, 346));
    hexagonPositionMap.put(4, new Point2D(538, 310));
    hexagonPositionMap.put(5, new Point2D(533, 263));
    hexagonPositionMap.put(6, new Point2D(492, 220));
    hexagonPositionMap.put(7, new Point2D(458, 201));
    hexagonPositionMap.put(8, new Point2D(427, 183));
    hexagonPositionMap.put(9, new Point2D(394, 165));
    hexagonPositionMap.put(10, new Point2D(336, 157));
    hexagonPositionMap.put(11, new Point2D(289, 167));
    hexagonPositionMap.put(12, new Point2D(257, 185));
    hexagonPositionMap.put(13, new Point2D(224, 204));
    hexagonPositionMap.put(14, new Point2D(191, 222));
    hexagonPositionMap.put(15, new Point2D(141, 263));
    hexagonPositionMap.put(16, new Point2D(145, 310));
    hexagonPositionMap.put(17, new Point2D(145, 345));
    hexagonPositionMap.put(18, new Point2D(145, 381));
    hexagonPositionMap.put(19, new Point2D(145, 416));
    hexagonPositionMap.put(20, new Point2D(141, 477));
    hexagonPositionMap.put(21, new Point2D(194, 494));
    hexagonPositionMap.put(22, new Point2D(226, 512));
    hexagonPositionMap.put(23, new Point2D(257, 530));
    hexagonPositionMap.put(24, new Point2D(290, 549));
    hexagonPositionMap.put(25, new Point2D(336, 580));
    hexagonPositionMap.put(26, new Point2D(396, 551));
    hexagonPositionMap.put(27, new Point2D(428, 532));
    hexagonPositionMap.put(28, new Point2D(458, 514));
    hexagonPositionMap.put(29, new Point2D(491, 497));
    hexagonPositionMap.put(30, new Point2D(534, 477));
    hexagonPositionMap.put(300, new Point2D(471, 435));
    hexagonPositionMap.put(330, new Point2D(415, 405));
    hexagonPositionMap.put(50, new Point2D(473, 293));
    hexagonPositionMap.put(55, new Point2D(416, 323));
    hexagonPositionMap.put(100, new Point2D(341, 219));
    hexagonPositionMap.put(110, new Point2D(341, 281));
    hexagonPositionMap.put(150, new Point2D(209, 293));
    hexagonPositionMap.put(165, new Point2D(267, 323));
    hexagonPositionMap.put(200, new Point2D(214, 434));
    hexagonPositionMap.put(220, new Point2D(269, 403));
    hexagonPositionMap.put(250, new Point2D(342, 506));
    hexagonPositionMap.put(275, new Point2D(342, 447));
    hexagonPositionMap.put(1000, new Point2D(334, 371));
  }

  // GamePlayView 인터페이스 구현 메소드들
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
  public void setTakeOutButtonListener(TakeOutButtonListener takeOutButtonListener) {
    this.takeOutButtonListener = takeOutButtonListener;
  }

  @Override
  public void updateCurrentPlayer(int playerNumber) {
    currentPlayerLabel.setText("현재 플레이어: " + playerNumber);
    currentPlayerIndex = playerNumber - 1;

    // 턴 화살표 이미지 로드
    String turnArrowPath = "src/view/images/turnArrow.png";
    File turnArrowFile = new File(turnArrowPath);
    Image turnArrowImage = new Image(turnArrowFile.toURI().toString());

    // 모든 플레이어의 화살표 제거
    for (int i = 0; i < playerArrows.size(); i++) {
      ImageView arrow = playerArrows.get(i);
      arrow.setImage(null); // 화살표 이미지 제거
    }

    // 현재 플레이어에게만 화살표 표시
    if (playerNumber > 0 && playerNumber <= playerArrows.size()) {
      ImageView currentArrow = playerArrows.get(playerNumber - 1); // playerNumber는 1부터 시작
      currentArrow.setImage(turnArrowImage);
    }

    // 플레이어 턴 변경 알림
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

    // 보드에서 모든 말 제거
    for (Map.Entry<String, ImageView> entry : playerPiecesMap.entrySet()) {
      ImageView pieceView = entry.getValue();
      if (boardPane.getChildren().contains(pieceView)) {
        boardPane.getChildren().remove(pieceView);
      }
    }

    // 스택 카운트 라벨들 제거
    for (Label label : stackCountLabels) {
      if (boardPane.getChildren().contains(label)) {
        boardPane.getChildren().remove(label);
      }
    }
    stackCountLabels.clear();

    // 대기 상태(-1)인 말들 처리 - 플레이어 패널로 돌려보내기
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

    // 각 셀별로 말 개수 카운트
    Map<Integer, List<PositionDTO>> cellPieces = new HashMap<>();
    for (PositionDTO dto : currentPositions) {
      int cellId = dto.getCellId();

      // 완주한 말과 대기 상태는 보드에 표시하지 않음
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

    // 보드에 말 배치
    for (Map.Entry<Integer, List<PositionDTO>> entry : cellPieces.entrySet()) {
      int cellId = entry.getKey();
      List<PositionDTO> piecesAtCell = entry.getValue();
      Point2D pos = getPositionByBoardType(cellId);

      if (pos == null) {
        continue;
      }

      if (piecesAtCell.size() == 1) {
        // 말이 1개일 경우
        PositionDTO dto = piecesAtCell.get(0);
        ImageView pieceView = playerPiecesMap.get(dto.getPlayerId() + "_" + dto.getPieceId());

        if (pieceView != null) {
          // 기존 부모에서 제거
          HBox parentPanel = playerPiecePanels.get(dto.getPlayerId());
          if (parentPanel != null && parentPanel.getChildren().contains(pieceView)) {
            parentPanel.getChildren().remove(pieceView);
          }

          pieceView.setLayoutX(pos.getX() - 20);
          pieceView.setLayoutY(pos.getY() - 35);
          boardPane.getChildren().add(pieceView);
        }
      } else if (piecesAtCell.size() > 1) {
        // 말이 여러 개일 경우 (업혀있는 경우)
        PositionDTO mainDto = piecesAtCell.get(0);
        ImageView mainPieceView = playerPiecesMap.get(
            mainDto.getPlayerId() + "_" + mainDto.getPieceId());

        if (mainPieceView != null) {
          // 기존 부모에서 제거
          HBox mainParentPanel = playerPiecePanels.get(mainDto.getPlayerId());
          if (mainParentPanel != null && mainParentPanel.getChildren().contains(mainPieceView)) {
            mainParentPanel.getChildren().remove(mainPieceView);
          }

          mainPieceView.setLayoutX(pos.getX() - 20);
          mainPieceView.setLayoutY(pos.getY() - 35);
          boardPane.getChildren().add(mainPieceView);

          // 업힌 말 수 표시 라벨 생성
          Label stackCountLabel = new Label("+" + (piecesAtCell.size() - 1));
          stackCountLabel.setStyle(
              "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red; -fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");
          stackCountLabel.setLayoutX(pos.getX() + 15);
          stackCountLabel.setLayoutY(pos.getY() - 35);
          boardPane.getChildren().add(stackCountLabel);
          stackCountLabels.add(stackCountLabel);

          // 나머지 말들 처리
          for (int i = 1; i < piecesAtCell.size() && i < 5; i++) {
            PositionDTO dto = piecesAtCell.get(i);
            ImageView pieceView = playerPiecesMap.get(dto.getPlayerId() + "_" + dto.getPieceId());

            if (pieceView != null) {
              // 기존 부모에서 제거
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

    // 커스텀 버튼들
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
}