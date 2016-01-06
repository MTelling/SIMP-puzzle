import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static final int GAME_BORDER = 24;
	public static final int BOARD_SIZE = 400;
	public static final int BOARD_BORDER_SIZE = 4;
	public static final int TOP_CONTROLS_SIZE = 24;
	public static final int LABEL_TEXT_SIZE = 16;
	public static final int WINDOW_WIDTH = BOARD_SIZE + 2 * GAME_BORDER;
	public static final int WINDOW_HEIGHT = BOARD_SIZE + 2 * GAME_BORDER + TOP_CONTROLS_SIZE;
	
	private static CardLayout cardLayout;
	private static JPanel cardPanel;
	private MainMenuPanel mainMenuPanel;
	private static GamePanel gamePanel;
	private static MenuPanel menuPanel;
	
	public static boolean menuToggle;
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Window game = new Window();
	}
	
	public Window() {
		super("N-Puzzle Game");
		
		//Create CardLayout
		cardPanel = new JPanel();
		this.getContentPane().add(cardPanel);
		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);
		cardPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		//Initialize the different panels
		mainMenuPanel = new MainMenuPanel();
		mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
		
		JLayeredPane puzzlePane = new JLayeredPane();
		gamePanel = new GamePanel(new Board(4), new Score());
		SimpController controller = new SimpController(gamePanel);
		gamePanel.addKeyListener(controller);
		gamePanel.addMouseListener(controller);
		menuPanel = new MenuPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		menuPanel.setVisible(false);
		puzzlePane.add(gamePanel, new Integer(0), 0);
		puzzlePane.add(menuPanel, new Integer(1), 0);
		
		//Add the different panels to the CardLayout
		cardPanel.add(mainMenuPanel, "mainMenu");
		cardPanel.add(puzzlePane, "puzzle");
		
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void swapView(String key) {
		cardLayout.show(cardPanel, key);
		if(key.equals("puzzle")) {
			gamePanel.requestFocus();
		}
	}
	
	public static void toggleMenu() {
		menuPanel.setVisible(menuToggle);
		menuToggle = !menuToggle;
	}
}
