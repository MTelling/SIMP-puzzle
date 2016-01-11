import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ImageCropPanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	
	private Settings settings;
	private BufferedImage image;
	private int sx1, sy1, sx2, sy2;
	private int mouseStartX, mouseStartY;
	
	public ImageCropPanel(Settings settings) {
		this.settings = settings;
		try {
			image = ImageIO.read(new File(settings.getGamePicture()));
		} catch (IOException e) { }
		
		sx1 = 0;
		sy1 = 0;
		sx2 = Window.BOARD_SIZE;
		sy2 = Window.BOARD_SIZE;
		
		System.out.println(this.sx1 + " " + this.sy1 + " " + this.sx2 + " " + this.sy2);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		JButton btnCrop = new JButton("Crop Image");
		btnCrop.addActionListener(this);
		this.add(btnCrop);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(this.image, Window.GAME_BORDER, Window.TOP_CONTROLS_SIZE, Window.BOARD_SIZE + Window.GAME_BORDER, Window.BOARD_SIZE + Window.TOP_CONTROLS_SIZE, sx1, sy1, sx2, sy2, null);
		g2d.drawRect(Window.GAME_BORDER, Window.TOP_CONTROLS_SIZE, Window.BOARD_SIZE, Window.BOARD_SIZE);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getX() > Window.GAME_BORDER && e.getX() < Window.GAME_BORDER + Window.BOARD_SIZE) {
			if(e.getY() > Window.TOP_CONTROLS_SIZE && e.getY() < Window.TOP_CONTROLS_SIZE + Window.BOARD_SIZE) {
				mouseStartX = e.getX();
				mouseStartY = e.getY();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) { 
		if(e.getX() > Window.GAME_BORDER && e.getX() < Window.GAME_BORDER + Window.BOARD_SIZE) {
			if(e.getY() > Window.TOP_CONTROLS_SIZE && e.getY() < Window.TOP_CONTROLS_SIZE + Window.BOARD_SIZE) {
				this.sx1 += this.mouseStartX - e.getX();
				this.sx1 = Math.max(this.sx1, 0);
				if(this.sx2 + Window.BOARD_SIZE + this.sx1 <= this.image.getWidth()) {
					this.sx2 = this.sx1 + Window.BOARD_SIZE;
				} else {
					this.sx1 = this.sx2 - Window.BOARD_SIZE;
				}
				
				this.sy1 += this.mouseStartY - e.getY();
				this.sy1 = Math.max(this.sy1, 0);
				if(this.sy2 + Window.BOARD_SIZE + this.sy1 <= this.image.getHeight()) {
					this.sy2 = this.sy1 + Window.BOARD_SIZE;
				} else {
					this.sy1 = this.sy2 - Window.BOARD_SIZE;
				}
				
				System.out.println(this.sx1 + " " + this.sy1 + " " + this.sx2 + " " + this.sy2);
				
				this.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) { }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		settings.setGamePicture("resources/pics/" + ImageHandler.cropAndSave(this.image, this.sx1, this.sx2));
		System.out.println(settings.getGamePicture());
		Window.swapView("settings");
	}
}
