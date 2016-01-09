import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class MoveAnimator implements ActionListener {
	
	private SimpController simpController;
	private boolean firstTime;
	
	public MoveAnimator(SimpController simpController) {
		this.simpController = simpController;
		this.firstTime = false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (firstTime) {
			simpController.setAnimating(true);
			firstTime = false;
		}
		
		// TODO Auto-generated method stub
		boolean arrivedAtFinalPosition = simpController.getGamePanel().getBoard().moveWithAnimation(simpController.getGamePanel().getSettings().getAnimationSpeed());
		if(arrivedAtFinalPosition) {
			simpController.getGamePanel().repaint();
			simpController.setAnimating(false);
			firstTime = true;
			((Timer)(e.getSource())).stop();
		} else {
			simpController.getGamePanel().repaint();
		}
	}

}
