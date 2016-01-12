import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class SimpController implements ActionListener, KeyListener, MouseListener, MouseMotionListener {

	private GamePanel gamePanel;
	private boolean isAnimating;
	private Timer moveAnimator;
	
	public SimpController(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.isAnimating = false;
		
		initMoveAnimator();
	}
	
	public void initMoveAnimator() {
		this.moveAnimator = new Timer(Window.getSettings().getRefreshRate(), new MoveAnimator(this));
	}
	
	private void makeMove(Move move) {
		//Before making a move, check if a move should be made. 
		//If it should be made saveGameState to the current board and then make the move.
		//Also never allow  a move if the game is won. 
		if (gamePanel.getBoard().isMoveValid(move) && !gamePanel.getGameState().isGameDone()) {
			//Start time if it's the first move in the game, or if it's the first new move after load game. 
			if (gamePanel.getScore().getMoves() == 0 || gamePanel.getScore().getNewMoves() == 0 ) {
				this.startClock();
			}
			//Before making the move, save current game stat to gameState. 
			gamePanel.getGameState().saveCurrentMove(move);
			
			//Move tile
			gamePanel.getBoard().setToAnimationState(move);
			
			//Add a move to scoreModel.
			gamePanel.getScore().addMoves(1);			
						
			//Move with or without animation depending on what the setting is in settings. 
			showMove(Window.getSettings().isAnimationOn());
		
		}
	}
	
	//Shows a move. Animated or not. 
	private void showMove(boolean shouldAnimate) {
		this.setAnimating(true);

		if (shouldAnimate) {
			this.moveAnimator.start();	
		} else {
			//Just sets the board to the new default state and then repaints. 
			gamePanel.getBoard().moveWithoutAnimation();
			
			//TODO: There should probably be a method in gamePanel that is called instead of repaint. Depending on where we choose to put the check for game won. 
			gamePanel.repaint();
			
			this.setAnimating(false);
		}
	}
	
	private void scrambleBoard() {
		LinkedList<Move> scramblingSequence = this.gamePanel.getBoard().makeRandomValidMoves(Window.getSettings().getDifficulty());
		
		if (Window.getSettings().isAnimationScramblingOn()) {
			//Show scramble
			Timer scrambleAnimationTimer = new Timer(Window.getSettings().getRefreshRate(), new MoveSequenceAnimator(this, scramblingSequence));
			scrambleAnimationTimer.start();
		} else {//Don't show scramble
			while (scramblingSequence.size() != 0) {
				gamePanel.getBoard().setToAnimationState(scramblingSequence.get(0));
				gamePanel.getBoard().moveWithoutAnimation();
				scramblingSequence.remove(0);
			}
		}
		

	}
	
	//1000 is a 1000milliseconds so the timer will fire each second. 
		private Timer clock = new Timer(1000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// call the updateSeconds functions which adds a second to the scoreModel and updates the labeltext. 
				gamePanel.getScore().addSeconds(1);
				gamePanel.repaint();

				//TODO: We must agree on where to put this. 
				if (gamePanel.getGameState().isGameDone()) {
					gamePanel.getGameState().setGameDone(true);
					JOptionPane.showMessageDialog(null, "OMG YOU HAVE WON!!");
					stopClock();
				}
			}
		});
		
		public void startClock () {
			clock.start();
		}
		
		public void stopClock() {
			clock.stop();
		}
	
	/// ALL EVENTS FROM HERE /// 

	@Override
	public void mousePressed(MouseEvent e) {
		WindowSize currWindowSize = Window.getSettings().getCurrWindowSize();
		if (!this.isAnimating) {
			if (!Window.menuToggle) {
				if (e.getY() > (currWindowSize.getTOP_CONTROLS_SIZE() - GamePanel.MENUBUTTON_SIZE) / 2 
						&& e.getY() < (currWindowSize.getTOP_CONTROLS_SIZE() - GamePanel.MENUBUTTON_SIZE) / 2 + GamePanel.MENUBUTTON_SIZE) {
					if (e.getX() > currWindowSize.getWINDOW_WIDTH()-currWindowSize.getGAME_BORDER()-GamePanel.MENUBUTTON_SIZE 
							&& e.getX() < currWindowSize.getWINDOW_WIDTH() - currWindowSize.getGAME_BORDER()) {
						Window.toggleMenu();
					}
				} else {
					int xPos = (e.getX() - currWindowSize.getGAME_BORDER()) / (int)gamePanel.getBoard().getTileSize();
					int yPos = (e.getY() - currWindowSize.getTOP_CONTROLS_SIZE()) / (int)gamePanel.getBoard().getTileSize();
					
					//Ask the board to move the tile at the clicked coordinate, if it is movable. And repaint if it is. 
					int dx = 0, dy = 0;
					int emptyX = this.gamePanel.getBoard().getCurrEmptyTile().x;
					int emptyY = this.gamePanel.getBoard().getCurrEmptyTile().y;
					
					//Determine where the clicked tile should go
					if(xPos == emptyX - 1 && yPos == emptyY) {
						dx = -1;
					} else if(xPos == emptyX + 1 && yPos == emptyY) {
						dx = 1;
					} else if(xPos == emptyX && yPos == emptyY - 1) {
						dy = -1;
					} else if(xPos == emptyX && yPos == emptyY + 1) {
						dy = 1;
					}
					
					//Try to make the move
					makeMove(new Move(dx,dy));
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!this.isAnimating) {
			//Redo undo if ctrl+z and ctrl+y
			if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown() && !Window.menuToggle && !gamePanel.getGameState().isGameDone()) {
				// This is what happens if you press CTRL+Z. This should undo last move.
				if(gamePanel.getGameState().canUndo()) {
					gamePanel.getGameState().undoMove();
					
					if (gamePanel.getScore().getMoves() == 0 || gamePanel.getScore().getNewMoves() == 0 ) {
						this.startClock();
					}
					
					showMove(Window.getSettings().isAnimationOn());
				}
			} else if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown() && !Window.menuToggle && !gamePanel.getGameState().isGameDone()) {
				// This is what happens if you press CTRL+Y. This should redo last undo
				if(gamePanel.getGameState().canRedo()) {
					gamePanel.getGameState().redoMove();
					
					if (gamePanel.getScore().getMoves() == 0 || gamePanel.getScore().getNewMoves() == 0 ) {
						this.startClock();
					}
					
					showMove(Window.getSettings().isAnimationOn());
				}
			} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if(!Window.menuToggle)
					this.stopClock();
				else if (!gamePanel.getGameState().isGameDone())
					this.startClock();
				Window.toggleMenu();
			} else if(!Window.menuToggle) {
				int dx = 0, dy = 0;
				int keyCode = e.getKeyCode();
				
				int[] controls = Window.getSettings().getControls();
				
				if (keyCode == controls[0]) { //Moves tile to the left
					dx = -1;
				} else if (keyCode == controls[1]) { //Moves tile to the right
					dx = 1;
				} else if (keyCode == controls[2]) { //Moves tile up
					dy = -1;
				} else if (keyCode == controls[3]) { //Moves tile down
					dy = 1;
				} else {
				}
				
				//Try to make the move. 
				makeMove(new Move(dx, dy));
			} 
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		WindowSize currWindowSize = Window.getSettings().getCurrWindowSize();
		//Check if the mouse is over the cogwheel.
		if (!Window.menuToggle && e.getY() > (currWindowSize.getTOP_CONTROLS_SIZE() - GamePanel.MENUBUTTON_SIZE) / 2 
				&& e.getY() < (currWindowSize.getTOP_CONTROLS_SIZE() - GamePanel.MENUBUTTON_SIZE) / 2 + GamePanel.MENUBUTTON_SIZE) {
			if (e.getX() > currWindowSize.getWINDOW_WIDTH()-currWindowSize.getGAME_BORDER()-GamePanel.MENUBUTTON_SIZE 
					&& e.getX() < currWindowSize.getWINDOW_WIDTH() - currWindowSize.getGAME_BORDER()) {
				//If the mouse is over the cogwheel make it a hand. 
				gamePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		} else { //Mouse must be outside the cogwheel and should be normal
			gamePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("mainMenuNewGame")) {
			Window.swapView("puzzle");
			
			//Settings could have changed so reinit moveAnimator to get the new FPS
			initMoveAnimator();
			
			//Reset the game
			gamePanel.getGameState().restartGame();
			gamePanel.reset();
			
			this.scrambleBoard();
		} else if (e.getActionCommand().equals("mainMenuLoadGame")) {
			//Load the game from file
			Object obj = SaveLoad.loadFromFile("SavedGame");
			if(obj instanceof GameState) {
				this.gamePanel.updateGameState((GameState) obj);
				this.gamePanel.getScore().setNewMoves(0);
				this.gamePanel.reset();
				Window.swapView("puzzle");
			}
		} else if(e.getActionCommand().equals("mainMenuSettings")) {
			Window.swapView("settings");
		} else if(e.getActionCommand().equals("mainMenuExitGame")) {
			System.exit(0);
		} else if(e.getActionCommand().equals("inGameContinueGame")) {
			Window.toggleMenu();
			if (!gamePanel.getGameState().isGameDone()) {
				this.startClock();
			}
		} else if (e.getActionCommand().equals("inGameNewGame")) {
			this.stopClock();
			Window.toggleMenu();
			this.gamePanel.getGameState().restartGame();
			this.gamePanel.reset();
			this.scrambleBoard();
		} else if (e.getActionCommand().equals("inGameSaveGame")) {
			SaveLoad.saveToFile(this.gamePanel.getGameState(), "SavedGame");
		} else if(e.getActionCommand().equals("inGameExitToMainMenu")) {
			this.gamePanel.getGameState().restartGame();
			this.gamePanel.reset();
			this.stopClock();
			Window.toggleMenu();
			Window.swapView("mainMenu");
		} else if (e.getActionCommand().equals("inGameSolveGame")) {
			System.out.println("solving game");
			System.out.println("Got here");
			LinkedList<Move> solveSequence = gamePanel.getBoard().solveUpperAndLeft();
			Timer scrambleAnimationTimer = new Timer(50, new MoveSequenceAnimator(this, solveSequence));
			scrambleAnimationTimer.start();
		}
	}
	
	
	/// SETTERS FROM HERE ///
	
	public void setAnimating(boolean isAnimating) {
		this.isAnimating = isAnimating;
	}
	
	/// GETTERS FROM HERE ///
	
	public GamePanel getGamePanel() {
		return this.gamePanel;
	}
	
	/// UNUSUED FROM HERE ///
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}

}
