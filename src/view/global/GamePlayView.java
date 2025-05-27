package view.global;

import model.YutResult;
import model.PositionDTO;
import java.util.Map;
import java.util.List;

public interface GamePlayView {

  interface ThrowButtonListener {

    /**
     * 던지기 버튼이 클릭되었을 때 호출됩니다.
     *
     * @return 윷 던지기 결과
     */
    YutResult onThrowButtonClicked();
  }

  interface FixedYutButtonListener {

    /**
     * 지정 윷 결과가 선택되었을 때 호출됩니다.
     *
     * @param yutType 선택된 윷 결과 유형 (예: "도", "개", "걸", "윷", "모", "백도")
     * @return 해당하는 YutResult 객체
     */
    YutResult onFixedYutSelected(String yutType);
  }

  interface GameEndListener {

    /**
     * 사용자가 동일한 설정으로 게임을 재시작하기로 선택했을 때 호출됩니다.
     */
    void onRestartGame();

    /**
     * 사용자가 다른 설정으로 새 게임을 시작하기로 선택했을 때 호출됩니다.
     */
    void onNewGameSetup();

    /**
     * 사용자가 게임을 종료하기로 선택했을 때 호출됩니다.
     */
    void onExitGame();
  }

  /**
   * 말 꺼내기 버튼 클릭 이벤트를 처리하기 위한 인터페이스.
   */
  interface TakeOutButtonListener {

    /**
     * 말 꺼내기 버튼이 클릭되었을 때 호출됩니다.
     *
     * @return 새 말을 꺼낸 후 현재 말 위치 목록
     */
    List<PositionDTO> onTakeOutButtonClicked();
  }

  /**
   * 말 선택 이벤트를 처리하기 위한 인터페이스.
   */
  interface PieceSelectionListener {

    /**
     * 말이 선택되었을 때 호출됩니다.
     *
     * @param pieceId 선택된 말의 ID
     * @return 가능한 목적지 셀 ID와 그 값의 맵
     */
    Map<Integer, Integer> onPieceSelected(int pieceId);
  }

  /**
   * 셀 선택 이벤트를 처리하기 위한 인터페이스.
   */
  interface CellSelectionListener {

    /**
     * 말 이동 목적지로 셀이 선택되었을 때 호출됩니다.
     *
     * @param cellId 선택된 셀의 ID
     */
    void onCellSelected(int cellId);
  }

  /**
   * 윷 던지기 버튼 클릭 이벤트 리스너를 설정합니다.
   *
   * @param throwButtonListener 윷 던지기 버튼이 클릭될 때 통보받을 리스너
   */
  void setThrowButtonListener(ThrowButtonListener throwButtonListener);

  /**
   * 지정 윷 선택 이벤트 리스너를 설정합니다.
   *
   * @param listener 지정 윷 결과가 선택될 때 통보받을 리스너
   */
  void setFixedYutButtonListener(FixedYutButtonListener listener);

  /**
   * 말 선택 이벤트 리스너를 설정합니다.
   *
   * @param listener 말이 선택될 때 통보받을 리스너
   */
  void setPieceSelectionListener(PieceSelectionListener listener);

  /**
   * 셀 선택 이벤트 리스너를 설정합니다.
   *
   * @param listener 셀이 선택될 때 통보받을 리스너
   */
  void setCellSelectionListener(CellSelectionListener listener);

  /**
   * 게임 종료 이벤트 리스너를 설정합니다.
   *
   * @param listener 게임이 종료될 때 통보받을 리스너
   */
  void setGameEndListener(GameEndListener listener);

  /**
   * 윷 결과 목록을 표시합니다.
   *
   * @param results 표시할 윷 결과 목록
   */
  void displayYutResultList(List<YutResult> results);

  /**
   * 사용할 윷 결과를 선택하는 패널을 표시합니다. 이 메서드는 사용자가 가능한 결과 세트에서 하나의 윷 결과를 선택할 수 있는 UI 패널이나 대화상자를 표시합니다.
   *
   * @param yutResult 선택 가능한 윷 결과 배열
   * @return 선택된 윷 결과의 이름
   */
  String showYutSelectPanel(YutResult[] yutResult);

  /**
   * 말 꺼내기 버튼 클릭 이벤트 리스너를 설정합니다.
   *
   * @param takeOutButtonListener 말 꺼내기 버튼이 클릭될 때 통보받을 리스너
   */
  void setTakeOutButtonListener(TakeOutButtonListener takeOutButtonListener);

  /**
   * UI에서 현재 플레이어 표시를 업데이트합니다.
   *
   * @param playerNumber 현재 플레이어 번호 (1부터 시작하는 인덱스)
   */
  void updateCurrentPlayer(int playerNumber);

  /**
   * 윷 던지기 버튼을 활성화하거나 비활성화합니다. 이 메서드는 사용자가 언제 윷을 던질 수 있는지 제어하는 데 사용됩니다.
   *
   * @param enabled true면 버튼 활성화, false면 비활성화
   */
  void setThrowButtonEnabled(boolean enabled);

  /**
   * 말 선택 모드를 활성화합니다. 이 메서드는 사용자가 이동시킬 말을 선택해야 할 때 호출됩니다.
   */
  void enableWaitingPieceSelection();

  /**
   * 말 선택 모드를 비활성화합니다. 이 메서드는 사용자가 말을 선택할 수 없을 때 호출됩니다.
   */
  void disablePieceSelection();

  /**
   * UI에 표시되는 상태 메시지를 설정합니다.
   *
   * @param message 표시할 상태 메시지
   */
  void setStatusMessage(String message);

  /**
   * 보드의 모든 말을 다시 그립니다. 이 메서드는 현재 위치에 따라 모든 말의 시각적 표현을 업데이트합니다.
   */
  void repaintAllPieces();

  /**
   * 게임 종료 대화상자를 표시합니다. 이 메서드는 승자를 발표하고 게임 재시작, 새 게임 시작, 종료 옵션을 제공하는 대화상자를 표시합니다.
   *
   * @param winnerPlayer 승리한 플레이어 번호 (1부터 시작하는 인덱스)
   */
  void showGameEndDialog(int winnerPlayer);

  /**
   * 모든 활성 말 선택을 초기화합니다. 이 메서드는 선택된 말의 시각적 표시를 제거합니다.
   */
  void clearPieceSelection();

  /**
   * 윷 이미지 표시를 초기화합니다. 이 메서드는 윷 결과 표시 영역을 재설정합니다.
   */
  void clearYutImage();
}