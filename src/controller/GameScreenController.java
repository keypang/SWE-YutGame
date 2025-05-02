package controller;

import model.GameManager;
import model.PositionDTO;
import model.Yut;
import model.YutResult;
import view.GamePlayView;

import java.util.ArrayList;

public class GameScreenController {
    private GamePlayView gameView;
    private GameManager gameManager;
    private Yut yut;

    public GameScreenController(GamePlayView gameView, GameManager model){
        this.gameView = gameView;
        this.gameManager = model;

        initController();
    }

    // 컨트롤러 초기화
    private void initController() {

        gameView.setThrowButtonListener(new GamePlayView.ThrowButtonListener(){
            @Override
            public YutResult onThrowButtonClicked() {
                return RandomYutThrow();
            }
        });

    }

    // 랜덤 윷 던지는 버튼을 눌렀을 때
    public ArrayList RandomYutThrow(){
        YutResult yutResult = gameManager.throwYut();
        gameManager.setExtraTurn(yutResult.canRollAgain());
        if (gameManager.getExtraTurn()){
            // 윷버튼 활성화
        }
        else {
            // 윷버튼 비활성화
        }
        return gameManager.getYutResults();
    }

    // 지정 윷 던지는 버튼을 눌렀을 때
    public ArrayList FixedYutThrow(String getresult){
        // View로 부터 정보를 어떻게 받아오겠죠?
        YutResult yutResult = gameManager.throwFixedYut(getresult);
        gameManager.setExtraTurn(yutResult.canRollAgain());
        if (gameManager.getExtraTurn()){
            // 윷버튼 활성화
        }
        else {
            // 윷버튼 비활성화
        }
        return gameManager.getYutResults();
    }

    // 말을 선택했을 때
    public int[] PieceSelect(int selectpiece){
        return gameManager.findMovableCells(selectpiece);
        // 지금 생각해야 할게 selectpiece를 gameManager가 저장하고 있어야 하는 거 아닌가?

    }

    // 좌표 선택했을 때
    public void selectCoordinate(int coordinate){
        // 모델에서는 선택된 말이 무엇인지를 알고 있어야함.

        // 좌표값을 움직이는 칸수로 변환해야함. (현재 model에서 구현된 방식)
        // 잡혔을 때 extraturn이 바뀌어 있는지 확인해야함.

        if (gameManager.getExtraTurn()){
            // 윷버튼 활성화
        }
        else {
            if (gameManager.isYutResultsEmpty()) {
                // 턴넘기기
            }
            else {
                // 일단 전체 상태를 넘겨주고
                // 말을 선택할 수 있는 상태로 가야 하는데
            }
        }

    }

    // 전체 상태를 넘겨주는 과정
    public ArrayList<PositionDTO> fetchBoardStatus() {
        ArrayList<PositionDTO> status = gameManager.getAllPiecePos();
        // 이거 받아올 때 extratrun 값도 받아와서 true면 주사위 굴리게 하고 false면 턴 넘겨야함.
        return status;
    }



}
