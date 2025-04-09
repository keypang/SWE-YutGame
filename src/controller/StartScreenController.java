package controller;

import model.BoardType;
import model.GameManager;
import model.StartInfo;
import view.GameConfigView;
import view.GamePlayView;
import view.SwingPlayScreen;

public class StartScreenController {

    private GameConfigView view; // 게임 설정화면
    private GameManager gameManger;

    // 생성자 (메인 함수에서 넣어주겠죠?)
    public StartScreenController(GameConfigView view, GameManager gameManager) {
        this.view = view;
        this.gameManger = gameManager;

        initController();
    }

    // 컨트롤러 초기화
    private void initController() {

        view.setStartButtonListener(new GameConfigView.StartButtonListener() {
            @Override
            public void onStartButtonClicked() {
                process();
            }
        });

        // 좀 복잡해보이는데.. 나중에 람다식으로 리팩토링 할 수 있음
    }

    // 간단한 예시
    private void process() {

        // 뷰에서 사용자 입력 따오기
        int playerCount = view.getSelectedPlayerCount();
        int pieceCount = view.getSelectedPieceCount();
        BoardType boardType = view.getSelectedBoardType();

        // 사용자 입력에 대해 유효성 검사 진행
        if (gameManger.validate(playerCount, pieceCount, boardType)) {

            StartInfo startInfo = new StartInfo(playerCount, pieceCount, boardType);
            gameManger.setStartInfo(startInfo);

            // 모델에 정보 제대로 담겼는지 확인
            check();
            // 게임시작 추가
            startGame();
        } else {
            // 유효성 검사 실패한 경우  , 화면에 에러메시지 띄우기
            view.displayErrorMessage("제대로 입력해라.");
        }
    }

    // 모델에 정보 제대로 담겼는지 확인
    private void check() {
        gameManger.test();
    }

    public void startGame() {
        // 게임 설정 화면 닫기
        view.close();

        // 게임 화면 생성
        GamePlayView gameView = new SwingPlayScreen(
                gameManger.getStartInfo().getPlayerCount(),
                gameManger.getStartInfo().getPieceCount(),
                gameManger.getStartInfo().getBoardType()
        );

        // 게임화면 컨트롤러 생성 및 연결
        GameScreenController gameController = new GameScreenController(gameView, gameManger);
    }

    // getter
    public GameConfigView getView() {
        return view;
    }

    // getter
    public GameManager getModel() {
        return gameManger;
    }

    public void setGameManger(GameManager gameManger) {
        this.gameManger = gameManger;
    }


}