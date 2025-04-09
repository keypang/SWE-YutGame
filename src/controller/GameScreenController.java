package controller;

import model.GameManager;
import model.Yut;
import model.YutResult;
import view.GameView;

public class GameScreenController {
    private GameView gameView;
    private GameManager gameManager;
    private Yut yut;

    public GameScreenController(GameView gameView, GameManager model){
        this.gameView = gameView;
        this.gameManager = model;
    }

    // 랜덤 윷 던지는 버튼을 눌렀을 때
    public YutResult RandomYutThrow(){
        // View로 부터 정보를 어떻게 받아오겠죠?
        YutResult yutResult = gameManager.throwYut();
        return yutResult;
    }

    // 지정 윷 던지는 버튼을 눌렀을 때
    public YutResult FixedYutThrow(String getresult){
        // View로 부터 정보를 어떻게 받아오겠죠?
        YutResult yutResult = gameManager.throwFixedYut(getresult);
        return yutResult;
    }

    // 윷 굴리기 이전 초기상태 설정 (턴 넘어 갔을 때)

    // 말을 선택했을 때
    public void PieceSelect(int selectpiece){
        // 선택된 말이 갈 수 있는 곳을 표현해주는 메서드 구현
        // 모델로 부터 받아올 값은 좌표 값들 (ex. 7,9,12..) view로 넘겨주면 됨.

    }

    // 좌표 선택했을 때
    public void playercontrolresult(String selectedyut){
        // 모델에서는 선택된 말이 무엇인지를 알고 있어야함.
        // 모델에서 그 선택된 좌표 계산해서 옮기고 전체상태 넘겨주면됨. (여기서 만약 추가 턴 여부 발생시 윷굴리는 과정 시작)
    }
}
