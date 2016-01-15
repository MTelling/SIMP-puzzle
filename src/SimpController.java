import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class SimpController implements KeyListener, MouseListener {

	private SimpPuzzleView puzzleView;
	private ControlView controlView;
	
	public SimpController(SimpPuzzleView puzzleView, ControlView controlView) {
		this.puzzleView = puzzleView;
		this.controlView = controlView;
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
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if(puzzleView.getBoard().moveTile(e.getKeyCode())) {

			makeMove();

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	
	
	
	//Helper method
	private void makeMove() {
		controlView.startTimerIfFirstMove();
		
		controlView.updateMovesLabel();
		
		puzzleView.repaint();
		
		//Check if game is won
		if(puzzleView.getBoard().isGameOver()){
			controlView.stopTiming();
			JOptionPane.showMessageDialog(null, "OMG YOU HAVE WON!");
		}
	}
}
