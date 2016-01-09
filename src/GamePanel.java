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

	public static final int MENUBUTTON_SIZE = Window.TOP_CONTROLS_SIZE - Window.TOP_CONTROLS_SIZE/4;
	public static final String RESOURCE_PATH = "resources/";
	public static final String THEME_PATH = RESOURCE_PATH + "themes/default/";
 	private static final long serialVersionUID = 1L;
	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private final Color TILE_TEXT_COLOR = Color.WHITE;
	private final Color LABEL_WHEN_CORNERED_BACKGROUND_COLOR = new Color(100,100,100,150);
	private GameState gameState;
	
	private Image boardImg;
	private Image menuButtonImg;
	private int movesLabelxPos;
	private int movesLabelyPos;
	private int menuButtonXPos;
	private int menuButtonYPos;
	private int timeLabelxPos;
	private int timeLabelyPos;
	private int[] stringWidths; //Saves width of strings depending how many characters are in. 
	private int stringHeight;
	private boolean firstPaint;
	private boolean animationInProgress;
	private BufferedImage[] picList;
	
	public GamePanel(GameState gs) {
		
		this.animationInProgress = false;
		this.setBounds(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
		this.setOpaque(true);
		
		this.gameState = gs;
		
		this.setBackground(BACKGROUND_COLOR);
				
		//Set doublebuffering to true. It should be by default, but just in case. 
		this.setDoubleBuffered(true);
		
		this.reset();
	}
	
	public void reset() {
		this.firstPaint = true;
		
		this.loadImages();
		
		this.repaint();
	}
	
	public void scrambleBoard() {
		//TODO: THIS ASKS THE MODEL TO DO STUFF
		LinkedList<Move> scramblingSequence = getBoard().makeRandomValidMoves(this.getSettings().getDifficulty());
		Timer scrambleAnimationTimer = new Timer(getSettings().getRefreshRate(), new MoveSequenceAnimator(this, scramblingSequence));
		scrambleAnimationTimer.start();
	}
	
	//1000 is a 1000milliseconds so the timer will fire each second. 
	private Timer clock = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// call the updateSeconds functions which adds a second to the scoreModel and updates the labeltext. 
			//TODO: THIS ASKS THE MODEL TO DO STUFF
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
	

	
	//TODO: THIS ASKS THE MODEL TO DO STUFF
	public void checkIfGameIsOver() {
		if (this.getBoard().isGameOver()){
			this.gameState.setGameDone(true);
		}
	}
	
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
		this.drawBoard(g2d, this.getBoard().getTileSize());
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
		
		g2d.setColor(LABEL_WHEN_CORNERED_BACKGROUND_COLOR);
		g2d.fillRect(xCoord, yCoord, cornerSize, cornerSize);
		
		this.drawLabelInCenter(g2d, x, y, xCoord, yCoord, cornerSize);
		
	}
	
	private void drawControls(Graphics2D g2d) {
		//Draw Time and Move labels.  
				g2d.drawString("Time: " + this.getScore().timeToString(), this.timeLabelxPos, this.timeLabelyPos);
				g2d.drawString("Moves: " + this.getScore().getMoves(), this.movesLabelxPos, this.movesLabelyPos);
			
				//Draw cogwheel (settings) button
				g2d.drawImage(menuButtonImg, menuButtonXPos, menuButtonYPos, MENUBUTTON_SIZE, MENUBUTTON_SIZE, null);
	}
	
	private void drawBoard(Graphics2D g2d, int tileSize) {
		Tile[][] tiles = this.getBoard().getTiles();
		for(int y = 0; y < this.getBoard().getTilesPerRow(); y++) {
			for(int x = 0; x < this.getBoard().getTilesPerRow(); x++) {
				//Draw all tiles except for the empty one.
				if(tiles[x][y].getNumber() != Math.pow(this.getBoard().getTilesPerRow(),2)) {
				
					//Get x and y position
					int xCoord = tiles[x][y].getX();
					int yCoord = tiles[x][y].getY();
					
					//Draws tile at x and y pos with image gotten from ressources. 
					g2d.drawImage(picList[tiles[x][y].getNumber() - 1 ], xCoord, yCoord, tileSize, this.getBoard().getTileSize(), null);
					
					//Draws text on image
					if(this.getSettings().isPictureOn()) {

						//Draw border around unfinished picture
						if (!this.gameState.isGameDone()){
							g2d.setColor(Color.BLACK);
							g2d.drawRect(xCoord, yCoord, tileSize, this.getBoard().getTileSize());
						} 
						
						//If a picture is showing and labels is on the label should be printed in the upper left corner. 
						if (this.getSettings().isLabelsOn()) {
							//TODO: How can we decide the size of this more appropriately? 
							//		Also the size of the text on the tile should be calculated.. 
							this.drawLabelInCorner(g2d, x, y, xCoord, yCoord, tileSize/3);
						} 
						
					} else { //No picture is showing, just draw label in center. 
						this.drawLabelInCenter(g2d, x, y, xCoord, yCoord, tileSize);
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
		ImageIcon menuButtonIc = new ImageIcon(RESOURCE_PATH + "inGameMenuIcon.png");
		this.menuButtonImg = menuButtonIc.getImage();
		
		//Create list of tileImages
		try {
			//If picture is set to on in settings, get the picture. Otherwise just get a plain image. 
			if (this.getSettings().isPictureOn()) {
				this.picList = ImageHandler.getTilePics(this.getBoard().getTilesPerRow(), this.getBoard().getTileSize(), RESOURCE_PATH + "pics/test", "jpg");
			} else {
				this.picList = ImageHandler.getTilePics(this.getBoard().getTilesPerRow(), this.getBoard().getTileSize(), RESOURCE_PATH + "pics/basic", "jpg");
			}
		} catch (IOException e) {
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
		
		this.menuButtonXPos = Window.WINDOW_WIDTH - Window.GAME_BORDER - MENUBUTTON_SIZE;
		this.menuButtonYPos = (Window.TOP_CONTROLS_SIZE - MENUBUTTON_SIZE) / 2;
	}
	
	//Returns an array with the width of the labels according to how many digits there are. Goes from 1 to 4 digits. 
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
