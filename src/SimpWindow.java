import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SimpWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_WIDTH = 448;
	public static final int WINDOW_HEIGHT = 512;
	public static final int GAME_BORDER = 24;
	
	
	//TODO: Do these need to be private? 
	Board board;
	Score score;
	SimpPuzzleView puzzleView;
	ControlView controlView;
	JLabel scoreLabel;
	JLabel timeLabel;
	
	public static void main(String[] args) {
		SimpWindow game = new SimpWindow(new Board(4));
	}
	
	public SimpWindow(Board board) {
		super("Simp-Puzzle");
		
		this.score = new Score();
		this.controlView = new ControlView(this.score);
		
		this.getContentPane().add(controlView, BorderLayout.NORTH);
		
		this.board = board;
		this.puzzleView = new SimpPuzzleView(this.board);
		
		puzzleView.setFocusable(true);
		
		SimpController controller = new SimpController(puzzleView, controlView);
		puzzleView.addKeyListener(controller);
		puzzleView.addMouseListener(controller);
		this.getContentPane().add(puzzleView, BorderLayout.CENTER);
		
		this.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(puzzleView);
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}