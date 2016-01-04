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
		if(puzzleView.getBoard().moveTile((e.getX() - SimpWindow.GAME_BORDER) / puzzleView.getTileSize(), (e.getY() - SimpWindow.GAME_BORDER) / puzzleView.getTileSize())) {
			puzzleView.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

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
