import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class SimpController implements KeyListener, MouseListener {

	GamePanel gamePanel;
	GameState gameState;
	
	public SimpController(GamePanel gamePanel, GameState gameState) {
		this.gamePanel = gamePanel;
		this.gameState = gameState;
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
		if(gamePanel.getBoard().moveTile(xPos, yPos)) {
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
			
			if(gameState.canGoBack()) {
				
				gamePanel.getBoard().setEmptyTile(gameState.goBackEmpty(1));
				gamePanel.getBoard().setTiles(gameState.goBack(1));
				

				gamePanel.repaint();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()) {
			// This is what happens if you press CTRL+Y. This should redo last move
			if(gameState.canGoForward()) {
				
				gamePanel.getBoard().setEmptyTile(gameState.goForwardEmpty(1));
				gamePanel.getBoard().setTiles(gameState.goForward(1));
				
				
				gamePanel.repaint();
			}
		}

		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Window.toggleMenu();
		}

		//TODO: comment? 

		if(gamePanel.getBoard().moveTile(e.getKeyCode()) && !Window.menuToggle) {
			makeMove();
			gameState.updateGameState(gamePanel.getBoard().getTiles(), gamePanel.getBoard().getEmptyTile());
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
