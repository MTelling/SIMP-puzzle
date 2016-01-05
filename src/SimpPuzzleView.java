import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class SimpPuzzleView extends JPanel {

	private static final long serialVersionUID = 1L;
	private Board board;
	private int tileSize;
	
	public SimpPuzzleView(Board board) {
		this.board = board;
		this.board.init();
		this.tileSize = (SimpWindow.WINDOW_WIDTH - SimpWindow.GAME_BORDER * 2) / board.getBoardSize();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
				
		int[][] tiles = this.board.getTiles();
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles.length; x++) {
				if(tiles[x][y] != 0) {
					int xPos = (x * tileSize) + SimpWindow.GAME_BORDER;
					int yPos = (y * tileSize) + SimpWindow.GAME_BORDER;
					
					g.setColor(Color.gray);
					g.fillRect(xPos, yPos, tileSize, tileSize);
					g.setColor(Color.black);
					g.drawRect(xPos, yPos, tileSize, tileSize);
					g.setColor(Color.white);
					g.drawString(Integer.toString(tiles[x][y]), xPos + (tileSize / 2), yPos + (tileSize / 2));
				}
			}
		}
	}

	public Board getBoard() {
		return board;
	}

	public int getTileSize() {
		return tileSize;
	}
	
}
