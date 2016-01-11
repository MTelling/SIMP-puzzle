import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class MoveAnimator implements ActionListener {
	
	private SimpController simpController;
	private boolean firstTime;
	
	public MoveAnimator(SimpController simpController) {
		this.simpController = simpController;
		this.firstTime = true;
	}
	
	//This function will be called every time the timer ticks.
	@Override
	public void actionPerformed(ActionEvent e) {
		if (firstTime) {
			//First time through tell the controller that a move is being animated. 
			simpController.setAnimating(true);
			firstTime = false;
		}
		
		boolean arrivedAtFinalPosition = simpController.getGamePanel().getBoard().moveWithAnimation(Window.getSettings().getAnimationSpeed());
		
		if(arrivedAtFinalPosition) {	
			//Ask the timer to stop ticking, because the tile should now be in the correct place. 
			((Timer)(e.getSource())).stop();
			
			//Tell the controller that the animation is done, and reset for next time the timer is called. 
			simpController.setAnimating(false);
			
			firstTime = true;
			
			simpController.getGamePanel().repaint();
		} else {
			simpController.getGamePanel().repaint();
		}
	}

}
