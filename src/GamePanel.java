import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

	private static final String RESOURCE_PATH = "resources/";
	private static final String THEME_PATH = RESOURCE_PATH + "themes/default/";
	private static final long serialVersionUID = 1L;
	private static final int COGWHEEL_SIZE = Window.TOP_CONTROLS_SIZE*2 - Window.TOP_CONTROLS_SIZE/2;
	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private final Color TILE_TEXT_COLOR = Color.WHITE;
	private Board board;
	private Score score;
	private GameState gameState;
	private Image boardImg;
	private Image tileImg;
	private Image cogwheelImg;
	private int movesLabelxPos;
	private int movesLabelyPos;
	private int timeLabelxPos;
	private int timeLabelyPos;
	private boolean firstPaint;
	
	
	//1000 is a 1000milliseconds so the timer will fire each second. 
	private Timer timer = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// call the updateSeconds functions which adds a second to the scoreModel and updates the labeltext. 
			getScore().addSeconds(1);
			repaint();
		}
		
	});
	
	public GameState getGameState() {
		return gameState;
	}
	
	public GamePanel(Board board, Score score) {
		this.setBounds(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
		this.setOpaque(true);
		
		this.setBackground(BACKGROUND_COLOR);
		this.board = board;
		this.board.init();
		this.score = score;
		this.gameState = new GameState(this.board, this.score);
		this.firstPaint = true;
		
		this.loadImages();
		
	}
	
	
	private void loadImages() {
		//Load boardImage
		ImageIcon boardIc = new ImageIcon(THEME_PATH + "board.jpeg");
		this.boardImg = boardIc.getImage();
		
		//Load tileImage
		ImageIcon tileIc = new ImageIcon(THEME_PATH + "tile.jpeg");
		this.tileImg = tileIc.getImage();
		
		//Load cogwheelImage
		ImageIcon cogwheelIc = new ImageIcon(RESOURCE_PATH + "cogwheel.png");
		this.cogwheelImg = cogwheelIc.getImage();
	}
	
	//TODO: Can we move a lot of code out of this that does not need to be calculated at each repaint? 
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Set font. Has to be done on each repaint, because it defaults it otherwise. 
		g.setFont(new Font("Sans Serif", Font.PLAIN, Window.LABEL_TEXT_SIZE));
		
		//Only calculate labelpositions first time round. 
		//We do this so it doesn't have to calculate the positions every single time the view is repainted. 
		//It only saves a bit cpu, but we think it's worth it. 
		if (this.firstPaint) {
			calculateLabelPositions(g);
			this.firstPaint = false;
		}
		
		//Draw Time and Move labels.  
		g.drawString("Time: " + score.timeToString(), this.timeLabelxPos, this.timeLabelyPos);
		g.drawString("Moves: " + score.getMoves(), this.movesLabelxPos, this.movesLabelyPos);
	
		//Draw cogwheel (settings) button
		int cogWheelXPos = Window.WINDOW_WIDTH - Window.GAME_BORDER - COGWHEEL_SIZE;
		int cogWheelYPos = (Window.TOP_CONTROLS_SIZE*2 - COGWHEEL_SIZE) / 2;
		g.drawImage(cogwheelImg, cogWheelXPos, cogWheelYPos, COGWHEEL_SIZE, COGWHEEL_SIZE, null);
		
		//Draw board background. 
		g.drawImage(boardImg, Window.GAME_BORDER, Window.WINDOW_HEIGHT - Window.GAME_BORDER - Window.BOARD_SIZE, Window.BOARD_SIZE, Window.BOARD_SIZE, null);
		
		//TODO: Somehow the tiles are positioned a bit off the y position at other boardsizes than 4. 
		g.setFont(new Font("Sans Serif", Font.ITALIC, Window.LABEL_TEXT_SIZE));
		//Draw Board
		int[][] tiles = this.board.getTiles();
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles.length; x++) {
				if(tiles[x][y] != Math.pow(this.board.getBoardSize(),2)) {
					
					int xPos = Window.GAME_BORDER + Window.BOARD_BORDER_SIZE + (x*this.board.getTileSize());
					//Y position is gotten from the bottom and then up. This way it will always have exactly distance to bottom if the top is changed. 
					int yPos = Window.WINDOW_HEIGHT - Window.GAME_BORDER - ((this.getBoard().getBoardSize() - y) * (this.getBoard().getTileSize())) - Window.BOARD_BORDER_SIZE;
					
					//Draws tile at x and y pos with image gotten from ressources. 
					g.drawImage(tileImg, xPos, yPos, this.getBoard().getTileSize(), this.getBoard().getTileSize(), null);
					
					//Draws text on image
					g.setColor(TILE_TEXT_COLOR);
					
					//calculate position for labels on tiles
					String TileNum = Integer.toString(tiles[x][y]);
					int digitWidth = calcStringWidth(g, TileNum);
					int strXPos = xPos + (this.board.getTileSize() / 2) - digitWidth / 2;
					int strYPos = yPos + (this.board.getTileSize() / 2) + g.getFontMetrics().getHeight()/4;
					
					//Draw text for each tile
					g.drawString(TileNum, strXPos, strYPos);
				}
			}
		}
		
	}
	
	//Helper method to calculate label positions. 
	private void calculateLabelPositions(Graphics g) {
		
		//Calculate position so it will be in the middle. To do this we need to know the width of the label with current font. 
		int movesLabelWidth = calcStringWidth(g, "Move: " + this.score.getMoves());
		
		this.movesLabelxPos = (Window.WINDOW_WIDTH-movesLabelWidth)/2;
		this.movesLabelyPos = g.getFontMetrics().getHeight() + Window.TOP_CONTROLS_SIZE / 2;
		
		this.timeLabelxPos = Window.GAME_BORDER;
		this.timeLabelyPos = g.getFontMetrics().getHeight() + Window.TOP_CONTROLS_SIZE / 2;
	}
	
	//Returns the width of a given string with it's current font
	private int calcStringWidth(Graphics g, String str) {
		FontMetrics fontMetrics = g.getFontMetrics(g.getFont());
		int width = fontMetrics.stringWidth(str);
		return width;
	}
	
	public void startTiming () {
		timer.start();
	}
	
	public void stopTiming() {
		timer.stop();
	}

	public Board getBoard() {
		return this.board;
	}
	
	public Score getScore() {
		return this.score;
	}
}
