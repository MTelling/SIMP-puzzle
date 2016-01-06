import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SimpController implements KeyListener, MouseListener {

	SimpPuzzleView puzzleView;
	ControlView controlView;
	GameState gameState;
	
	public SimpController(SimpPuzzleView puzzleView, ControlView controlView, GameState gameState) {
		this.puzzleView = puzzleView;
		this.controlView = controlView;
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
		
		int xPos = (e.getX() - SimpWindow.GAME_BORDER) / puzzleView.getTileSize();
		int yPos = (e.getY() - SimpWindow.GAME_BORDER) / puzzleView.getTileSize();
		//Ask the board to move the tile at the clicked coordinate, if it is movable. And repaint if it is. 

		if(puzzleView.getBoard().moveTile(xPos, yPos)) {
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

				puzzleView.getBoard().setTiles(gameState.goBack(1));

				puzzleView.repaint();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()) {
			// This is what happens if you press CTRL+Y. This should redo last move
			if(gameState.canGoForward()) {
				
				puzzleView.getBoard().setTiles(gameState.goForward(1));
				
				puzzleView.repaint();
			}
		}
		
		if(puzzleView.getBoard().moveTile(e.getKeyCode())) {
			makeMove();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	
	
	
	//Helper method
	private void makeMove() {
		gameState.updateGameState(puzzleView.getBoard().getTiles());
		controlView.updateMovesLabel();
		puzzleView.repaint();
	}
}
