package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectYutResultScreen extends JFrame{
	
	public SelectYutResultScreen() {
        super("지정윷 선택");
        InitUI();
    }
	
	private void InitUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 200);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20,20,10,10);
        getContentPane().setBackground(new Color(255, 245, 230));
        
        String[] yutResult = {"백도", "도", "개", "걸", "윷", "모"};
        int[] yutValues = {-1, 1, 2, 3, 4, 5};
        
        for (int i = 0; i < yutResult.length; i++) {
            JButton button = new JButton(yutResult[i]);
            int result = yutValues[i]; 
            button.setFont(new Font("SansSerif", Font.BOLD, 18));
            button.setBackground(new Color(120, 200, 120));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(100, 70));
            button.addActionListener(e -> {
                System.out.println("선택된 윷 결과: " + result);
                dispose(); 
            });
            
            gbc.gridx = i;
            gbc.gridy = 0;
            add(button, gbc);
        }
        
        setLocationRelativeTo(null);
        setVisible(true);
       
	}
}
