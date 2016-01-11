import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.Timer;

public class MoveSequenceAnimator implements ActionListener{

	private boolean animating;
	private LinkedList<Move> moves;
	private GamePanel gamePanel;
	
	public MoveSequenceAnimator(GamePanel gamePanel, LinkedList<Move> moves) {
		this.animating = false;
		this.gamePanel = gamePanel;
		this.moves = moves;
		gamePanel.setAnimationInProgress(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!animating) {
			gamePanel.getBoard().setToAnimationState(moves.get(0));
			animating = true;
		}
		
		if(gamePanel.getBoard().moveWithAnimation(Window.getSettings().getScrambleAnimationSpeed())) {
			gamePanel.repaint();
			animating = false;
			moves.remove(0);
		} else {
			gamePanel.repaint();
		}
		
		//If there are no more moves, stop animating. 
		if (moves.size() < 1) {
			((Timer)(e.getSource())).stop();
			gamePanel.setAnimationInProgress(false);
		}
	}

}
