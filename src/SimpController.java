import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SimpController implements KeyListener, MouseListener {

	SimpPuzzleView puzzleView;
	
	public SimpController(SimpPuzzleView puzzleView) {
		this.puzzleView = puzzleView;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) {
		int xCoord = (e.getX() - SimpWindow.GAME_BORDER) / puzzleView.getTileSize();
		int yCoord = (e.getY() - SimpWindow.GAME_BORDER) / puzzleView.getTileSize();
		//Ask the board to move the tile at the clicked coordinate, if it is movable. And repaint if it is. 
		if(puzzleView.getBoard().moveTile(xCoord, yCoord)) {
			puzzleView.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if(puzzleView.getBoard().moveTile(e.getKeyCode())) {
			puzzleView.repaint();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	
	
}
