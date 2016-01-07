import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

public class SimpController implements KeyListener, MouseListener, MouseMotionListener {

	GamePanel gamePanel;
	
	public SimpController(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}
	

	@Override
	public void mouseExited(MouseEvent arg0) {}

	
	@Override
	public void mousePressed(MouseEvent e) {
		if (!gamePanel.isAnimating()) {
			if (!Window.menuToggle) {
				
				if (e.getY() > (Window.TOP_CONTROLS_SIZE - GamePanel.COGWHEEL_SIZE) / 2 
						&& e.getY() < (Window.TOP_CONTROLS_SIZE - GamePanel.COGWHEEL_SIZE) / 2 + GamePanel.COGWHEEL_SIZE) {
					if (e.getX() > Window.WINDOW_WIDTH-Window.GAME_BORDER-GamePanel.COGWHEEL_SIZE 
							&& e.getX() < Window.WINDOW_WIDTH - Window.GAME_BORDER) {
						Window.toggleMenu(false);
					}
				} else {
				
					int xPos = (e.getX() - Window.GAME_BORDER) / gamePanel.getBoard().getTileSize();
					int yPos = (e.getY() - Window.TOP_CONTROLS_SIZE) / gamePanel.getBoard().getTileSize();
					
					//Ask the board to move the tile at the clicked coordinate, if it is movable. And repaint if it is. 
					int dx, dy;
					dx = dy = 0;
					int emptyX = this.gamePanel.getBoard().getEmptyTile().x;
					int emptyY = this.gamePanel.getBoard().getEmptyTile().y;
					
					//Determine where the clicked tile should go
					if(xPos == emptyX - 1 && yPos == emptyY) {
						dx = -1;
					} else if(xPos == emptyX + 1 && yPos == emptyY) {
						dx = 1;
					} else if(xPos == emptyX && yPos == emptyY - 1) {
						dy = -1;
					} else if(xPos == emptyX && yPos == emptyY + 1) {
						dy = 1;
					}
					
					//Before making a move, check if a move should be made. 
					//If it should be made saveGameState to the current board and then make the move.  
					if (gamePanel.getBoard().isMoveValid(dx, dy)) {
						makeMove(dx, dy);
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gamePanel.isAnimating()) {
			//Redo undo if ctrl+z and ctrl+y
			if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown() && !Window.menuToggle) {
				// This is what happens if you press CTRL+Z. This should undo last move.
				if(gamePanel.getGameState().canUndo()) {
					gamePanel.getGameState().undoMove();
					gamePanel.startTiming();
					gamePanel.startAnimation();
				}
			} else if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown() && !Window.menuToggle) {
				// This is what happens if you press CTRL+Y. This should redo last undo
				if(gamePanel.getGameState().canRedo()) {
					gamePanel.getGameState().redoMove();
					gamePanel.startAnimation();
				}
			} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				Window.toggleMenu(false);
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
				
				//Before making a move, check if a move should be made at all. 
				if (gamePanel.getBoard().isMoveValid(dx, dy)) {
					makeMove(dx, dy);
				} 
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	
	

	private void makeMove(int dx, int dy) {
		//Start time if it's the first move in the game, or if it's the first new move after load game. 
		if (gamePanel.getScore().getMoves() == 0 || gamePanel.getScore().getNewMoves() == 0 ) {
			gamePanel.startTiming();
		}
		
		
		//Before making the move, save current game stat to gameState. 
		gamePanel.getGameState().saveCurrentState();
		
		//Move tile
		gamePanel.getBoard().moveTile(dx, dy);
		
		//Add a move to scoreModel.
		gamePanel.getScore().addMoves(1);
		gamePanel.startAnimation();
		
		//Check if game is won.
		if(gamePanel.getBoard().isGameOver()){
			gamePanel.stopTiming();
			JOptionPane.showMessageDialog(null, "OMG YOU HAVE WON!");
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!Window.menuToggle && e.getY() > (Window.TOP_CONTROLS_SIZE - GamePanel.COGWHEEL_SIZE) / 2 
				&& e.getY() < (Window.TOP_CONTROLS_SIZE - GamePanel.COGWHEEL_SIZE) / 2 + GamePanel.COGWHEEL_SIZE) {
			if (e.getX() > Window.WINDOW_WIDTH-Window.GAME_BORDER-GamePanel.COGWHEEL_SIZE 
					&& e.getX() < Window.WINDOW_WIDTH - Window.GAME_BORDER) {
				gamePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		} else {
			gamePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
	}
}
