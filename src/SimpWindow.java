import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SimpWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 450;
	public static final int GAME_BORDER = 24;
	
	
	//TODO: Do these need to be private? 
	private Board board;
	private Score score;
	private SimpPuzzleView puzzleView;
	private ControlView controlView;
	
	public static void main(String[] args) {

		@SuppressWarnings("unused")
		SimpWindow game = new SimpWindow(new Board(Integer.parseInt(args[0])));

	}
	
	public SimpWindow(Board board) {
		super("N-Puzzle");
		
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