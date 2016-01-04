import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SimpWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_WIDTH = 448;
	public static final int WINDOW_HEIGHT = 512;
	public static final int GAME_BORDER = 24;
	
	Board board;
	SimpPuzzleView puzzleView;
	ControlView controlView;
	JLabel scoreLabel;
	JLabel timeLabel;
	
	public static void main(String[] args) {
		SimpWindow game = new SimpWindow(new Board(7));
	}
	
	public SimpWindow(Board board) {
		super("Simp-Puzzel");
		
		this.controlView = new ControlView();
		
		JButton menuButton = new JButton("Menu");
		menuButton.setFocusable(false);
		scoreLabel = new JLabel("Score: ");
		timeLabel = new JLabel("Time Spent: ");
		
		controlView.add(menuButton);
		controlView.add(scoreLabel);
		controlView.add(timeLabel);
		
		this.getContentPane().add(controlView, BorderLayout.NORTH);
		
		this.board = board;
		this.puzzleView = new SimpPuzzleView(board);
		
		puzzleView.setFocusable(true);
		
		SimpController controller = new SimpController(puzzleView);
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