import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MenuPanel extends JPanel {

	public MenuPanel() {
		this.setBounds(64, 64, 448-128, 512-128);
		this.setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(128,128,128,200));
		g.fillRoundRect(0, 0, 448-128, 512-128, 32, 32);
	}
	
	
	
}
