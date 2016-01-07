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
		if(false) {
			gamePanel.getBoard().moveTile(xPos, yPos);
			//makeMove();

		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		
		//Redo undo if ctrl+z and ctrl+y
		if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown() && !Window.menuToggle) {
			// This is what happens if you press CTRL+Z. This should undo last move.
			if(gamePanel.getGameState().canUndo()) {
				gamePanel.getGameState().undoMove();
				gamePanel.repaint();
			}
		} else if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown() && !Window.menuToggle) {
			// This is what happens if you press CTRL+Y. This should redo last undo
			if(gamePanel.getGameState().canRedo()) {
				gamePanel.getGameState().redoMove();
				gamePanel.repaint();
			}
		} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Window.toggleMenu();
			//otherwise a move could have been made. Check if one has been made and make it if it has. 
		} else if(!Window.menuToggle) {
			int dx, dy;
			dx = dy = 0;
			switch (e.getKeyCode()) {
				case KeyEvent.VK_RIGHT:	dx = -1; break;
				case KeyEvent.VK_LEFT: dx = 1; break;
				case KeyEvent.VK_DOWN: dy = -1; break;
				case KeyEvent.VK_UP: dy = 1; break;
				default: break;
			}
			
			//Before making a move, check if a move should be made. 
			//If it should be made saveGameState to the current board and then make the move.  
			if (gamePanel.getBoard().isMoveValid(dx, dy)) {
				makeMove(dx, dy);
			} 
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	
	
	
	//Helper method
	private void makeMove(int dx, int dy) {
		//TODO: Comment? 
		if (gamePanel.getScore().getMoves() == 0 || gamePanel.getScore().getNewMoves() == 0 ) {
			gamePanel.startTiming();
		}
				
		gamePanel.getGameState().saveCurrentState();
		gamePanel.getBoard().moveTile(dx, dy);
		gamePanel.getScore().addMoves(1);
		gamePanel.repaint();
		
		if(gamePanel.getBoard().isGameOver()){
			gamePanel.stopTiming();
			JOptionPane.showMessageDialog(null, "OMG YOU HAVE WON!");
		}
	}
}
