import java.awt.Dimension;

public enum WindowSize {
	SMALL(400), MEDIUM(600), LARGE(800);
	
	private final int GAME_BORDER;
	private final int BOARD_SIZE;
	private final int BOARD_BORDER_SIZE;
	private final int TOP_CONTROLS_SIZE;
	private final int CONTROLS_TEXT_SIZE;
	
	
	private final int WINDOW_WIDTH;
	private final int WINDOW_HEIGHT;
	
	private WindowSize(int boardSize) {
		BOARD_SIZE = boardSize;
		GAME_BORDER = 24;
		BOARD_BORDER_SIZE = 4;
		TOP_CONTROLS_SIZE = 48;
		CONTROLS_TEXT_SIZE = 16;
		WINDOW_WIDTH = BOARD_SIZE + 2 * GAME_BORDER;
		WINDOW_HEIGHT = GAME_BORDER + BOARD_SIZE + TOP_CONTROLS_SIZE;
	}

	public int getGAME_BORDER() {
		return GAME_BORDER;
	}

	public int getBOARD_SIZE() {
		return BOARD_SIZE;
	}

	public int getBOARD_BORDER_SIZE() {
		return BOARD_BORDER_SIZE;
	}

	public int getTOP_CONTROLS_SIZE() {
		return TOP_CONTROLS_SIZE;
	}

	public int getWINDOW_WIDTH() {
		return WINDOW_WIDTH;
	}

	public int getWINDOW_HEIGHT() {
		return WINDOW_HEIGHT;
	}
	
	public Dimension getDimension() {
		return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public int getCONTROLS_TEXT_SIZE() {
		return CONTROLS_TEXT_SIZE;
	}
	
}
