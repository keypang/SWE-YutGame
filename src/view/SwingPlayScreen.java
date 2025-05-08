    package view;
    
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import model.BoardType;
    import model.Yut;
    import model.YutResult;
    import model.PositionDTO;
    
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Arrays;
    
    public class SwingPlayScreen extends JFrame implements GamePlayView {
    
        private int playerCount;
        private int pieceCount;
        private BoardType boardType;
        private ArrayList<ImageIcon> playerIcons = new ArrayList<>();
    
        //좌표와 Cell 매핑 (키 : cell_numer_point(X,Y))
        private Map<Integer, Point> squarePositionMap = new HashMap<>();
        private Map<Integer, Point> pentagonPositionMap = new HashMap<>();
        private Map<Integer, Point> hexagonPositionMap = new HashMap<>();
    
        // 버튼
        private JButton rollButton;
        private JButton testRollButton;
    
        // 필드 추가
        private FixedYutButtonListener fixedYutButtonListener;
        private ThrowButtonListener throwButtonListener;
        private GameEndListener gameEndListener;
        private TakeOutButtonListener takeOutButtonListener;
        private PieceSelectionListener pieceSelectionListener;
        private CellSelectionListener cellSelectionListener;
    
        // 라벨
        private JLabel yutImageLabel;
        private JLabel yutResultLabel;
        private JLabel currentPlayerLabel; // 현재 플레이어 표시 라벨
        private JLabel statusMessageLabel; // 게임 상태 메시지 라벨
        private List<JLabel> yutResultLabels = new ArrayList<>();
        private ArrayList<JLabel> pieceLabels = new ArrayList<>(); // 말 아이콘 라벨들
        private JLabel turnArrowLabel; // 턴 화살표 라벨
        private List<JLabel> movablePoints = new ArrayList<>();
    
        private JPanel yutResultsPanel;// 윷 결과 리스트를 표시할 패널 추가
    
        private int currentPlayerIndex = 0; // 현재 플레이어 인덱스
        private int selectedPieceIndex = 0;
    
        // 플레이어 패널에 있는 말 이미지 (키: playerID_pieceID)
        private Map<String, JLabel> playerPiecesMap = new HashMap<>();
    
        // 말 선택 가능 여부 변수
        //for test true로 변경 후 테스트
        private boolean waitingPieceSelection = false;


        private Map<Integer, JPanel> playerPiecePanels = new HashMap<>();
        private List<JLabel> stackCountLabels = new ArrayList<>();



        public SwingPlayScreen(int playerCount, int pieceCount, BoardType boardType) {
            super("게임 화면");
            this.playerCount = playerCount;
            this.pieceCount = pieceCount;
            this.boardType = boardType;
            initUI();
        }
    
        private void initUI() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1280, 800); // 화면 높이 증가
            setResizable(false);
    
            // 전체 패널 생성 (메인 컨테이너)
            JPanel mainPanel = new JPanel(new BorderLayout(10, 0));
            mainPanel.setBackground(new Color(255, 245, 230));
    
            // 게임 판 패널 (왼쪽 영역)
            JPanel boardPanel = createBoardPanel();
            boardPanel.setPreferredSize(new Dimension(700, 720)); // 윷 판이 온전히 보이도록 충분한 공간 확보
            mainPanel.add(boardPanel, BorderLayout.WEST);
    
            // 게임 정보 및 컨트롤 패널 (오른쪽 영역)
            JPanel controlPanel = createControlPanel();
            mainPanel.add(controlPanel, BorderLayout.CENTER);
    
            // 메인 패널을 프레임에 추가
            setContentPane(mainPanel);
    
            //클릭 시 좌표 출력

            getContentPane().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    System.out.println("클릭한 위치: (" + x + ", " + y + ")");
                }
            });
    
            //좌표 Hash map 초기화
            if(boardType == BoardType.SQUARE) initializeSquarePositions();
            else if(boardType == BoardType.PENTAGON) initializePentagonPositions();
            else initializeHexagonPositions();
    
            setLocationRelativeTo(null);
            setVisible(true);

        }
    
        // 게임 판 패널 생성
        private JPanel createBoardPanel() {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(700, 720));
            panel.setLayout(null); // null 레이아웃 사용 (절대 위치)
            panel.setBackground(new Color(255, 245, 230));
    
            // 윷놀이 판 이미지 추가
            ImageIcon boardIcon = new ImageIcon(getClass().getResource("/view/images/" + boardType.toString() + ".png"));
    
            // 이미지 크기 조정 (윷판이 완전히 보이도록)
            Image img = boardIcon.getImage();
            Image resizedImg;
    
            // 보드 타입에 따라 크기 조정
            if(boardType == BoardType.SQUARE) {
                resizedImg = img.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
            }
            else if(boardType == BoardType.PENTAGON) {
                resizedImg = img.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
            }
            else { // HEXAGON
                resizedImg = img.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
            }
    
            boardIcon = new ImageIcon(resizedImg);
            JLabel boardLabel = new JLabel(boardIcon);
            boardLabel.setBounds(50, 50, 600, 600); // 절대 위치 지정
    
            // 보드 레이블 추가
            panel.add(boardLabel);

            // 완주 셀 추가
            ImageIcon goalCell = new ImageIcon(getClass().getResource("/view/images/" + "완주셀" + ".png"));
            JLabel goalCellLabel = new JLabel(goalCell);

            if(boardType == BoardType.SQUARE) {
                goalCellLabel.setBounds(658, 570, 43, 43);
            }
            else if(boardType == BoardType.PENTAGON) {
                goalCellLabel.setBounds(570, 580, 43, 43);
            }
            else{
                goalCellLabel.setBounds(600, 445, 43, 43);
            }

            panel.add(goalCellLabel);
    
            return panel;
        }
    
        // 게임 정보 및 컨트롤 패널 생성
        private JPanel createControlPanel() {
            JPanel panel = new JPanel();
            panel.setBackground(new Color(255, 245, 230));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
    
            // 전체 패널에 GridBagLayout 적용
            panel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
    
            // 플레이어 정보 패널
            JPanel playerInfoPanel = createPlayerInfoPanel();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weighty = 0.5;
            panel.add(playerInfoPanel, gbc);
    
            // 현재 플레이어 정보 패널
            JPanel currentPlayerPanel = new JPanel();
            currentPlayerPanel.setLayout(new BoxLayout(currentPlayerPanel, BoxLayout.Y_AXIS));
            currentPlayerPanel.setBackground(new Color(255, 245, 230));
            currentPlayerPanel.setBorder(BorderFactory.createTitledBorder("게임 상태"));
    
            // 현재 플레이어 표시
            currentPlayerLabel = new JLabel("현재 플레이어: 1");
            currentPlayerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            currentPlayerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
            // 게임 상태 메시지
            statusMessageLabel = new JLabel("게임 시작! 윷을 던져주세요.");
            statusMessageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            statusMessageLabel.setForeground(new Color(0, 100, 0));
            statusMessageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
            currentPlayerPanel.add(currentPlayerLabel);
            currentPlayerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            currentPlayerPanel.add(statusMessageLabel);
    
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weighty = 0;
            panel.add(currentPlayerPanel, gbc);
    
            // 윷 결과 표시 패널
            JPanel yutResultPanel = createYutResultPanel();
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weighty = 0.3;
            panel.add(yutResultPanel, gbc);
    
            // 버튼 패널
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
            buttonPanel.setBackground(new Color(255, 245, 230));
    
            // 지정 윷 던지기 버튼
            testRollButton = new JButton("지정윷 던지기");
            testRollButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            testRollButton.setBackground(new Color(120, 200, 120));
            testRollButton.setForeground(Color.WHITE);
            testRollButton.setFocusPainted(false);
            testRollButton.setPreferredSize(new Dimension(150, 60));
    
            testRollButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayResultSelect();
                }
            });
    
            // 윷 던지기 버튼
            rollButton = new JButton("윷 던지기");
            rollButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            rollButton.setBackground(new Color(120, 200, 120));
            rollButton.setForeground(Color.WHITE);
            rollButton.setFocusPainted(false);
            rollButton.setPreferredSize(new Dimension(150, 60));
    
            rollButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (throwButtonListener != null) {
                        YutResult yutResult = throwButtonListener.onThrowButtonClicked();
                        displaySingleYutResult(yutResult);
                    }
                }
            });
    
            buttonPanel.add(testRollButton);
            buttonPanel.add(rollButton);
    
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(buttonPanel, gbc);
    
            // 게임 종료 테스트 버튼 (테스트용)
            JButton testEndGameButton = new JButton("게임 종료 테스트");
            testEndGameButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            testEndGameButton.setBackground(new Color(255, 100, 100));
            testEndGameButton.setForeground(Color.WHITE);
            testEndGameButton.setFocusPainted(false);
            testEndGameButton.setPreferredSize(new Dimension(200, 40));
    
            testEndGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 게임 종료 다이얼로그 테스트 (현재 플레이어를 승자로 설정)
                    showGameEndDialog(currentPlayerIndex + 1);
                }
            });
    
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.weighty = 0;
            gbc.insets = new Insets(20, 5, 5, 5); // 위쪽 여백 추가
            panel.add(testEndGameButton, gbc);
    
            return panel;
        }
    
        // 플레이어 정보 패널 생성
        private JPanel createPlayerInfoPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(playerCount, 1, 0, 10));
            panel.setBackground(new Color(255, 245, 230));
            panel.setBorder(BorderFactory.createTitledBorder("플레이어 정보"));
    
            // 턴 화살표 이미지 로드
            ImageIcon turnArrowIcon = new ImageIcon(getClass().getResource("/view/images/턴화살표.png"));
            // 이미지 크기 조정
            Image img = turnArrowIcon.getImage();
            Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            turnArrowIcon = new ImageIcon(resizedImg);
    
            // 각 플레이어 정보 행 패널 생성
            for (int i = 0; i < playerCount; i++) {
                final int playerId = i + 1;
    
                // 플레이어 패널 (전체)
                JPanel playerPanel = new JPanel();
                playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
                playerPanel.setBackground(new Color(255, 245, 230));
    
                // 플레이어 정보 행 (이름과 화살표)
                JPanel playerInfoRow = new JPanel();
                playerInfoRow.setLayout(new BoxLayout(playerInfoRow, BoxLayout.X_AXIS));
                playerInfoRow.setBackground(new Color(255, 245, 230));
                playerInfoRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
                // 현재 턴 표시 화살표 (첫 번째 플레이어에게만 표시)
                JLabel arrowLabel = new JLabel(i == 0 ? turnArrowIcon : new ImageIcon());
                arrowLabel.setPreferredSize(new Dimension(30, 30));
                playerInfoRow.add(arrowLabel);
                playerInfoRow.add(Box.createRigidArea(new Dimension(10, 0)));
    
                if (i == 0) {
                    turnArrowLabel = arrowLabel; // 화살표 라벨 저장 (나중에 위치 업데이트 위해)
                }
    
                // 플레이어 이름
                JLabel nameLabel = new JLabel("PLAYER" + playerId);
                nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
                nameLabel.setPreferredSize(new Dimension(100, 30));
                playerInfoRow.add(nameLabel);
                playerInfoRow.add(Box.createHorizontalGlue()); // 여백 추가
    
                // 플레이어 말 아이콘 로드
                ImageIcon pieceIcon = new ImageIcon(getClass().getResource("/view/images/플레이어" + playerId + "말.png"));
    
                // 말 아이콘 크기 조정
                Image pieceImg = pieceIcon.getImage();
                Image resizedPieceImg = pieceImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                pieceIcon = new ImageIcon(resizedPieceImg);
    
                playerIcons.add(pieceIcon);
    
                // 말 아이콘 표시
                JLabel pieceLabel = new JLabel(pieceIcon);
                pieceLabel.setPreferredSize(new Dimension(40, 40));
                pieceLabels.add(pieceLabel);
                playerInfoRow.add(pieceLabel);
    
                // 플레이어 패널에 정보 행 추가
                playerPanel.add(playerInfoRow);


                // 말 행 패널 (말들과 꺼내기 버튼)
                JPanel piecesRow = new JPanel();
                piecesRow.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
                piecesRow.setBackground(new Color(255, 245, 230));
    
                // 각 플레이어의 말을 패널에 추가
                for (int j = 0; j < pieceCount; j++) {
                    final int pieceId = j;
    
                    // 말 이미지 생성
                    JLabel pieceImageLabel = new JLabel(pieceIcon);
                    pieceImageLabel.setName("piece_" + playerId + "_" + pieceId);
                    pieceImageLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                    pieceImageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
                    pieceImageLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if(!waitingPieceSelection){
                                JOptionPane.showMessageDialog(SwingPlayScreen.this, "윷을 던져주세요!");
                                return;
                            }
                            //for test
                            //setCurrentPlayerIndex(2);

                            if(playerId != currentPlayerIndex + 1){
                                JOptionPane.showMessageDialog(SwingPlayScreen.this, "플레이어의 말이 아닙니다!");
                                return;
                            }
                            waitingPieceSelection = false;
                            //for test
                            //System.out.println("선택된 말: playerId=" + playerId + ", pieceId=" + pieceId);
                            onPieceSelected(pieceId);
    
                        }
                    });
    
                    // 말 이미지를 패널에 추가
                    piecesRow.add(pieceImageLabel);
    
                    // 말 라벨 저장 (나중에 참조하기 위해)
                    playerPiecesMap.put(playerId + "_" + pieceId, pieceImageLabel);

                    playerPiecePanels.put(playerId, piecesRow);
                }

                /*
                // 말 꺼내기 버튼
                JButton takeOutButton = new JButton("말 꺼내기");
                takeOutButton.setFont(new Font("SansSerif", Font.BOLD, 12));
                takeOutButton.setBackground(new Color(200, 200, 255));
    
                piecesRow.add(takeOutButton);

    
                takeOutButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(takeOutButtonListener != null){
                            List<PositionDTO> currentPositions = takeOutButtonListener.onTakeOutButtonClicked();
                            takeOutPiece(playerId, currentPositions);
                        }
                    }
                });
                */
    
                // 플레이어 패널에 말 행 추가
                playerPanel.add(piecesRow);
    
                // 전체 패널에 플레이어 패널 추가
                panel.add(playerPanel);
            }
    
            return panel;
        }
    
        // 윷 결과 표시 패널 생성
        private JPanel createYutResultPanel() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(new Color(255, 245, 230));
            panel.setBorder(BorderFactory.createTitledBorder("윷 결과"));
            panel.setPreferredSize(new Dimension(300, 150)); // 크기 지정
    
            // 최근 윷 결과 표시 영역 (왼쪽)
            JPanel currentResultPanel = new JPanel();
            currentResultPanel.setLayout(new BoxLayout(currentResultPanel, BoxLayout.Y_AXIS));
            currentResultPanel.setBackground(new Color(255, 245, 230));
            currentResultPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            currentResultPanel.setPreferredSize(new Dimension(120, 120)); // 크기 지정
    
            yutResultLabel = new JLabel("윷 결과: ");
            yutResultLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            yutResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currentResultPanel.add(yutResultLabel);
    
            // 이미지와 라벨 사이 간격 추가
            currentResultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    
            // 이미지 라벨 생성 및 중앙 정렬
            yutImageLabel = new JLabel();
            yutImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currentResultPanel.add(yutImageLabel);
    
            panel.add(currentResultPanel, BorderLayout.WEST);
    
            // 윷 결과 리스트 표시 영역 (오른쪽)
            JPanel resultsListPanel = new JPanel(new BorderLayout());
            resultsListPanel.setBackground(new Color(255, 245, 230));
            resultsListPanel.setBorder(BorderFactory.createTitledBorder("가능한 이동"));
            resultsListPanel.setPreferredSize(new Dimension(150, 120)); // 크기 지정
    
            // 결과 리스트를 스크롤 패널에 추가
            yutResultsPanel = new JPanel();
            yutResultsPanel.setLayout(new BoxLayout(yutResultsPanel, BoxLayout.Y_AXIS));
            yutResultsPanel.setBackground(new Color(255, 245, 230));
    
            JScrollPane scrollPane = new JScrollPane(yutResultsPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
            resultsListPanel.add(scrollPane, BorderLayout.CENTER);
    
            panel.add(resultsListPanel, BorderLayout.CENTER);
    
            return panel;
        }
    
        //지정 윷을 정하는 화면 표시하는 메서드
        private void displayResultSelect() {
            // 윷 선택 리스너 생성
            SelectYutResultScreen.YutSelectListener listener = result -> {
                if (fixedYutButtonListener != null) {
                    // 선택된 윷 결과를 컨트롤러에 전달
                    YutResult yutResult = fixedYutButtonListener.onFixedYutSelected(result);
                    // 던진 윷 결과 화면에 표시
                    displaySingleYutResult(yutResult);
                }
            };
    
            // 선택 화면 생성 및 리스너 전달
            String[] yutResult = {"백도", "도","개", "걸", "윷", "모"};
            new SelectYutResultScreen(listener, yutResult);
        }
    
        // 단일 윷 결과를 표시 (팝업용)
        private void displaySingleYutResult(YutResult result) {
            String resultText = result.name();
            String imageName = "";
    
            switch (result.getMove()) {
                case -1:
                    imageName = "빽도";
                    break;
                case 1:
                    imageName = "도";
                    break;
                case 2:
                    imageName = "개";
                    break;
                case 3:
                    imageName = "걸";
                    break;
                case 4:
                    imageName = "윷";
                    break;
                case 5:
                    imageName = "모";
                    break;
                default:
                    imageName = null;
                    break;
            }
    
            yutResultLabel.setText("윷 결과: " + resultText);
    
            // 윷 이미지 표시
            if (imageName != null) {
                ImageIcon yutIcon = new ImageIcon(getClass().getResource("/view/images/" + imageName + ".png"));
    
                // 이미지 크기 조정
                Image img = yutIcon.getImage();
                Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                yutIcon = new ImageIcon(resizedImg);
    
                yutImageLabel.setIcon(yutIcon);
    
                // 팝업 창에 이미지 표시
                JDialog resultDialog = new JDialog(this, "윷 결과: " + resultText, true);
                resultDialog.setLayout(new BorderLayout());
    
                ImageIcon popupYutIcon = new ImageIcon(getClass().getResource("/view/images/" + imageName + ".png"));
                Image popupImg = popupYutIcon.getImage();
                Image resizedPopupImg = popupImg.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                popupYutIcon = new ImageIcon(resizedPopupImg);
    
                JLabel imageLabel = new JLabel(popupYutIcon);
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                resultDialog.add(imageLabel, BorderLayout.CENTER);
    
                JButton confirmButton = new JButton("확인");
                confirmButton.addActionListener(e -> resultDialog.dispose());
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(confirmButton);
                resultDialog.add(buttonPanel, BorderLayout.SOUTH);
    
                resultDialog.setSize(400, 450);
                resultDialog.setLocationRelativeTo(this);
    
                // 팝업 창이 3초후에 닫히도록 타이머 설정하기
                Timer timer = new Timer(2000, e -> resultDialog.dispose());
                timer.setRepeats(false);
                timer.start();
    
                resultDialog.setVisible(true);
            }
        }

        // 윷 선택하는 패널(팝업) 생성 메서드
        public String showYutSelectPanel(YutResult[] yutResult) {
            final String[] selectedResult = {null};

            String[] yutResultStrings = Arrays.stream(yutResult).map(Enum::name).toArray(String[]::new);

            // 윷 선택창 생성
            JDialog dialog = new JDialog(this, "소모할 윷을 선택하세요!", true);
            dialog.setSize(800, 200);
            dialog.setLayout(new GridBagLayout());
            dialog.getContentPane().setBackground(new Color(255, 245, 230));
            dialog.setLocationRelativeTo(this); // 화면 중앙 정렬

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 10, 10);

            for (int i = 0; i < yutResultStrings.length; i++) {
                final String result = yutResultStrings[i];

                JButton button = new JButton(result);
                button.setFont(new Font("SansSerif", Font.BOLD, 18));
                button.setBackground(new Color(120, 200, 120));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setPreferredSize(new Dimension(100, 70));

                button.addActionListener(e -> {
                    selectedResult[0] = result;
                    dialog.dispose();
                });

                gbc.gridx = i;
                gbc.gridy = 0;
                dialog.add(button, gbc);
            }

            dialog.setVisible(true);

            // 선택된 윷 결과 반환
            return selectedResult[0];
        }

    
        // 말 선택 시 처리 메서드
        private void onPieceSelected(int pieceId) {
            //for test == null로 변경 후 test
            Map<Integer, Integer> availableCells = null;
            if (pieceSelectionListener != null) {
                selectedPieceIndex = pieceId;

                System.out.println("말 선택한 후 전달될 piece ID :" + pieceId);
                //controller에게 pieceId 전달
                availableCells = pieceSelectionListener.onPieceSelected(pieceId);

                JLabel selectedPiece = playerPiecesMap.get(currentPlayerIndex + 1 + "_" + pieceId);
                if (selectedPiece != null) {
                    selectedPiece.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                }
            }
            repaintAllPieces();
            showMovablePoints(availableCells);
        }
    
        // 말 옮기는 지점 표시 메서드
        public void showMovablePoints(Map<Integer, Integer> availableCells) {
            JPanel boardPanel = getBoardPanel();
            for(JLabel label : movablePoints) {
                boardPanel.remove(label);
            }
            movablePoints.clear();

            if(availableCells == null || availableCells.isEmpty()){
                JOptionPane.showMessageDialog(this, "이동 가능한 위치가 없습니다.");
                return;
            }

            for(Map.Entry<Integer, Integer> entry : availableCells.entrySet()){
                int cellId = entry.getKey();
                Point target;

                // 완주 처리를 위한 특수 케이스 처리
                if(boardType == BoardType.SQUARE){
                    if(cellId == -1) {
                        // 도착 셀(완주셀) 위치 사용
                        target = squarePositionMap.get(-1);
                    } else {
                        target = squarePositionMap.get(cellId);
                    }
                }
                else if(boardType == BoardType.PENTAGON){
                    if(cellId == -1) {
                        // 도착 셀(완주셀) 위치 사용
                        target = pentagonPositionMap.get(-1);
                    } else {
                        target = pentagonPositionMap.get(cellId);
                    }
                }
                else{
                    if(cellId == -1) {
                        // 도착 셀(완주셀) 위치 사용
                        target = hexagonPositionMap.get(-1);
                    } else {
                        target = hexagonPositionMap.get(cellId);
                    }
                }

                if(target == null){
                    System.out.println("알 수 없는 셀 ID: " + cellId);
                    continue;
                }

                ImageIcon movePointIcon = new ImageIcon(getClass().getResource("/view/images/이동가능점.png"));
                JLabel movablePoint = new JLabel(movePointIcon);

                if(boardType == BoardType.SQUARE){
                    movePointIcon = new ImageIcon(getClass().getResource("/view/images/이동가능점.png"));
                    movablePoint = new JLabel(movePointIcon);
                    movablePoint.setBounds(target.x-2, target.y-25, 30, 30);
                }
                else if(boardType == BoardType.PENTAGON){
                    movePointIcon = new ImageIcon(getClass().getResource("/view/images/이동가능점오각형.png"));
                    movablePoint = new JLabel(movePointIcon);
                    movablePoint.setBounds(target.x-13, target.y-16, 30, 30);
                }
                else {
                    movePointIcon = new ImageIcon(getClass().getResource("/view/images/이동가능점육각형.png"));
                    movablePoint = new JLabel(movePointIcon);
                    movablePoint.setBounds(target.x-7, target.y-22, 30, 30);
                }

                movablePoint.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                movablePoints.add(movablePoint);

                final int finalCellId = cellId; // 클로저에서 사용하기 위해 final 변수로 캡처
                movablePoint.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("말 옮기기 전 전달될 cell ID :" + finalCellId);
                        //선택된 cellID controller에게 전달
                        if(cellSelectionListener != null){
                            cellSelectionListener.onCellSelected(finalCellId);
                        }

                        JPanel boardPanel = getBoardPanel();
                        for(JLabel targetLabel : movablePoints){
                            boardPanel.remove(targetLabel);
                        }
                        movablePoints.clear();
                        boardPanel.repaint();
                    }
                });

                boardPanel.add(movablePoint);
                boardPanel.setComponentZOrder(movablePoint, 0);
                boardPanel.repaint();
            }
        }
    
        // 말 꺼내기 시 메서드(사용 안 함)
        public void takeOutPiece(int playerId, List<PositionDTO> currentPositions) {
            boolean ownAlreadyStart = false;
            boolean canTakeOut = false;
            int pieceIdToTakeOut = -1;
    
            //현재 Position에서 꺼낼 수 있는 Piece가 있는지 확인(controller code)
            //=============================================================================
            for (PositionDTO dto : currentPositions) {
                if (dto.getPlayerId() == playerId && dto.getCellId() == 0) {
                    ownAlreadyStart = true;
                    break;
                }
            }
    
            if (ownAlreadyStart) {
                JOptionPane.showMessageDialog(this, "시작 지점에 이미 말이 있습니다!");
                return;
            }
    
            for (PositionDTO dto : currentPositions) {
                if (dto.getPlayerId() == playerId && dto.getCellId() == -1) {
                    canTakeOut = true;
                    pieceIdToTakeOut = dto.getPieceId();
                    dto.setCellId(0);  // 말이 꺼내질 때 시작 셀로 설정
                    break;
                }
            }
    
            if (!canTakeOut) {
                JOptionPane.showMessageDialog(this, "꺼낼 수 있는 말이 없습니다!");
                return;
            }
    
            //위치 테스트용 코드
            for (PositionDTO dto : currentPositions) {
                System.out.println(dto);
            }
    
            //이상부분 컨트롤러로 이전 필요======================================================================
    
            //View에서 위치 업데이트
            JLabel pieceLabel = playerPiecesMap.get(playerId + "_" + pieceIdToTakeOut);
            if(pieceLabel != null){
                Point pos = squarePositionMap.get(0);
                if(pos != null){
                    pieceLabel.setBounds(pos.x-5, pos.y-35, 40, 40);
                    getBoardPanel().add(pieceLabel);
                    getBoardPanel().setComponentZOrder(pieceLabel, 0);
                    getBoardPanel().repaint();
                }
            }
        }
    
        // 윷 결과 리스트를 화면에 표시하는 메서드 구현
        @Override
        public void displayYutResultList(List<YutResult> results) {
            // 기존 결과 라벨들 모두 제거
            yutResultsPanel.removeAll();
            yutResultLabels.clear();
    
            if (results.isEmpty()) {
                JLabel emptyLabel = new JLabel("던진 윷 없음");
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                yutResultsPanel.add(emptyLabel);
                yutResultLabels.add(emptyLabel);
            } else {
                // 각 윷 결과마다 라벨 생성하여 패널에 추가
                for (YutResult result : results) {
                    String resultText = result.name() + " (" + result.getMove() + "칸)";
                    JLabel resultLabel = new JLabel(resultText);
                    resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    resultLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    resultLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    
                    yutResultsPanel.add(resultLabel);
                    yutResultLabels.add(resultLabel);
    
                    // 구분선 추가 (마지막 항목 제외)
                    if (results.indexOf(result) < results.size() - 1) {
                        yutResultsPanel.add(new JSeparator());
                    }
                }
            }
    
            // 패널 갱신
            yutResultsPanel.revalidate();
            yutResultsPanel.repaint();
        }
    
        // 현재 플레이어 업데이트
        @Override
        public void updateCurrentPlayer(int playerNumber) {
            currentPlayerLabel.setText("현재 플레이어: " + playerNumber);
            currentPlayerIndex = playerNumber - 1;
    
            // 모든 화살표 제거
            for (int i = 0; i < playerCount; i++) {
                JPanel playerRow = (JPanel) pieceLabels.get(i).getParent();
                Component[] components = playerRow.getComponents();
                for (Component comp : components) {
                    if (comp instanceof JLabel && comp != pieceLabels.get(i) && ((JLabel) comp).getIcon() != null) {
                        ((JLabel) comp).setIcon(null);
                    }
                }
            }
    
            // 현재 플레이어에 화살표 표시
            ImageIcon turnArrowIcon = new ImageIcon(getClass().getResource("/view/images/턴화살표.png"));
            Image img = turnArrowIcon.getImage();
            Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            turnArrowIcon = new ImageIcon(resizedImg);
    
            // 플레이어 인덱스는 0부터, 플레이어 번호는 1부터 시작
            JPanel playerRow = (JPanel) pieceLabels.get(playerNumber-1).getParent();
            Component[] components = playerRow.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel && comp != pieceLabels.get(playerNumber-1)) {
                    // 화살표 위치의 라벨 찾기 (첫 번째 컴포넌트)
                    if (playerRow.getComponentZOrder(comp) == 0) {
                        ((JLabel) comp).setIcon(turnArrowIcon);
                        break;
                    }
                }
            }
    
            repaint();
        }
    
        @Override
        public void showGameEndDialog(int winnerPlayer) {
           // 게임 종료 다이얼로그 생성
            JDialog endDialog = new JDialog(this, "게임 종료", true);
            endDialog.setLayout(new BorderLayout());
            endDialog.setSize(450, 300);
            endDialog.setLocationRelativeTo(this);
    
            // 승리 메시지 패널
            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
            messagePanel.setBackground(new Color(255, 245, 230));
            messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
            // 승리자 라벨 생성
            JLabel winnerLabel = new JLabel("플레이어 " + winnerPlayer + " 승리!");
            winnerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
            // 축하 메시지
            JLabel congratsLabel = new JLabel("축하합니다!");
            congratsLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
            messagePanel.add(winnerLabel);
            messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
            messagePanel.add(congratsLabel);
    
            // 버튼 패널 - 주요 변경: 축소된 BorderLayout 사용
            JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));
            buttonPanel.setBackground(new Color(255, 245, 230));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));
    
            // 왼쪽과 오른쪽 버튼을 위한 패널
            JPanel leftRightPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            leftRightPanel.setBackground(new Color(255, 245, 230));
    
            // 새 설정으로 게임 시작 버튼
            JButton newSetupButton = new JButton("새 설정으로 게임 시작");
            newSetupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            newSetupButton.setBackground(new Color(100, 180, 220));
            newSetupButton.setForeground(Color.WHITE);
            newSetupButton.setFocusPainted(false);
            newSetupButton.addActionListener(e -> {
                if (gameEndListener != null) {
                    gameEndListener.onNewGameSetup();
                }
                endDialog.dispose();
            });
    
            // 같은 설정으로 재시작 버튼
            JButton restartButton = new JButton("같은 설정으로 재시작");
            restartButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            restartButton.setBackground(new Color(120, 200, 120));
            restartButton.setForeground(Color.WHITE);
            restartButton.setFocusPainted(false);
            restartButton.addActionListener(e -> {
                if (gameEndListener != null) {
                    gameEndListener.onRestartGame();
                }
                endDialog.dispose();
            });
    
            // 왼쪽 오른쪽 패널에 버튼 추가
            leftRightPanel.add(newSetupButton);
            leftRightPanel.add(restartButton);
    
            // 종료 버튼 - 위치 변경됨
            JButton exitButton = new JButton("종료");
            exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            exitButton.setBackground(new Color(200, 100, 100));
            exitButton.setForeground(Color.WHITE);
            exitButton.setFocusPainted(false);
            exitButton.addActionListener(e -> {
                if (gameEndListener != null) {
                    gameEndListener.onExitGame();
                }
                endDialog.dispose();
            });
    
            // 종료 버튼을 별도의 패널에 배치
            JPanel exitPanel = new JPanel();
            exitPanel.setBackground(new Color(255, 245, 230));
            exitPanel.add(exitButton);
    
            // 버튼 패널에 종료 버튼을 위쪽에, 나머지 버튼들을 아래쪽에 배치
            buttonPanel.add(exitPanel, BorderLayout.NORTH);
            buttonPanel.add(leftRightPanel, BorderLayout.CENTER);
    
            endDialog.add(messagePanel, BorderLayout.CENTER);
            endDialog.add(buttonPanel, BorderLayout.SOUTH);
    
            // 다이얼로그 표시
            endDialog.setVisible(true);
        }

        // 보드에 따른 좌표 반환 메서드
        private Point getPositionByBoardType(int cellId){
            if(boardType == BoardType.SQUARE) return squarePositionMap.get(cellId);
            else if(boardType == BoardType.PENTAGON) return pentagonPositionMap.get(cellId);
            else if(boardType == BoardType.HEXAGON) return hexagonPositionMap.get(cellId);
            else return null;
        }

        // 말 전체 다시 그리기 메서드
        public void repaintAllPieces() {
            if(takeOutButtonListener == null) return;

            List<PositionDTO> currentPositions = takeOutButtonListener.onTakeOutButtonClicked();
            JPanel boardPanel = getBoardPanel();

            // 보드에서 모든 말 제거
            for (Map.Entry<String, JLabel> entry : playerPiecesMap.entrySet()) {
                JLabel pieceLabel = entry.getValue();
                if (pieceLabel.getParent() == boardPanel) {
                    boardPanel.remove(pieceLabel);
                }
            }

            // 이전에 생성된 업힌 말 표시 라벨들 모두 제거
            for (JLabel label : stackCountLabels) {
                if (label.getParent() != null) {
                    label.getParent().remove(label);
                }
            }
            stackCountLabels.clear();

            // 먼저 대기 상태(-1)인 말들 처리 - 플레이어 패널로 돌려보내기
            for (PositionDTO dto : currentPositions) {
                if (dto.getCellId() == -1) {
                    int playerId = dto.getPlayerId();
                    int pieceId = dto.getPieceId();

                    JLabel pieceLabel = playerPiecesMap.get(playerId + "_" + pieceId);
                    if (pieceLabel != null) {
                        // 기존 부모에서 제거
                        if (pieceLabel.getParent() != null) {
                            pieceLabel.getParent().remove(pieceLabel);
                        }

                        // 플레이어 패널에 추가
                        JPanel piecePanel = playerPiecePanels.get(playerId);
                        if (piecePanel != null) {
                            piecePanel.add(pieceLabel);
                            piecePanel.revalidate();
                            piecePanel.repaint();
                        }
                    }
                }
            }

            // 이제 보드 위에 있는 말들 처리
            // 각 셀별로 말 개수 카운트
            Map<Integer, List<PositionDTO>> cellPieces = new HashMap<>();
            for (PositionDTO dto : currentPositions) {
                int cellId = dto.getCellId();
                // 완주한 말(21번 셀)과 대기 상태(-1번 셀)는 보드에 표시하지 않음

                // SQUARE 일때
                if (boardType == BoardType.SQUARE){
                    if (cellId != -1 && cellId != 21) {
                        if (!cellPieces.containsKey(cellId)) {
                            cellPieces.put(cellId, new ArrayList<>());
                        }
                        cellPieces.get(cellId).add(dto);
                    }
                    // PENTAGON 일때
                } else if (boardType == BoardType.PENTAGON) {
                    if (cellId != -1 && cellId != 26) {
                        if (!cellPieces.containsKey(cellId)) {
                            cellPieces.put(cellId, new ArrayList<>());
                        }
                        cellPieces.get(cellId).add(dto);
                    }
                    // HEXAGON 일때
                } else if (boardType == BoardType.HEXAGON) {
                    if (cellId != -1 && cellId != 31) {
                        if (!cellPieces.containsKey(cellId)) {
                            cellPieces.put(cellId, new ArrayList<>());
                        }
                        cellPieces.get(cellId).add(dto);
                    }

                }







            }

            // 보드에 말 배치 (동일 셀에 여러 말이 있을 경우 겹쳐 표시)
            for (Map.Entry<Integer, List<PositionDTO>> entry : cellPieces.entrySet()) {
                int cellId = entry.getKey();
                List<PositionDTO> piecesAtCell = entry.getValue();
                Point pos = getPositionByBoardType(cellId);

                if (pos == null) continue;

                // 말이 1개일 경우 일반 표시
                if (piecesAtCell.size() == 1) {
                    PositionDTO dto = piecesAtCell.get(0);
                    JLabel pieceLabel = playerPiecesMap.get(dto.getPlayerId() + "_" + dto.getPieceId());

                    if (pieceLabel != null) {
                        if(boardType == BoardType.SQUARE) pieceLabel.setBounds(pos.x - 5, pos.y - 35, 40, 40);
                        else if (boardType == BoardType.PENTAGON) pieceLabel.setBounds(pos.x - 12, pos.y - 25, 40, 40);
                        else pieceLabel.setBounds(pos.x - 8, pos.y - 35, 40, 40);
                        boardPanel.add(pieceLabel);
                        boardPanel.setComponentZOrder(pieceLabel, 0);
                    }
                }
                // 말이 여러 개일 경우 (업혀있는 경우)
                else if (piecesAtCell.size() > 1) {
                    // 메인 말 위치 (첫 번째 말)
                    PositionDTO mainDto = piecesAtCell.get(0);
                    JLabel mainPieceLabel = playerPiecesMap.get(mainDto.getPlayerId() + "_" + mainDto.getPieceId());

                    if (mainPieceLabel != null) {
                        if(boardType == BoardType.SQUARE) mainPieceLabel.setBounds(pos.x - 5, pos.y - 35, 40, 40);
                        else if (boardType == BoardType.PENTAGON) mainPieceLabel.setBounds(pos.x - 14, pos.y - 25, 40, 40);
                        else mainPieceLabel.setBounds(pos.x - 8, pos.y - 35, 40, 40);
                        boardPanel.add(mainPieceLabel);
                        boardPanel.setComponentZOrder(mainPieceLabel, 0);

                        // 업힌 말 수 표시 라벨 생성
                        JLabel stackCountLabel = new JLabel("+" + (piecesAtCell.size() - 1));
                        stackCountLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                        stackCountLabel.setForeground(Color.RED);
                        stackCountLabel.setBackground(Color.WHITE);
                        stackCountLabel.setOpaque(true);
                        stackCountLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                        stackCountLabel.setBounds(pos.x + 15, pos.y - 35, 25, 20);
                        boardPanel.add(stackCountLabel);
                        boardPanel.setComponentZOrder(stackCountLabel, 0);

                        // 생성한 라벨을 리스트에 추가하여 추적
                        stackCountLabels.add(stackCountLabel);

                        // 나머지 말들은 살짝 겹쳐서 표시 (시각적 효과)
                        for (int i = 1; i < piecesAtCell.size() && i < 4; i++) { // 최대 3개까지만 표시
                            PositionDTO dto = piecesAtCell.get(i);
                            JLabel pieceLabel = playerPiecesMap.get(dto.getPlayerId() + "_" + dto.getPieceId());

                            if (pieceLabel != null) {
                                // 약간씩 오프셋 주기
                                pieceLabel.setBounds(pos.x - 5 + (i * 5), pos.y - 35 - (i * 5), 40, 40);
                                boardPanel.add(pieceLabel);
                                boardPanel.setComponentZOrder(pieceLabel, i);
                            }
                        }
                    }
                }
            }

            // 화면 갱신
            boardPanel.revalidate();
            boardPanel.repaint();
        }
    
        // 윷 이미지 초기화 메서드
        @Override
        public void clearYutImage() {
            yutResultLabel.setText("윷 결과: ");
            yutImageLabel.setIcon(null);
        }
    
        // 현재 플레이어 인덱스 설정
        public void setCurrentPlayerIndex(int index) {
            this.currentPlayerIndex = index;
        }
    
        // 게임 보드 패널 가져오기
        private JPanel getBoardPanel() {
            return (JPanel) ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.WEST);
        }
    
        @Override
        public void setThrowButtonListener(ThrowButtonListener throwButtonListener) {
            this.throwButtonListener = throwButtonListener;
        }
    
        @Override
        public void setFixedYutButtonListener(FixedYutButtonListener listener) {
            this.fixedYutButtonListener = listener;
        }
    
        @Override
        public void setTakeOutButtonListener(TakeOutButtonListener listener){
            this.takeOutButtonListener = listener;
        }
    
        @Override
        public void setPieceSelectionListener(PieceSelectionListener listener){
            this.pieceSelectionListener = listener;
        }
    
        @Override
        public void setCellSelectionListener(CellSelectionListener listener){
            this.cellSelectionListener = listener;
        }
    
        @Override
        public void setGameEndListener(GameEndListener listener) {
            this.gameEndListener = listener;
        }
    
        @Override
        public void setThrowButtonEnabled(boolean enabled) {
            rollButton.setEnabled(enabled);
            testRollButton.setEnabled(enabled);
    
            // 버튼 색상 변경 (비활성화 상태 시각화)
            if (enabled) {
                rollButton.setBackground(new Color(120, 200, 120));
                testRollButton.setBackground(new Color(120, 200, 120));
            } else {
                rollButton.setBackground(new Color(200, 200, 200));
                testRollButton.setBackground(new Color(200, 200, 200));
            }
        }
    
        // 말 선택 단계 판정(페이즈 전환)
        public void enableWaitingPieceSelection() {
            JOptionPane.showMessageDialog(this, "이동할 말을 선택하세요!");
            this.waitingPieceSelection = true;
        }
    
        public void disablePieceSelection() {
            JOptionPane.showMessageDialog(this, "윷을 던져주세요!");
            for (Map.Entry<String, JLabel> entry : playerPiecesMap.entrySet()) {
                JLabel pieceLabel = entry.getValue();
                pieceLabel.setBorder(null);
                pieceLabel.repaint();
            }
            this.waitingPieceSelection = false;
        }
    
        //좌표 초기화
        private void initializeSquarePositions(){
            squarePositionMap.put(-1, new Point(667,602));
            squarePositionMap.put(0, new Point(568,604));
            squarePositionMap.put(1, new Point(574,494));
            squarePositionMap.put(2, new Point(574,405));
            squarePositionMap.put(3, new Point(574,320));
            squarePositionMap.put(4, new Point(574,234));
            squarePositionMap.put(5, new Point(574,131));
    
            squarePositionMap.put(6, new Point(465,127));
            squarePositionMap.put(7, new Point(379,127));
            squarePositionMap.put(8, new Point(293,127));
            squarePositionMap.put(9, new Point(208,127));
            squarePositionMap.put(10, new Point(91,131));
    
            squarePositionMap.put(11, new Point(99,234));
            squarePositionMap.put(12, new Point(99,320));
            squarePositionMap.put(13, new Point(99,405));
            squarePositionMap.put(14, new Point(99,494));
            squarePositionMap.put(15, new Point(90,604));
    
            squarePositionMap.put(16, new Point(208,602));
            squarePositionMap.put(17, new Point(293,602));
            squarePositionMap.put(18, new Point(379,602));
            squarePositionMap.put(19, new Point(465,602));
            squarePositionMap.put(20, new Point(568,602));
    
            squarePositionMap.put(50, new Point(488,212));
            squarePositionMap.put(55, new Point(423,277));
            squarePositionMap.put(100, new Point(185,213));
            squarePositionMap.put(110, new Point(250,277));
            squarePositionMap.put(150, new Point(185,515));
            squarePositionMap.put(165, new Point(250,450));
            squarePositionMap.put(200, new Point(488,516));
            squarePositionMap.put(220, new Point(423,450));
            squarePositionMap.put(1000, new Point(330,370));
        }


        private void initializePentagonPositions(){
            pentagonPositionMap.put(-1, new Point(588,602));
            pentagonPositionMap.put(0, new Point(491,588));
            pentagonPositionMap.put(1, new Point(527,520));
            pentagonPositionMap.put(2, new Point(541,469));
            pentagonPositionMap.put(3, new Point(558,419));
            pentagonPositionMap.put(4, new Point(575,367));

            pentagonPositionMap.put(5, new Point(582,296));
            pentagonPositionMap.put(6, new Point(530,240));
            pentagonPositionMap.put(7, new Point(490,210));
            pentagonPositionMap.put(8, new Point(450,184));
            pentagonPositionMap.put(9, new Point(408, 155));

            pentagonPositionMap.put(10, new Point(350,130));
            pentagonPositionMap.put(11, new Point(287,157));
            pentagonPositionMap.put(12, new Point(248,184));
            pentagonPositionMap.put(13, new Point(208,212));
            pentagonPositionMap.put(14, new Point(170,240));

            pentagonPositionMap.put(15, new Point(115,292));
            pentagonPositionMap.put(16, new Point(123,366));
            pentagonPositionMap.put(17, new Point(139,417));
            pentagonPositionMap.put(18, new Point(155,470));
            pentagonPositionMap.put(19, new Point(171,522));

            pentagonPositionMap.put(20, new Point(202,594));
            pentagonPositionMap.put(21, new Point(275,598));
            pentagonPositionMap.put(22, new Point(324,596));
            pentagonPositionMap.put(23, new Point(375,595));
            pentagonPositionMap.put(24, new Point(425,597));



            pentagonPositionMap.put(50, new Point(503,320));
            pentagonPositionMap.put(55, new Point(430,345));
            pentagonPositionMap.put(100, new Point(347,212));
            pentagonPositionMap.put(110, new Point(349,285));
            pentagonPositionMap.put(150, new Point(195,318));
            pentagonPositionMap.put(165, new Point(268,344));
            pentagonPositionMap.put(200, new Point(250,517));
            pentagonPositionMap.put(220, new Point(300,450));
            pentagonPositionMap.put(250, new Point(441,520));
            pentagonPositionMap.put(275, new Point(395,446));

            pentagonPositionMap.put(1000, new Point(350,375));

        }

        private void initializeHexagonPositions(){
            hexagonPositionMap.put(-1, new Point(613,474));
            hexagonPositionMap.put(0, new Point(534,477));
            hexagonPositionMap.put(1, new Point(538,416));
            hexagonPositionMap.put(2, new Point(538,381));
            hexagonPositionMap.put(3, new Point(538,346));
            hexagonPositionMap.put(4, new Point(538,310));

            hexagonPositionMap.put(5, new Point(533,263));
            hexagonPositionMap.put(6, new Point(492,220));
            hexagonPositionMap.put(7, new Point(458,201));
            hexagonPositionMap.put(8, new Point(427,183));
            hexagonPositionMap.put(9, new Point(394,165));

            hexagonPositionMap.put(10, new Point(336,157));
            hexagonPositionMap.put(11, new Point(289,167));
            hexagonPositionMap.put(12, new Point(257,185));
            hexagonPositionMap.put(13, new Point(224,204));
            hexagonPositionMap.put(14, new Point(191,222));

            hexagonPositionMap.put(15, new Point(141,263));
            hexagonPositionMap.put(16, new Point(145,310));
            hexagonPositionMap.put(17, new Point(145,345));
            hexagonPositionMap.put(18, new Point(145,381));
            hexagonPositionMap.put(19, new Point(145,416));

            hexagonPositionMap.put(20, new Point(141,477));
            hexagonPositionMap.put(21, new Point(194,494));
            hexagonPositionMap.put(22, new Point(226,512));
            hexagonPositionMap.put(23, new Point(257,530));
            hexagonPositionMap.put(24, new Point(290,549));

            hexagonPositionMap.put(25, new Point(336,580));
            hexagonPositionMap.put(26, new Point(396,551));
            hexagonPositionMap.put(27, new Point(428,532));
            hexagonPositionMap.put(28, new Point(458,514));
            hexagonPositionMap.put(29, new Point(491,497));
            hexagonPositionMap.put(30, new Point(534,477));

            hexagonPositionMap.put(300, new Point(471,435));
            hexagonPositionMap.put(330, new Point(415,405));

            hexagonPositionMap.put(50, new Point(473,293));
            hexagonPositionMap.put(55, new Point(416,323));

            hexagonPositionMap.put(100, new Point(341,219));
            hexagonPositionMap.put(110, new Point(341,281));

            hexagonPositionMap.put(150, new Point(209,293));
            hexagonPositionMap.put(165, new Point(267,323));

            hexagonPositionMap.put(200, new Point(214,434));
            hexagonPositionMap.put(220, new Point(269,403));

            hexagonPositionMap.put(250, new Point(342,506));
            hexagonPositionMap.put(275, new Point(342,447));

            hexagonPositionMap.put(1000, new Point(334,371));
        }


    
        @Override
        public void setStatusMessage(String message) {
            statusMessageLabel.setText(message);
        }

        @Override
        public void clearPieceSelection() {
            // 모든 말의 테두리 제거
            for (Map.Entry<String, JLabel> entry : playerPiecesMap.entrySet()) {
                JLabel pieceLabel = entry.getValue();
                pieceLabel.setBorder(null);
            }
            // 선택된 말 인덱스 초기화 (필요시)
            this.selectedPieceIndex = -1;
        }

        /**
         * 화면을 닫는 메서드
         */
        public void dispose() {
            super.dispose();
        }
    }