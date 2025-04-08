package model;

import java.util.*;

public class Board {
    private Map<Integer, Cell> cells = new HashMap<>();
    private Map<Integer, Player> players = new HashMap<>();

    public Board(ArrayList<Player> players, BoardType boardType) {
        if(boardType == BoardType.SQUARE){

        }
        else if(boardType == BoardType.PENTAGON){

        }
        else if(boardType == BoardType.HEXAGON){
            generatePentagonBoard();
        }
        initBoard(players);
    }

    private void generatePentagonBoard() {
        cells.clear(); // 재시작용

        List<Integer> inRoadList = Arrays.asList(5, 10, 15, 20); //들어갈 수 갈림길 관리
        List<Integer> outRoadList = Arrays.asList(25, 30);

        //외부 셀 생성
        for(int i = 1; i<30; i++) {
            if(inRoadList.contains(i)) {
                Cell cell = new Cell(i, "갈림길", i/5);
                cells.put(i, cell);
            }
            else {
                Cell cell = new Cell(i, "일반", 0);
                cells.put(i, cell);
            }
        }

        //시작 셀
        Cell cell = new Cell(0,"출발",0);
        cells.put(0,cell);

        //도착 셀
        cell = new Cell(30,"도착",0);
        cells.put(30,cell);

        //외부 셀 연결
        cells.get(0).addNextCell(cells.get(1));
        for(int i = 1; i<30; i++) {
            cells.get(i).addPreviousCell(cells.get(i-1));
            cells.get(i).addNextCell(cells.get(i+1));
        }
        cells.get(30).addPreviousCell(cells.get(29));

        //교차로 셀
        cell = new Cell(1000, "갈림길",100);
        cells.put(1000,cell);

        //지름길
        for(int i = 1; i<7; i++) {
            cell = new Cell(i*50,"지름길",i);
            cells.put(i*50,cell);
            cell = new Cell(i*55,"지름길",i);
            cells.put(i*55,cell);
        }

        //
        cells.get(275).setType("일반");
        cells.get(250).setType("일반");
        //갈림길 진입점
        for(int in: inRoadList) {
            cells.get(in).addNextCell(cells.get(in*10));
            cells.get(in*10).addPreviousCell(cells.get(in));
            cells.get(in*10).addNextCell(cells.get(in*11));
            cells.get(in*11).addPreviousCell(cells.get(in*10));
            cells.get(in*11).addNextCell(cells.get(1000));
        }

        //나가는 길
        for(int out: outRoadList) {
            cells.get(1000).addNextCell(cells.get(out*11));
            cells.get(out*11).addNextCell(cells.get(out*10));
            cells.get(out*10).addNextCell(cells.get(out));
            cells.get(out).addPreviousCell(cells.get(out*10));
            cells.get(out*10).addPreviousCell(cells.get(out*11));
        }
    }

    private void initBoard(ArrayList<Player> players) {
        for(Player player : players) {
            for(int i = 0; i<player.getPieceNum(); i++) {
                player.getPieces(i).setStartCell(cells.get(0));
            }
        }
    }

    public void movePiecePostive(Piece piece, int move) {
        Cell current = piece.getStartCell();
        Cell startAt = piece.getStartCell();
        for(int i = 0; i < move; i++) {
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
                }
                else {
                    for (Cell cell : nextList) {
                        if (cell.getType().equals("일반")) {
                            current = cell;
                            break;
                        }
                    }
                }
            }
            else {
                current = nextList.getFirst();
            }
            System.out.println(current.getType()+"/"+current.getId());
        }
        piece.setStartCell(current);
    }

    private void movePieceNegitive(Piece piece, int move) {

    }

    public Cell getCell(int i) {
        return cells.get(i);
    }
}
