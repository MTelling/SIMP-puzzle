import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class SimpController implements KeyListener, MouseListener {

	GamePanel gamePanel;
	
	public SimpController(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
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

		int xPos = (e.getX() - Window.GAME_BORDER) / gamePanel.getBoard().getTileSize();
		int yPos = (e.getY() - Window.GAME_BORDER) / gamePanel.getBoard().getTileSize();
		
		//Ask the board to move the tile at the clicked coordinate, if it is movable. And repaint if it is. 
		//TODO: Because i changed the movefunctions in Board, this has to be fixed. 
		if(true) {
			gamePanel.getBoard().moveTile(xPos, yPos);
			makeMove();

		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
		if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
			// This is what happens if you press CTRL+Z. This should undo last move.
			if(gamePanel.getGameState().canUndo()) {
				gamePanel.getGameState().undoMove();
				gamePanel.repaint();
			}
		} else if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()) {
			// This is what happens if you press CTRL+Y. This should redo last undo
			if(gamePanel.getGameState().canRedo()) {
				gamePanel.getGameState().redoMove();
				gamePanel.repaint();
			}
		} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Window.toggleMenu();
		} else if(gamePanel.getGameState().moveMade(e.getKeyCode()) && !Window.menuToggle) {
			makeMove();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	
	
	
	//Helper method
	private void makeMove() {
		//TODO: Comment? 
		if (gamePanel.getScore().getMoves() == 0) {
			gamePanel.startTiming();
		}
				
		gamePanel.getScore().addMoves(1);
		gamePanel.repaint();
		
		if(gamePanel.getBoard().isGameOver()){
			gamePanel.stopTiming();
			JOptionPane.showMessageDialog(null, "OMG YOU HAVE WON!");
		}
	}
}
