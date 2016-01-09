import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class SimpController implements KeyListener, MouseListener, MouseMotionListener {

	private GamePanel gamePanel;
	
	public SimpController(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	
	private void makeMove(int dx, int dy) {
		//Before making a move, check if a move should be made. 
		//If it should be made saveGameState to the current board and then make the move. 
		if (gamePanel.getBoard().isMoveValid(dx, dy)) {
			//Start time if it's the first move in the game, or if it's the first new move after load game. 
			if (gamePanel.getScore().getMoves() == 0 || gamePanel.getScore().getNewMoves() == 0 ) {
				gamePanel.startTiming();
			}
			
			//Before making the move, save current game stat to gameState. 
			gamePanel.getGameState().saveCurrentMove(dx, dy);
			
			//Move tile
			gamePanel.getBoard().setToAnimationState(dx, dy);
			
			//Add a move to scoreModel.
			gamePanel.getScore().addMoves(1);
			gamePanel.startAnimation();	
		}
	}

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
					int xPos = (e.getX() - Window.GAME_BORDER) / (int)gamePanel.getBoard().getTileSize();
					int yPos = (e.getY() - Window.TOP_CONTROLS_SIZE) / (int)gamePanel.getBoard().getTileSize();
					
					//Ask the board to move the tile at the clicked coordinate, if it is movable. And repaint if it is. 
					int dx = 0, dy = 0;
					int emptyX = this.gamePanel.getBoard().getCurrEmptyTile().x;
					int emptyY = this.gamePanel.getBoard().getCurrEmptyTile().y;
					
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
					
					//Try to make the move
					makeMove(dx, dy);
				}
			}
		}
	}

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
				int keyCode = e.getKeyCode();
				
				int[] controls = gamePanel.getSettings().getControls();
				
				if (keyCode == controls[0]) { //Moves tile to the left
					dx = -1;
				} else if (keyCode == controls[1]) { //Moves tile to the right
					dx = 1;
				} else if (keyCode == controls[2]) { //Moves tile up
					dy = -1;
				} else if (keyCode == controls[3]) { //Moves tile down
					dy = 1;
				} else {
				}
				
				//Try to make the move. 
				makeMove(dx, dy);
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//Check if the mouse is over the cogwheel.
		if (!Window.menuToggle && e.getY() > (Window.TOP_CONTROLS_SIZE - GamePanel.COGWHEEL_SIZE) / 2 
				&& e.getY() < (Window.TOP_CONTROLS_SIZE - GamePanel.COGWHEEL_SIZE) / 2 + GamePanel.COGWHEEL_SIZE) {
			if (e.getX() > Window.WINDOW_WIDTH-Window.GAME_BORDER-GamePanel.COGWHEEL_SIZE 
					&& e.getX() < Window.WINDOW_WIDTH - Window.GAME_BORDER) {
				//If the mouse is over the cogwheel make it a hand. 
				gamePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		} else { //Mouse must be outside the cogwheel and should be normal
			gamePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}	

}
