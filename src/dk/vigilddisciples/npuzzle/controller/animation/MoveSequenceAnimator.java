package dk.vigilddisciples.npuzzle.controller.animation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.Timer;

import dk.vigilddisciples.npuzzle.NPuzzle;
import dk.vigilddisciples.npuzzle.controller.SimpController;
import dk.vigilddisciples.npuzzle.model.Move;

public class MoveSequenceAnimator implements ActionListener{

	private boolean animating;
	private LinkedList<Move> moves;
	private SimpController controller;

	public MoveSequenceAnimator(SimpController controller, LinkedList<Move> moves) {
		this.animating = false;
		this.controller = controller;
		this.moves = moves;
		this.controller.setAnimating(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!animating) {
			this.controller.getGamePanel().getBoard().setToAnimationState(moves.get(0));
			animating = true;
		}

		if(this.controller.getGamePanel().getBoard().moveWithAnimation(NPuzzle.getSettings().getScrambleAnimationSpeed())) {
			this.controller.getGamePanel().repaint();
			animating = false;
			moves.remove(0);
		} else {
			this.controller.getGamePanel().repaint();
		}

		//If there are no more moves, stop animating. 
		if (moves.size() < 1) {
			((Timer)(e.getSource())).stop();
			this.controller.setAnimating(false);
		}
	}

}
