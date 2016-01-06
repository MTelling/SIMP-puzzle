import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class SimpController implements KeyListener, MouseListener {

	GamePanel gameView;
	
	public SimpController(GamePanel gameView) {
		this.gameView = gameView;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	
	//TODO: It doesn't work when you click right now? The click is registered, but nothing happens. 
	@Override
	public void mousePressed(MouseEvent e) {
		int xPos = (e.getX() - Window.GAME_BORDER) / gameView.getBoard().getTileSize();
		int yPos = (e.getY() - Window.TOP_CONTROLS_SIZE) / gameView.getBoard().getTileSize();
		
		//Ask the board to move the tile at the clicked coordinate, if it is movable. And repaint if it is. 
		if(gameView.getBoard().moveTile(xPos, yPos)) {
			makeMove();

		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Window.toggleMenu();
		}

		//TODO: comment? 

		if(gameView.getBoard().moveTile(e.getKeyCode()) && !Window.menuToggle) {
			makeMove();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	
	
	
	//Helper method
	private void makeMove() {
		//TODO: Comment? 
		if (gameView.getScore().getMoves() == 0) {
			gameView.startTiming();
		}
		
		gameView.getScore().addMoves(1);
		gameView.repaint();
		
		if(gameView.getBoard().isGameOver()){
			gameView.stopTiming();
			JOptionPane.showMessageDialog(null, "OMG YOU HAVE WON!");
		}
	}
}
