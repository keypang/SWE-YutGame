package model;

public class PositionDTO {
    private final int pieceId;
    private final int playerId;
    private int cellId;

    public PositionDTO(int pieceId, int playerId) {
        this.pieceId = pieceId;
        this.playerId = playerId;
        cellId = -1; // 최초 시작 시 항상 0번 Cell로 초기화 되기 때문에 직접 설정
    }

    public int getPieceId() {
        return pieceId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getCellId() {
        return cellId;
    }

    // 최초 생성 후에는 위치만 변경되기 때문에 CellId에 대해서만 setter 구현
    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    // 객체 일치 확인
    public PositionDTO getCorrectDTO(int pieceId, int playerId) {
        if (pieceId == this.pieceId && playerId == this.playerId) {
            return this;
        }

        return null;
    }

    // TODO: 나중에 JUnit으로 구체적인 테스트 구현 후 삭제
    @Override
    public String toString() {
        // 테스트용 정보 출력
        return playerId+"번 플레이어의 "+pieceId+"번 말은 "+cellId+"번 지점에 있습니다";
    }
}
