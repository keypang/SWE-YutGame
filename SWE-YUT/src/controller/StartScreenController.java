package controller;

import model.BoardType;
import model.Model;
import model.StartInfo;
import view.GameView;

public class StartScreenController {

    private GameView view;
    private Model model;

    // 생성자
    public StartScreenController(GameView view, Model model) {
        this.view = view;
        this.model = model;

        initController();
    }

    // 컨트롤러 초기화
    private void initController() {

        view.setStartButtonListener(new GameView.StartButtonListener() {
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
        if (model.validate(playerCount, pieceCount, boardType)) {

            StartInfo startInfo = new StartInfo(playerCount, pieceCount, boardType);
            model.setStartInfo(startInfo);

            // 모델에 정보 제대로 담겼는지 확인
            check();
        } else {
            // 유효성 검사 실패한 경우  , 화면에 에러메시지 띄우기
            view.displayErrorMessage("제대로 입력해라.");
        }
    }

    // 모델에 정보 제대로 담겼는지 확인
    private void check() {
        // 게임 설정하는 화면 닫고
        view.close();
        // model 에 게임 설정 정보가 제대로 담겼는지 확인
        model.test();
    }

    // getter
    public GameView getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }
}