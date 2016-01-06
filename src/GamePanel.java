import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Board board;
	private Score score;
	
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
		
		this.board = board;
		this.board.init();
		this.score = score;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Draw "Score" Values
		g.drawString("Moves: " + score.getMoves(), Window.GAME_BORDER, g.getFontMetrics().getHeight());
		g.drawString("Time: " + score.timeToString(), (Window.WINDOW_WIDTH / 2) - 16, g.getFontMetrics().getHeight());
		
		//Draw Board
		int[][] tiles = this.board.getTiles();
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles.length; x++) {
				if(tiles[x][y] != Math.pow(this.board.getBoardSize(),2)) {
					int xPos = (x * this.board.getTileSize()) + Window.GAME_BORDER;
					int yPos = Window.WINDOW_HEIGHT - Window.GAME_BORDER - ((this.getBoard().getBoardSize() - y) * this.getBoard().getTileSize());
					
					g.setColor(Color.gray);
					g.fillRect(xPos, yPos, this.board.getTileSize(), this.board.getTileSize());
					g.setColor(Color.black);
					g.drawRect(xPos, yPos, this.board.getTileSize(), this.board.getTileSize());
					g.setColor(Color.white);
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
