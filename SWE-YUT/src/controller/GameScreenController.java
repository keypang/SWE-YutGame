package controller;

import model.GameManager;
import model.Yut;
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
    public int RandomYutThrow(){
        // View로 부터 정보를 어떻게 받아오겠죠?
        int num = gameManager.throwYut();
        return num;
    }

    // 지정 윷 던지는 버튼을 눌렀을 때
    public void FixedYutThrow(){
        // View로 부터 정보를 어떻게 받아오겠죠?
        int num = gameManager.throwFixedYut();

    }

    // 말을 선택했을 때
    public void TokenSelect(){


    }

    // 말을 움직일 때
    public void PieceAction(){

    }
}
