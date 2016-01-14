package dk.vigilddisciples.npuzzle.controller.animation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import dk.vigilddisciples.npuzzle.NPuzzle;
import dk.vigilddisciples.npuzzle.controller.SimpController;

public class MoveAnimator implements ActionListener {
	
	private SimpController simpController;
	
	public MoveAnimator(SimpController simpController) {
		this.simpController = simpController;
	}
	
	//This function will be called every time the timer ticks.
	@Override
	public void actionPerformed(ActionEvent e) {
		
		boolean arrivedAtFinalPosition = simpController.getGamePanel().getBoard().moveWithAnimation(NPuzzle.getSettings().getAnimationSpeed().getValue());
		
		if(arrivedAtFinalPosition) {	
			//Ask the timer to stop ticking, because the tile should now be in the correct place. 
			((Timer)(e.getSource())).stop();
			
			//Tell the controller that the animation is done, and reset for next time the timer is called. 
			simpController.setAnimating(false);

			simpController.getGamePanel().repaint();

			simpController.checkIfGameIsWon();
			
		} else { //Repaint on each time through - no matter what. 
			simpController.getGamePanel().repaint();
		}
	}

}
