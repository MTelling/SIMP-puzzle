import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

	//TODO: Right now you can only use images. Fix this so we can use both images and just plain labels. 
	public static final int COGWHEEL_SIZE = Window.TOP_CONTROLS_SIZE - Window.TOP_CONTROLS_SIZE/4;
	public static final String RESOURCE_PATH = "resources/";
	public static final String THEME_PATH = RESOURCE_PATH + "themes/default/";
 	private static final long serialVersionUID = 1L;
	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private final Color TILE_TEXT_COLOR = Color.WHITE;
	private GameState gameState;
	
	private Image boardImg;
	private Image inGameMenuImg;
	private int movesLabelxPos;
	private int movesLabelyPos;
	private int cogWheelXPos;
	private int cogWheelYPos;
	private int timeLabelxPos;
	private int timeLabelyPos;
	private int[] stringWidths; //Saves width of strings depending how many characters are in. 
	private int stringHeight;
	private boolean firstPaint;
	private boolean animationInProgress;
	private BufferedImage[] picList;
	
	private Timer animationTimer;
	
	public GamePanel(GameState gs) {
		
		this.animationInProgress = false;
		this.setBounds(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
		this.setOpaque(true);
		
		this.gameState = gs;
				
		this.reset();
	}
	
	public void reset() {
		this.firstPaint = true;

		this.setBackground(BACKGROUND_COLOR);
		
		this.loadImages();
		
		//Set doublebuffering to true. It should be by default, but just in case. 
		this.setDoubleBuffered(true);
		
		this.initAnimationTimer();
		
		this.repaint();
		
		
	}
	
	public void scrambleBoard() {
		LinkedList<Move> scramblingSequence = getBoard().makeRandomValidMoves(this.getSettings().getDifficulty());
		Timer scrambleAnimationTimer = new Timer(getSettings().getRefreshRate(), new MoveSequenceAnimator(this, scramblingSequence));
		scrambleAnimationTimer.start();
	}
	
	//1000 is a 1000milliseconds so the timer will fire each second. 
	private Timer clock = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// call the updateSeconds functions which adds a second to the scoreModel and updates the labeltext. 
			getScore().addSeconds(1);
			repaint();
			
			//TODO: We must agree on where to put this. 
			if (gameState.isGameDone()) {
				gameState.setGameDone(true);
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
	
	//Animationtimer must be initted in the constructor because it needs variables from the gamestate. 
	private void initAnimationTimer() {
		this.animationTimer = new Timer(this.getSettings().getRefreshRate(), new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				boolean arrivedAtFinalPosition = getBoard().moveWithAnimation(getSettings().getAnimationSpeed());
				if(arrivedAtFinalPosition) {
					repaint();
					stopAnimation();
					
				} else {
					repaint();
				}
			}
			
		});
	}
	
	public void animateMove() {
		animationInProgress = true;
		animationTimer.start();
	}
	public void stopAnimation() {
		animationInProgress = false;
		animationTimer.stop();
		
		checkIfGameIsOver();
	}
	
	public void checkIfGameIsOver() {
		if (this.getBoard().isGameOver()){
			this.gameState.setGameDone(true);
		}
	}
	
	//TODO: Can we move a lot of code out of this that does not need to be calculated at each repaint? 
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		//Set font. Has to be done on each repaint, because it defaults it otherwise. 
		g2d.setFont(new Font("Sans Serif", Font.PLAIN, Window.LABEL_TEXT_SIZE));
		
		//Only calculate labelpositions first time round. 
		//We do this so it doesn't have to calculate the positions every single time the view is repainted. 
		//It only saves a bit cpu, but we think it's worth it. 
		if (this.firstPaint) {
			calcItemPositions(g2d);
		}
		
		//Draw everything above the board.
		this.drawControls(g2d);
		
		//Draw board background. 
		g2d.drawImage(boardImg, Window.GAME_BORDER, Window.WINDOW_HEIGHT - Window.GAME_BORDER - Window.BOARD_SIZE, Window.BOARD_SIZE, Window.BOARD_SIZE, null);
		
		//Set font for text on tiles. 
		g2d.setFont(new Font("Sans Serif", Font.ITALIC, Window.LABEL_TEXT_SIZE));
		
		//Calculate width of strings with 1 digit to 4 digits. 
		if (this.firstPaint) {
			this.stringWidths = calcStringWidths(g2d);
			this.stringHeight = g2d.getFontMetrics().getHeight();
			this.firstPaint = false;
		}
		
		//Draw Board
		this.drawBoard(g2d);
	}
	
	private void drawLabelInCenter(Graphics2D g2d, int x, int y, int xCoord, int yCoord, int fieldSize) {
		//Set text color
		g2d.setColor(TILE_TEXT_COLOR);
		
		//Position labels on tiles. 
		String TileNum = Integer.toString(this.getBoard().getTiles()[x][y].getNumber());
		int strXCoord = xCoord + (fieldSize / 2) - this.stringWidths[TileNum.length()] / 2;
		int strYCoord = yCoord + (fieldSize / 2) + this.stringHeight / 4;
		
		//Draw text for each tile
		g2d.drawString(TileNum, strXCoord, strYCoord);
	}
	
	private void drawLabelInCorner(Graphics2D g2d, int x, int y, int xCoord, int yCoord, int cornerSize) {
		
		//TODO: Make this color a field.
		Color clr = new Color(50, 50, 50, 100);
		g2d.setColor(clr);
		g2d.fillRect(xCoord, yCoord, cornerSize, cornerSize);
		
		this.drawLabelInCenter(g2d, x, y, xCoord, yCoord, cornerSize);
		
	}
	
	private void drawControls(Graphics2D g2d) {
		//Draw Time and Move labels.  
				g2d.drawString("Time: " + this.getScore().timeToString(), this.timeLabelxPos, this.timeLabelyPos);
				g2d.drawString("Moves: " + this.getScore().getMoves(), this.movesLabelxPos, this.movesLabelyPos);
			
				//Draw cogwheel (settings) button
				g2d.drawImage(inGameMenuImg, cogWheelXPos, cogWheelYPos, COGWHEEL_SIZE, COGWHEEL_SIZE, null);
	}
	
	private void drawBoard(Graphics2D g2d) {
		Tile[][] tiles = this.getBoard().getTiles();
		for(int y = 0; y < this.getBoard().getTilesPerRow(); y++) {
			for(int x = 0; x < this.getBoard().getTilesPerRow(); x++) {
				//Draw all tiles except for the empty one.
				if(tiles[x][y].getNumber() != Math.pow(this.getBoard().getTilesPerRow(),2)) {
				
					//Get x and y position
					int xCoord = (int)tiles[x][y].getX();
					int yCoord = (int)tiles[x][y].getY();
					
					//Draws tile at x and y pos with image gotten from ressources. 
					g2d.drawImage(picList[tiles[x][y].getNumber() - 1 ], xCoord, yCoord, (int)this.getBoard().getTileSize(), (int)this.getBoard().getTileSize(), null);
					
					
					//Draws text on image
					if(this.getSettings().isPictureOn()) {

						//Draw border around unfinished picture
						if (!this.gameState.isGameDone()){
							g2d.setColor(Color.BLACK);
							g2d.drawRect(xCoord, yCoord, (int)this.getBoard().getTileSize(), (int)this.getBoard().getTileSize());
						} 
						
						//If a picture is showing and labels is on the label should be printed in the upper left corner. 
						if (this.getSettings().isLabelsOn()) {
							//TODO: how can we decide the size of this more appropriately? Right now it's just 30. 
							this.drawLabelInCorner(g2d, x, y, xCoord, yCoord, 30);
						} 
						
					} else { //No picture is showing, just draw label in center. 
						this.drawLabelInCenter(g2d, x, y, xCoord, yCoord, (int)this.getBoard().getTileSize());
					}
					
				}
			}
		}
	}
	
	private void loadImages() {
		//Load boardImage
		ImageIcon boardIc = new ImageIcon(THEME_PATH + "board.jpeg");
		this.boardImg = boardIc.getImage();
		
		//Load cogwheelImage
		ImageIcon cogwheelIc = new ImageIcon(RESOURCE_PATH + "inGameMenuIcon.png");
		this.inGameMenuImg = cogwheelIc.getImage();
		
		//Create list of tileImages
		try {
			if (this.getSettings().isPictureOn()) {
				this.picList = ImageHandler.getTilePics(this.getBoard().getTilesPerRow(), (int)this.getBoard().getTileSize(), RESOURCE_PATH + "pics/test", "jpg");
			} else {
				this.picList = ImageHandler.getTilePics(this.getBoard().getTilesPerRow(), (int)this.getBoard().getTileSize(), RESOURCE_PATH + "pics/basic", "jpg");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Helper method to calculate label positions. 
	private void calcItemPositions(Graphics2D g2d) {
		
		//Calculate position so it will be in the middle. To do this we need to know the width of the label with current font. 
		int movesLabelWidth = calcWidthOfString(g2d, "Move: " + this.getScore().getMoves());
		
		this.movesLabelxPos = (Window.WINDOW_WIDTH-movesLabelWidth)/2;
		this.movesLabelyPos = g2d.getFontMetrics().getHeight() + Window.TOP_CONTROLS_SIZE / 4;
		
		this.timeLabelxPos = Window.GAME_BORDER;
		this.timeLabelyPos = g2d.getFontMetrics().getHeight() + Window.TOP_CONTROLS_SIZE / 4;
		
		this.cogWheelXPos = Window.WINDOW_WIDTH - Window.GAME_BORDER - COGWHEEL_SIZE;
		this.cogWheelYPos = (Window.TOP_CONTROLS_SIZE - COGWHEEL_SIZE) / 2;
	}
	
	//Returns an array with the width of the labels according to how many digits there are. Goes from 0 to 4 digits. 
	private int[] calcStringWidths(Graphics2D g2d) {
		int[] stringWidths = new int[5];
		int counter = 1;
		stringWidths[0] = 0;
		for (int i = 1; i < stringWidths.length; i++) {
			stringWidths[i] = calcWidthOfString(g2d, Integer.toString(counter));
			counter *= 10;
		}
		
		return stringWidths;
	}
	
	//Returns the width of a given string with it's current font
	private int calcWidthOfString(Graphics g, String str) {
		FontMetrics fontMetrics = g.getFontMetrics(g.getFont());
		int width = fontMetrics.stringWidth(str);
		return width;
	}

	
	///// GETTERS FROM HERE //////
	
	public GameState getGameState() {
		return gameState;
	}

	public void updateGameState(GameState gs) {
		this.gameState = gs;
	}
	
	//Helper method to retrieve board from gameState. 
	public Board getBoard() {
		return this.gameState.getBoard();
	}
	
	//Helper method to retrieve score from gameState
	public Score getScore() {
		return this.gameState.getScore();
	}
	
	public Settings getSettings() {
		return this.gameState.getSettings();
	}
	
	public boolean isAnimating() {
		return this.animationInProgress;
	}
	
	public void setAnimationInProgress(boolean animationInProgress) {
		this.animationInProgress = animationInProgress;
	}
	
	
	
}
