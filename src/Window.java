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
	public static final int TOP_CONTROLS_SIZE = 48;
	public static final int LABEL_TEXT_SIZE = 16;
	public static final int WINDOW_WIDTH = BOARD_SIZE + 2 * GAME_BORDER;
	public static final int WINDOW_HEIGHT = GAME_BORDER + BOARD_SIZE + TOP_CONTROLS_SIZE;
	
	private static Settings settings;
	private static CardLayout cardLayout;
	private static JPanel cardPanel;
	private static MainMenuPanel mainMenuPanel;
	private static SettingsPanel settingsPanel;
	private static GamePanel gamePanel;
	private static InGameMenuPanel inGameMenuPanel;
	
	public static boolean menuToggle;
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Window game = new Window();
	}

	
	public Window() {
		super("N-Puzzle Game");
		
		//Initialize the model.
		settings = new Settings();
		Board board = new Board(settings.getTilesPerRowInBoard());
		board.init();
		Score score = new Score();	
		GameState gs = new GameState(board, score);
				
		//Create CardLayout
		cardPanel = new JPanel();
		this.getContentPane().add(cardPanel);
		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);
		cardPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		//Create Controller
		gamePanel = new GamePanel(gs);
		SimpController controller = new SimpController(gamePanel);
		
		//Create mainManuPanel
		mainMenuPanel = new MainMenuPanel(controller);
		mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
		
		//Create settings panel and give it an instance of settings
		settingsPanel = new SettingsPanel(settings);
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		
		//Create puzzlePane. 
		JLayeredPane puzzlePane = new JLayeredPane();
		
		//Create inGameMenuPanel initially not visible
		inGameMenuPanel = new InGameMenuPanel(controller);
		inGameMenuPanel.setLayout(new BoxLayout(inGameMenuPanel, BoxLayout.Y_AXIS));
		inGameMenuPanel.setVisible(false);
		
		//Add gamePanel and inGameMenuPanel to puzzlePane
		puzzlePane.add(gamePanel, new Integer(0), 0);
		puzzlePane.add(inGameMenuPanel, new Integer(1), 0);

		//Add controller to panels
		gamePanel.addKeyListener(controller);
		gamePanel.addMouseListener(controller);
		gamePanel.addMouseMotionListener(controller);		
		
		//Add the different panels to the CardLayout
		cardPanel.add(mainMenuPanel, "mainMenu");
		cardPanel.add(settingsPanel, "settings");
		cardPanel.add(puzzlePane, "puzzle");
		
		//Set settings for main window. 
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static Settings getSettings(){
		return settings;
	}
	
	public static void loadGame(GameState gs) {
		gamePanel.updateGameState(gs);
		cardLayout.show(cardPanel, "puzzle");
		gamePanel.requestFocus();
	}
	
	public static void swapView(String key) {
		cardLayout.show(cardPanel, key);
		if(key.equals("puzzle")) {
			gamePanel.requestFocus();
		} else if(key.equals("mainMenu")) {
			toggleMenu(false);
			gamePanel.stopClock();
		}
	}
	
	public static void toggleMenu(boolean shouldStartTimer) {
		menuToggle = !menuToggle;
		inGameMenuPanel.setVisible(menuToggle);
		if(menuToggle || !(shouldStartTimer)) {
			gamePanel.stopClock();
		} else {
			gamePanel.startClock();
		}
		//Fixes issue of gamePanel being painted wrong when new game is started. 
		gamePanel.repaint();
	}
}
