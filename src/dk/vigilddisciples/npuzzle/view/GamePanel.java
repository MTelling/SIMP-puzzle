package dk.vigilddisciples.npuzzle.view;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dk.vigilddisciples.npuzzle.ImageHandler;
import dk.vigilddisciples.npuzzle.NPuzzle;
import dk.vigilddisciples.npuzzle.model.Board;
import dk.vigilddisciples.npuzzle.model.GameState;
import dk.vigilddisciples.npuzzle.model.Highscore;
import dk.vigilddisciples.npuzzle.model.Score;
import dk.vigilddisciples.npuzzle.model.Tile;
import dk.vigilddisciples.npuzzle.model.WindowSize;

public class GamePanel extends JPanel {

	public static final int MENUBUTTON_SIZE = NPuzzle.getSettings().getCurrWindowSize().getTOP_CONTROLS_SIZE() 
			- NPuzzle.getSettings().getCurrWindowSize().getTOP_CONTROLS_SIZE()/4;
	public static final String RESOURCE_PATH = "resources/";
	public static final String THEME_PATH = RESOURCE_PATH + "themes/default/";
 	private static final long serialVersionUID = 1L;
	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private final Color TILE_TEXT_COLOR = Color.WHITE;
	private final Color LABEL_WHEN_CORNERED_BACKGROUND_COLOR = new Color(100,100,100,150);
	private GameState gameState;
	private Highscore[] highscores;
	
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
	private int labelTextSize;
	private boolean firstPaint;
	private BufferedImage[] picList;
	private int tilesNeededToBeLarger;
	
	private WindowSize currWindowSize;
	
	public GamePanel(GameState gs, Highscore[] highscores) {
		this.setBounds(0, 0, NPuzzle.getSettings().getCurrWindowSize().getWINDOW_WIDTH(), NPuzzle.getSettings().getCurrWindowSize().getWINDOW_HEIGHT());
		this.setOpaque(true);
		
		this.gameState = gs;
		this.highscores = highscores;
		
		this.setBackground(BACKGROUND_COLOR);
				
		//Set doublebuffering to true. It should be by default, but just in case. 
		this.setDoubleBuffered(true);
	}
	
	public void reset() {
		this.currWindowSize = NPuzzle.getSettings().getCurrWindowSize();
		this.labelTextSize = getGameState().getBoard().getTileSize()/4;
		
		this.firstPaint = true;
		
		this.loadImages();
		
		this.repaint();		
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();

		//Set font. Has to be done on each repaint, because it defaults it otherwise. 
		g2d.setFont(new Font("Sans Serif", Font.PLAIN, currWindowSize.getCONTROLS_TEXT_SIZE()));
		
		int boardSize = currWindowSize.getBOARD_SIZE();
		int gameBorder = currWindowSize.getGAME_BORDER();
		int windowHeight = currWindowSize.getWINDOW_HEIGHT();
		
		//Only calculate labelpositions first time round. 
		//We do this so it doesn't have to calculate the positions every single time the view is repainted. 
		//It only saves a bit cpu, but we think it's worth it. 
		if (this.firstPaint) {
			calcItemPositions(g2d, currWindowSize.getWINDOW_WIDTH(), currWindowSize.getTOP_CONTROLS_SIZE(), gameBorder);
			//Determine how many tiles need to have an extra pixel. 
			tilesNeededToBeLarger = (currWindowSize.getBOARD_SIZE() - currWindowSize.getBOARD_BORDER_SIZE()*2) % this.getBoard().getTilesPerRow();
		}
		
		//Draw everything above the board.
		this.drawControls(g2d);
		
		//Draw board background. 
		
		g2d.drawImage(boardImg, gameBorder, windowHeight - gameBorder - boardSize, boardSize, boardSize, null);
		
		//Set font for text when picture and labels are on
		if (NPuzzle.getSettings().isPictureOn() && NPuzzle.getSettings().isLabelsOn()) {
			g2d.setFont(new Font("Sans Serif", Font.ITALIC, this.getBoard().getTileSize()/5));
		} else { //No picture is showing. Set font to usual
			g2d.setFont(new Font("Sans Serif", Font.ITALIC, labelTextSize));
		}
		
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
		
		int width = cornerSize, height = cornerSize;
		//add rectangle behind the label for readability
		//If the tilenumber is larger than 99, make the label a bit wider. 
		if (getBoard().getTiles()[x][y].getNumber() > 99) {
			width *= 1.3;
		}
		g2d.setColor(LABEL_WHEN_CORNERED_BACKGROUND_COLOR);
		g2d.fillRect(xCoord, yCoord, width, height);
		
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
		int extraX = 0, extraY = 0;
		
		Tile[][] tiles = this.getBoard().getTiles();
		for(int y = 0; y < this.getBoard().getTilesPerRow(); y++) {
			extraX = 0;
			int tileHeight = tileSize;
			//Determine if the tile should have an extra pixel in the height. 
			if (extraY < tilesNeededToBeLarger) {
				tileHeight++;
			}
			for(int x = 0; x < this.getBoard().getTilesPerRow(); x++) {
				//Draw all tiles except for the empty one. If the game is done and pictures are shown. Show the full image. 
				if(tiles[x][y].getNumber() != Math.pow(this.getBoard().getTilesPerRow(),2) || (this.gameState.isGameDone() && NPuzzle.getSettings().isPictureOn())) {
				
					//Get x and y position
					int xCoord = tiles[x][y].getX() + extraX;
					int yCoord = tiles[x][y].getY() - (tilesNeededToBeLarger - extraY);
					
					//Determine if the tile should have an extra pixel in width. 
					int tileWidth = tileSize;
					if (extraX < tilesNeededToBeLarger) {
						extraX++;
						tileWidth++;
					}
					
					//Draws tile at x and y pos with image gotten from ressources. 
					
					g2d.drawImage(picList[tiles[x][y].getNumber() - 1 ], xCoord, yCoord, tileWidth, tileHeight, null);
					
					//Draws text on image
					if(NPuzzle.getSettings().isPictureOn()) {

						//Draw border around unfinished picture
						if (!this.gameState.isGameDone()){
							g2d.setColor(Color.BLACK);
							g2d.drawRect(xCoord, yCoord, tileWidth, tileHeight);
							
							//Draw labels on unfinished picture
							if (this.gameState.isLabelsOn()) {
								this.drawLabelInCorner(g2d, x, y, xCoord, yCoord, tileSize/3);
							}
						} 
						
					} else { //No picture is showing, just draw label in center. 
						this.drawLabelInCenter(g2d, x, y, xCoord, yCoord, tileSize);
					}
					
				}
			}
			if (extraY < tilesNeededToBeLarger) {
				extraY++;
			}
		}
	}
	
	private void loadImages() {
		//Load boardImage
		ImageIcon boardIc = new ImageIcon(THEME_PATH + "board" + NPuzzle.getSettings().getCurrWindowSize().getBOARD_SIZE() +".png");
		this.boardImg = boardIc.getImage();
		
		//Load cogwheelImage
		ImageIcon menuButtonIc = new ImageIcon(RESOURCE_PATH + "inGameMenuIcon.png");
		this.menuButtonImg = menuButtonIc.getImage();
		
		//Create list of tileImages
		try {
			//If picture is set to on in settings, get the picture. Otherwise just get a plain image. 
			if (NPuzzle.getSettings().isPictureOn()) {
				this.picList = ImageHandler.getTilePics(this.getBoard().getTilesPerRow(), this.getBoard().getTileSize(), NPuzzle.getSettings().getGamePicture());
			} else {
				this.picList = ImageHandler.getTilePics(this.getBoard().getTilesPerRow(), this.getBoard().getTileSize(), RESOURCE_PATH + "pics/basic.jpg");
			}
		} catch (IOException e) {
			System.out.println("IOException, error loading image somehow");
		} catch (RasterFormatException e) {
			//If the image can't be loaded. Reset to basic without pciture and repaint the view. 
			JOptionPane.showMessageDialog(null, "Error loading image. You have probably changed window size, since you chose it.\n"+
					"I've reset to numbers for you. If you want to use a picture, go into settings and choose a new", "Image error", JOptionPane.ERROR_MESSAGE);
			NPuzzle.getSettings().setPictureOn(false);
			NPuzzle.getSettings().setGamePicture(RESOURCE_PATH + "pics/basic.jpg"); 
			//Reload images. 
			loadImages();
			this.repaint();
		}
	}
	
	//Helper method to calculate label positions. 
	private void calcItemPositions(Graphics2D g2d, int windowWidth, int topControlsSize, int gameBorder) {
		
		//Calculate position so it will be in the middle. To do this we need to know the width of the label with current font. 
		int movesLabelWidth = calcWidthOfString(g2d, "Move: " + this.getScore().getMoves());
		
		this.movesLabelxPos = (windowWidth-movesLabelWidth)/2;
		this.movesLabelyPos = g2d.getFontMetrics().getHeight() + topControlsSize / 4;
		
		this.timeLabelxPos = gameBorder;
		this.timeLabelyPos = g2d.getFontMetrics().getHeight() + topControlsSize / 4;
		
		this.menuButtonXPos = windowWidth - gameBorder - MENUBUTTON_SIZE;
		this.menuButtonYPos = (topControlsSize - MENUBUTTON_SIZE) / 2;
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
	
	public Highscore getHighscore(int difficulty) {
		return this.highscores[difficulty - 1];
	}
	
}
