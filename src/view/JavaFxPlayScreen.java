package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.BoardType;
import model.YutResult;
import model.PositionDTO;

import java.io.File;
import java.util.*;

public class JavaFxPlayScreen extends Application {

    private Stage parentStage;

    private int playerCount;
    private int pieceCount;
    private BoardType boardType;

    // 필드
    private GamePlayView.FixedYutButtonListener fixedYutButtonListener;

    // 버튼
    private Button testRollButton;
    private Button rollButton;

    // 이미지
    private ImageView turnArrowLabel;
    private ArrayList<Image> playerIcons = new ArrayList<>();

    // 뷰(Swing Label)
    private ArrayList<ImageView> pieceLabels = new ArrayList<>();

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

        //테스트용 좌표 출력
        mainPane.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            System.out.println("클릭한 위치: (" + x + ", " + y + ")");
        });

        // 게임 판 패널 (왼쪽 영역)
        Pane boardPane = createBoardPane();
        mainPane.setLeft(boardPane);

        // 게임 정보 및 컨트롤 패널 (오른쪽 영역)
        VBox controlPane = createControlPane();
        mainPane.setCenter(controlPane);

        Scene scene = new Scene(mainPane, 1280, 800);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    //게임 판 패널 생성
    private Pane createBoardPane() {
        Pane pane = new Pane();
        pane.setPrefSize(700,720);
        pane.setStyle("-fx-background-color: #fff5e6;");

        //윷놀이 판 이미지 추가
        String boardImagePath = "src/view/images/" + boardType.name() + ".png";
        File boardImageFile = new File(boardImagePath);
        Image boardImage = new Image(boardImageFile.toURI().toString());
        ImageView boardView = new ImageView(boardImage);

        //보드 타입에 따라 크기 및 위치 조정
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

        //완주 셀 추가
        String goalImagePath = "src/view/images/finishCell.png";
        File finishCellFile = new File(goalImagePath);
        Image goalImage = new Image(finishCellFile.toURI().toString());
        ImageView goalView = new ImageView(goalImage);

        //보드 타입에 따라 크기 및 위치 조정
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

        // 전체 패널에 GridBagLayout 적용
        VBox innerBox = new VBox(15);
        innerBox.setAlignment(Pos.TOP_CENTER);

        // 플레이어 정보 패널
        VBox playerInfoBox = createPlayerInfoPane();
        innerBox.getChildren().add(playerInfoBox);

        TitledPane playerInfoTitledPane = new TitledPane("플레이어 정보", playerInfoBox);
        playerInfoTitledPane.setCollapsible(false);
        
        // 현재 플레이어 정보 패널
        // 게임 상태 메세지
        VBox currentPlayerBox = new VBox(10);
        currentPlayerBox.setPadding(new Insets(10));
        currentPlayerBox.setStyle("-fx-background-color: #fff5e6;  -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");
        Label currentPlayerLabel = new Label("현재 플레이어: 1");
        currentPlayerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label statusMessageLabel = new Label("게임 시작! 윷을 던져주세요.");
        statusMessageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: darkgreen;");

        TitledPane statusTitledPane = new TitledPane("게임 상태", currentPlayerBox);
        statusTitledPane.setCollapsible(false);

        currentPlayerBox.getChildren().addAll(currentPlayerLabel, statusMessageLabel);

        innerBox.getChildren().add(currentPlayerBox);
        
        // 윷 결과 표시 패널
        HBox resultListPane = createYutResultPane();
        innerBox.getChildren().add(resultListPane);

        TitledPane resultListTitledPane = new TitledPane("윷 결과", resultListPane);
        resultListTitledPane.setCollapsible(false);

        // 버튼 패널
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        // 지정윷 던지기 버튼
        testRollButton = new Button("지정윷 던지기");
        testRollButton.setPrefSize(150,60);
        testRollButton.setStyle("-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        testRollButton.setOnAction(e -> {
            String[] yutResult = {"백도", "도", "개", "걸", "윷", "모"};

            JavaFxSelectYutResultScreen.show(parentStage, yutResult);
            // setThrowButtonEnabled(false);
            // displayResultSelect();
        });

        // 윷 던지기 버튼
        rollButton = new Button("윷 던지기");
        rollButton.setPrefSize(150, 60);
        rollButton.setStyle("-fx-background-color: #78c878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        rollButton.setOnAction(e -> {
            // if (throwButtonListener != null) {
            //     YutResult yutResult = throwButtonListener.onThrowButtonClicked();
            //     displaySingleYutResult(yutResult);
            // }
        });

        buttonBox.getChildren().addAll(testRollButton, rollButton);
        innerBox.getChildren().add(buttonBox);

        // 게임 종료 테스트 버튼
        Button testEndGameButton = new Button("게임 종료 테스트");
        testEndGameButton.setPrefSize(200, 40);
        testEndGameButton.setStyle("-fx-background-color: #ff6464; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        testEndGameButton.setOnAction(e -> {
            // showGameEndDialog(currentPlayerIndex + 1);
        });

        innerBox.getChildren().add(testEndGameButton);

        pane.getChildren().add(playerInfoTitledPane);
        pane.getChildren().add(statusTitledPane);
        pane.getChildren().add(resultListTitledPane);
        pane.getChildren().add(innerBox);

        return pane;
    }

    // 플레이어 정보 패널 생성
    private VBox createPlayerInfoPane(){
        VBox pane = new VBox(4);
        pane.setStyle("-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

        // 턴 화살표 이미지 로드
        String turnArrowPath = "src/view/images/turnArrow.png";
        File turnArrowFile = new File(turnArrowPath);
        Image turnArrowImage = new Image(turnArrowFile.toURI().toString());


        // 각 플레이어 정보 행 패널 생성
        for(int i = 0; i < playerCount; i++){
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

            if(i == 0){
                arrow.setImage(turnArrowImage);
                turnArrowLabel = arrow;
            }

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

            for(int j = 0; j < pieceCount; j++){
                int pieceId = j;

                ImageView pieceView = new ImageView(pieceImage);
                pieceView.setId("piece_" + playerId + "_" + pieceId);
                pieceView.setStyle("-fx-padding: 2 2 2 2;");
                pieceView.setFitHeight(40);
                pieceView.setFitWidth(40);
                pieceView.setCursor(Cursor.HAND);

                pieceView.setOnMouseClicked(e -> {
                    /*if (!waitingPieceSelection) {
                        showMessage("먼저 윷을 던져주세요!");
                        return;
                    }
                    if (playerId != currentPlayerIndex + 1) {
                        showMessage("플레이어의 말이 아닙니다!");
                        return;
                    }
                    waitingPieceSelection = false;
                    onPieceSelected(pieceId);*/
                });

                piecesRow.getChildren().add(pieceView);
                //playerPiecesMap.put(playerId + "_" + pieceId, pieceView);
            }

            //playerPiecePanels.put(playerId, piecesRow);

            playerBox.getChildren().addAll(infoRow, piecesRow);
            pane.getChildren().add(playerBox);
        }

        return pane;
    }

    // 윷 결과 표시 패널 생성
    private HBox createYutResultPane(){
        HBox pane = new HBox(20);
        pane.setPadding(new Insets(10));
        pane.setStyle("-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

        // 최근 윷 결과 표시 영역(왼쪽)
        VBox currentResultBox = new VBox(10);
        currentResultBox.setPadding(new Insets(5));
        currentResultBox.setAlignment(Pos.TOP_LEFT);
        currentResultBox.setPrefSize(120, 120);
        currentResultBox.setStyle("-fx-background-color: #fff5e6;");

        Label yutResultLabel = new Label("윷 결과: ");
        yutResultLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        ImageView yutImageView = new ImageView();
        yutImageView.setFitHeight(40);
        yutImageView.setFitWidth(100);

        currentResultBox.getChildren().addAll(yutResultLabel, yutImageView);

        // 윷 결과 리스트 표시 영역 (오른쪽)
        VBox resultsListBox = new VBox();
        resultsListBox.setPrefSize(150, 120);
        resultsListBox.setMaxWidth(Double.MAX_VALUE);
        resultsListBox.setStyle("-fx-background-color: #fff5e6; -fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

        VBox yutResultsBox = new VBox(5);
        yutResultsBox.setStyle("-fx-background-color: #fff5e6; -fx-padding: 5;");

        ScrollPane scrollPane = new ScrollPane(yutResultsBox);
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
}
