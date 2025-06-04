package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.global.GamePlayView;
import view.swing.SwingPlayScreen;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class YutThrowTest {

  private GameScreenController controller;

  @BeforeEach
  void setup() {
    // 초기 설정: 뷰, 게임 매니저, 시작 정보, 컨트롤러 초기화
    GamePlayView view = new SwingPlayScreen(4, 4, BoardType.SQUARE);
    GameManager gameManager = new GameManager();
    StartInfo startInfo = new StartInfo(4, 4, BoardType.SQUARE);
    gameManager.initGM(startInfo);
    controller = new GameScreenController(view, gameManager); // 필드에 할당
  }

  @Test
  void testYutThrowAndPieceMovement() {
    // 1단계: 윷 던지기 결과를 "도"로 고정
    controller.FixedYutThrow("도");
    List<YutResult> yutResults = controller.getYutResults();
    System.out.println("윷 결과: " + yutResults);
    assertFalse(yutResults.isEmpty(), "윷 결과가 비어있지 않아야 함");

    // 2단계: 말 선택 가능한 목록 조회
    Map<Integer, Integer> selectablePieces = controller.PieceSelect(1); // 플레이어 1
    System.out.println("선택 가능한 말: " + selectablePieces);
    assertFalse(selectablePieces.isEmpty(), "선택 가능한 말이 있어야 함");

    // 3단계: 임의의 말 선택 및 좌표 선택
    Integer selectedPieceId = selectablePieces.keySet().iterator().next();
    controller.selectCoordinate(selectedPieceId);
    System.out.println("선택된 말 ID: " + selectedPieceId);

    // 4단계: 말을 꺼냄 (기지에서 출발)
    List<PositionDTO> takenOutPieces = controller.takeOutPiece();
    System.out.println("꺼낸 말 위치:");
    for (PositionDTO piece : takenOutPieces) {
      System.out.println(piece);
    }
    assertFalse(takenOutPieces.isEmpty(), "꺼낸 말이 있어야 함");
  }
}
