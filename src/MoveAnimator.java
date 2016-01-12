import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class MoveAnimator implements ActionListener {
	
	private SimpController simpController;
	
	public MoveAnimator(SimpController simpController) {
		this.simpController = simpController;
	}
	
	//This function will be called every time the timer ticks.
	@Override
	public void actionPerformed(ActionEvent e) {
		
		boolean arrivedAtFinalPosition = simpController.getGamePanel().getBoard().moveWithAnimation(Window.getSettings().getAnimationSpeed());
		
		if(arrivedAtFinalPosition) {	
			//Ask the timer to stop ticking, because the tile should now be in the correct place. 
			((Timer)(e.getSource())).stop();
			
			//Tell the controller that the animation is done, and reset for next time the timer is called. 
			simpController.setAnimating(false);

			simpController.getGamePanel().repaint();

			//After each move, check if the game is won. 
			if (simpController.getGamePanel().getBoard().isGameOver()) {
				simpController.getGamePanel().getGameState().setGameDone(true);
			}
			
		} else { //Repaint on each time through - no matter what. 
			simpController.getGamePanel().repaint();
		}
	}

}
