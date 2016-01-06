import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

	private static final String RESOURCE_PATH = "themes/default/";
	private static final long serialVersionUID = 1L;
	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private Board board;
	private Score score;
	private Image boardImg;
	private Image tileImg;
	
	
	//1000 is a 1000milliseconds so the timer will fire each second. 
	private Timer timer = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// call the updateSeconds functions which adds a second to the scoreModel and updates the labeltext. 
			getScore().addSeconds(1);
			repaint();
		}
		
	});
	
	public GamePanel(Board board, Score score) {
		this.setBounds(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
		this.setOpaque(true);
		
		this.setBackground(BACKGROUND_COLOR);
		this.board = board;
		this.board.init();
		this.score = score;
		this.loadImages();
	}
	
	
	private void loadImages() {
		//Load boardImage
		ImageIcon boardIc = new ImageIcon(RESOURCE_PATH + "board.jpeg");
		this.boardImg = boardIc.getImage();
		
		//Load tileImage
		ImageIcon tileIc = new ImageIcon(RESOURCE_PATH + "tile.jpeg");
		this.tileImg = tileIc.getImage();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Draw "Score" Values
		g.drawString("Moves: " + score.getMoves(), Window.GAME_BORDER, g.getFontMetrics().getHeight());
		g.drawString("Time: " + score.timeToString(), (Window.WINDOW_WIDTH / 2) - 16, g.getFontMetrics().getHeight());
		
	
		
		g.drawImage(boardImg, Window.GAME_BORDER, Window.WINDOW_HEIGHT - Window.GAME_BORDER - Window.BOARD_SIZE, Window.BOARD_SIZE, Window.BOARD_SIZE, null);
		
		//Draw Board
		int[][] tiles = this.board.getTiles();
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles.length; x++) {
				if(tiles[x][y] != Math.pow(this.board.getBoardSize(),2)) {
					
					int xPos = Window.GAME_BORDER + Window.BOARD_BORDER_SIZE + (x*this.board.getTileSize());
					//Y position is gotten from the bottom and then up. This way it will always have exactly the game_border to the bottom
					int yPos = Window.WINDOW_HEIGHT - Window.GAME_BORDER - ((this.getBoard().getBoardSize() - y) * (this.getBoard().getTileSize())) - Window.BOARD_BORDER_SIZE;
					
					//Draws tile at x and y pos with image gotten from ressources. 
					g.drawImage(tileImg, xPos, yPos, this.getBoard().getTileSize(), this.getBoard().getTileSize(), null);
					
					//Draws text on image
					g.setColor(Color.WHITE);
					g.drawString(Integer.toString(tiles[x][y]), xPos + (this.board.getTileSize() / 2), yPos + (this.board.getTileSize() / 2));
				}
			}
		}
		
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
