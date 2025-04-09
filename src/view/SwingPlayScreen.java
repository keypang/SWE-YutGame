package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.BoardType;
import model.YutResult;

import java.util.ArrayList;

public class SwingPlayScreen extends JFrame implements GamePlayView{
    private int playerCount;
    private int pieceCount;
    private BoardType boardType;
    private JButton rollButton;
    private JButton testRollButton;
    private ArrayList<ImageIcon> playerIcons = new ArrayList<>();


    private JLabel yutResultLabel;
    // 추가
    private ThrowButtonListener throwButtonListener;


    public SwingPlayScreen(int playerCount, int pieceCount, BoardType boardType) {
        super("게임 화면");
        this.playerCount = playerCount;
        this.pieceCount = pieceCount;
        this.boardType = boardType;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(null);
        getContentPane().setBackground(new Color(255, 245, 230));


        //판 생성 및 배치
        ImageIcon boardIcon = new ImageIcon(getClass().getResource("/view/images/" + boardType.toString() + ".png"));
        JLabel boardLabel = new JLabel(boardIcon);

        if(boardType == BoardType.SQUARE) {
            boardLabel.setBounds(-155,-155,1000,1000);
        }
        else if(boardType == BoardType.PENTAGON) {
            boardLabel.setBounds(-210,-185,1150,1150);
        }
        else if(boardType == BoardType.HEXAGON){
            boardLabel.setBounds(-190,-155,1000,1000);
        }

        getContentPane().add(boardLabel);

        // 윷 결과 라벨 추가
        yutResultLabel = new JLabel("윷 결과: ");
        yutResultLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        yutResultLabel.setBounds(730, 560, 300, 40);
        add(yutResultLabel);



        //윷 던지기 버튼
        rollButton = new JButton("윷 던지기");
        rollButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        rollButton.setBackground(new Color(120, 200, 120));
        rollButton.setForeground(Color.WHITE);
        rollButton.setFocusPainted(false);

        rollButton.setBounds(950,610,300,60);

        add(rollButton);

        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (throwButtonListener != null) {
                    YutResult yutResult = throwButtonListener.onThrowButtonClicked();
                    displayYutResult(yutResult.getMove());
                }
            }
        });


        //지정 윷 던지기 버튼
        testRollButton = new JButton("지정윷 던지기");
        testRollButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        testRollButton.setBackground(new Color(120, 200, 120));
        testRollButton.setForeground(Color.WHITE);
        testRollButton.setFocusPainted(false);

        testRollButton.setBounds(730,610,200,60);

        add(testRollButton);

        //클릭 시 좌표 출력
        getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("클릭한 위치: (" + x + ", " + y + ")");
            }
        });

        //플레이어 정보 최초 초기화
        ArrayList<JLabel> playerLabels = new ArrayList();
        for(int i = 0; i < playerCount; i++) {
            playerLabels.add(new JLabel("PLAYER" + String.valueOf(i+1)));
            playerLabels.get(i).setFont(new Font("SansSerif", Font.BOLD, 24));
            playerLabels.get(i).setBounds(830,90+i*140,200,30);
            add(playerLabels.get(i));
        }


        for(int j = 0; j < playerCount; j++) {
            playerIcons.add(new ImageIcon(getClass().getResource("/view/images/" + "플레이어" + String.valueOf(j+1) + "말" + ".png")));
        } //플레이어 수에 맞게 ICON ArrayList를 미리 생성 이후 piece 수에 맞게 화면에 초기화
        //이후 piece 객체를 받아와 그 정보만큼 해당 ICON을 배치 -> 화면 갱신하는 renewal 매서드 필요

        //ICON 배치 테스트
        ImageIcon piece1Icon = new ImageIcon(getClass().getResource("/view/images/" + "플레이어1말" + ".png"));
        JLabel piece1Label = new JLabel(piece1Icon);

        piece1Label.setBounds(950,40,100,128);
        add(piece1Label);

        //턴 정보 초기화
        ImageIcon turnDirectionIcon = new ImageIcon(getClass().getResource("/view/images/" + "턴화살표" + ".png"));
        JLabel turnDirectionLabel = new JLabel(turnDirectionIcon);
        turnDirectionLabel.setBounds(785,80,50,50); //Y좌표 140씩 추가하면 다음 플레이어에게 배치
        add(turnDirectionLabel);

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    // 윷 결과를 화면에 표시하는 메서드
    private void displayYutResult(int result) {
        String resultText;

        switch (result) {
            case -1:
                resultText = "백도";
                break;
            case 1:
                resultText = "도";
                break;
            case 2:
                resultText = "개";
                break;
            case 3:
                resultText = "걸";
                break;
            case 4:
                resultText = "윷";
                break;
            case 5:
                resultText = "모";
                break;
            default:
                resultText = "알 수 없음";
                break;
        }

        yutResultLabel.setText("윷 결과: " + resultText);

    }
    public void renewalFrame() {
        //정보 받아와 갱신하는 작업
        revalidate();
        repaint();
    }


    @Override
    public void setThrowButtonListener(ThrowButtonListener throwButtonListener) {
        this.throwButtonListener = throwButtonListener;
    }
}
