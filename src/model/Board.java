package model;

import java.util.*;

public class Board {
    private Map<Integer, Cell> cells = new HashMap<>();
    private int shape;

    public Board(ArrayList<Player> players, BoardType boardType) {
        if(boardType == BoardType.SQUARE){
            this.shape = 4;
            generateBoard(4);
        }
        else if(boardType == BoardType.PENTAGON){
            this.shape = 5;
            generateBoard(5);
        }
        else if(boardType == BoardType.HEXAGON){
            this.shape = 6;
            generateBoard(6);
        }
        initBoard(players);
    }

    private void generateBoard(int shape){
        cells.clear();

        List<Integer> inRoadList = new ArrayList<>();
        List<Integer> outRoadList = new ArrayList<>();

        // 들어갈 수 있는 갈림길
        for(int i = 1; i < shape-1; i++) {
            inRoadList.add(i*5);
        }

        // 나가는 갈림길
        for(int i = shape-1; i<shape+1; i++) {
            outRoadList.add(i*5);
        }

        //외부 셀 생성
        for(int i = 1; i<shape*5; i++) {
            if(inRoadList.contains(i)) {
                Cell cell = new Cell(i, "갈림길", i/5);
                cells.put(i, cell);
            }
            else {
                Cell cell = new Cell(i, "일반",0);
                cells.put(i, cell);
            }
        }

        //시작 셀
        Cell cell = new Cell(0,"출발", 0);
        cells.put(0,cell);

        //도착 셀
        cell = new Cell(shape*5, "도착",shape);
        cells.put(shape*5,cell);

        //완주 셀
        cell = new Cell(shape*5+1, "완주",0);
        cells.put(shape*5+1,cell);

        //완주 셀 연결
        cells.get(shape*5).addNextCell(cells.get(shape*5+1));

        //시작 셀 연결
        cells.get(0).addNextCell(cells.get(1));

        //1번 셀 연결
        cells.get(1).addPreviousCell(cells.get(5*shape));

        //외부 셀 연결
        for(int i = 1; i<shape*5; i++) {
            cells.get(i).addNextCell(cells.get(i+1));
            cells.get(i+1).addPreviousCell(cells.get(i));
        }

        //교차로 셀
        cell = new Cell(1000, "교차로", 0);
        cells.put(1000,cell);

        //지름길
        for(int i = 1; i<(shape+1); i++) {
            cell = new Cell(i*50,"지름길",i);
            cells.put(i*50,cell);
            cell = new Cell(i*55,"지름길",i);
            cells.put(i*55,cell);
        }

        //끝 - 1 라인
        cells.get(outRoadList.getFirst()*10).setType("일반");
        cells.get(outRoadList.getFirst()*11).setType("일반");

        //갈림길 진입점
        for(int in: inRoadList) {
            cells.get(in).addNextCell(cells.get(in*10));
            cells.get(in*10).addPreviousCell(cells.get(in));
            cells.get(in*10).addNextCell(cells.get(in*11));
            cells.get(in*11).addPreviousCell(cells.get(in*10));
            cells.get(in*11).addNextCell(cells.get(1000));
            cells.get(1000).addPreviousCell(cells.get(in*11));
        }

        //나가는 길
        for(int out: outRoadList) {
            cells.get(1000).addNextCell(cells.get(out*11));
            cells.get(out*11).addNextCell(cells.get(out*10));
            cells.get(out*10).addNextCell(cells.get(out));
            cells.get(out).addPreviousCell(cells.get(out*10));
            cells.get(out*10).addPreviousCell(cells.get(out*11));
            cells.get(out*11).addPreviousCell(cells.get(1000));
        }

        //대기 셀 -> -1
        cell = new Cell(-1,"대기",-1);
        cells.put(-1,cell);
    }

    private void initBoard(ArrayList<Player> players) {
        for(Player player : players) {
            for(int i = 0; i<player.getPieceNum(); i++) {
                player.getPieces(i).setStartCell(cells.get(-1));
            }
        }
    }

    private boolean updatePieceLocation(Cell current, Cell startAt, Piece piece) {

        if(startAt.getPieces().isEmpty()){
            startAt.addPiece(piece);
        }
        if(current.checkCatchPiece(piece)) {
            System.out.println("잡았다");
            for (Piece p : current.getPieces()) {
                p.setStartCell(getCell(-1));
            }

            current.clearPieces();
            for(Piece p : startAt.getPieces()) {
                current.addPiece(p);
                p.setStartCell(current);
            }
            startAt.clearPieces();

            //test psy
            for(Piece p : startAt.getPieces()) {
                System.out.println("s."+p.getId());
            }

            for(Piece p : current.getPieces()) {
                System.out.println("c."+p.getId());
            }


            for (Piece p : current.getPieces()) {
                System.out.println(p.getId());  // 각 Piece의 id를 출력
            }

            return true;
        }
        else {
            System.out.println("업었다");

            List<Piece> pieceList = new ArrayList<>(startAt.getPieces());
            startAt.clearPieces();
            for (Piece p : pieceList) {
                current.addPiece(p);
                p.setStartCell(current);
            }

            return false;
        }
    }

    public int[] getMovableCells(Cell cell, int[] moves) {
        int[] movableCellsId = new int[moves.length];

        for(int i = 0; i<moves.length; i++) {
            Cell testCell = cell;
            if(moves[i]>0){
                for(int j = 0; j<moves[i]; j++) {
                    if (testCell.getType().equals("대기")) {
                        testCell = getCell(0);
                    }
                    List<Cell> nextList = testCell.getNextCells();
                    if(testCell.getType().equals("갈림길")) {
                        if(testCell == cell) {
                            for (Cell celln : nextList) {
                                if (celln.getType().equals("지름길")) {
                                    testCell = celln;
                                    break;
                                }
                            }
                        }
                        else {
                            for (Cell celln : nextList) {
                                if (celln.getType().equals("일반")) {
                                    testCell = celln;
                                    break;
                                }
                            }
                        }
                    }
                    else if(testCell.getType().equals("교차로")) {
                        if(testCell == cell) {
                            for (Cell celln : nextList) {
                                if(celln.getLineNum() == shape) {
                                    testCell = celln;
                                    break;
                                }
                            }
                        } else {
                            if(testCell.getLineNum() == shape-2) {
                                for (Cell celln : nextList) {
                                    if(celln.getLineNum() == shape) {
                                        testCell = celln;
                                        break;
                                    }
                                }
                            } else {
                                for (Cell celln : nextList) {
                                    if(celln.getLineNum() == shape-1) {
                                        testCell = celln;
                                        break;
                                    }
                                }
                            }
                        }

                    } else {
                        testCell = nextList.getFirst();
                    }

                    if (testCell.getType().equals("완주")) {
                        movableCellsId[i] = -1;
                        break;
                    }

                    movableCellsId[i] = testCell.getId();
                }

            }
            else {
                if(testCell.getPreviousCells().size() == 1) {
                    testCell = testCell.getPreviousCells().getFirst();
                    movableCellsId[i] = testCell.getId();
                }
                else if(testCell.getPreviousCells().size() >= 2) {
                    testCell = testCell.getPreviousCellWithMinId();
                    movableCellsId[i] = testCell.getId();
                }
            }
        }
        return movableCellsId;
    }

    public boolean movePiecePositive(Piece piece, int move) {
        Cell current = piece.getStartCell();
        Cell startAt = piece.getStartCell();

        piece.setPreviousLine(startAt.getLineNum());

        // 꺼내는 경우 처리
        if(current.getType().equals("대기")){
            current = getCell(0);
        }

        // 이동 실행
        for(int i = 0; i < move; i++) {
            if (current.getType().equals("완주")) {
                for(Piece p : startAt.getPieces()) {
                    p.setFinished(true);
                }
                startAt.clearPieces();
                return false;
            }
            List<Cell> nextList = current.getNextCells();
            if (nextList.isEmpty()) break;

            if(current.getType().equals("갈림길")) {
                if(current == startAt) {
                    for (Cell cell : nextList) {
                        if (cell.getType().equals("지름길")) {
                            current = cell;
                            break;
                        }
                    }
                } else {
                    for (Cell cell : nextList) {
                        if (cell.getType().equals("일반")) {
                            current = cell;
                            break;
                        }
                    }
                }
            }
            else if(current.getType().equals("교차로")) {
                if(current == startAt) {
                    for (Cell cell : nextList) {
                        System.out.println(cell.getLineNum()+" : "+shape);
                        if (cell.getLineNum() == shape) {
                            System.out.println(cell.getId());
                            current = cell;
                            break;
                        }
                    }
                } else {
                    if(piece.getPreviousLine() == shape-2) {
                        for (Cell cell : nextList) {
                            if (cell.getLineNum() == shape) {
                                current = cell;
                                break;
                            }
                        }
                    } else {
                        for (Cell cell : nextList) {
                            if (cell.getLineNum() == shape-1) {
                                current = cell;
                                break;
                            }
                        }
                    }
                }
            } else {
                current = nextList.getFirst();
            }
            System.out.println(current.getType()+"/"+current.getId());
        }

        // 도착점에 정확히 도달하거나 도착 전인 경우 - 일반 이동 처리
        boolean checkCol = updatePieceLocation(current, startAt, piece);
        piece.setStartCell(current);
        for(Piece p : startAt.getPieces()) {
            p.setStartCell(current);
        }
        startAt.clearPieces();
        return checkCol;
    }

    public boolean movePieceNegative(Piece piece) {
        Cell current = piece.getStartCell();
        Cell startAt = piece.getStartCell();

        // 대기상태인 말들은 빽도 불가능하게 설정
        if(current.getType().equals("대기")){
            System.out.println("윷판에 놓여있지 않은 말은 백도를 할 수 없습니다.");
            return false;
        }

        if(current.getPreviousCells().size() == 1) {
            current = current.getPreviousCells().getFirst();
            piece.setStartCell(current);

            return updatePieceLocation(current, startAt, piece);
        }
        else if(current.getPreviousCells().size() >= 2) {
            current = current.getPreviousCellWithMinId();
            piece.setStartCell(current);

            return updatePieceLocation(current, startAt, piece);
        }
        else {
            return false;
            // 백도 불가
        }
        //System.out.println(current.getType()+"/"+current.getId());
    }

    public Cell getCell(int i) {
        return cells.get(i);
    }

    public Map<Integer, Cell> getCells() {
        return cells;
    }

    // Cell id에 해당하는 객체 찾기
    public Cell getCellById(int id){
        for (Cell cell : cells.values()) {
            if (cell.getId() == id) {
                return cell;
            }
        }

        return null;
    }
}
