package dk.vigilddisciples.npuzzle.view;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dk.vigilddisciples.npuzzle.ImageHandler;
import dk.vigilddisciples.npuzzle.NPuzzle;
import dk.vigilddisciples.npuzzle.model.AnimationSpeed;
import dk.vigilddisciples.npuzzle.model.Difficulty;
import dk.vigilddisciples.npuzzle.model.Settings;
import dk.vigilddisciples.npuzzle.model.WindowSize;


public class SettingsPanel extends JPanel implements ActionListener, ChangeListener{

	private static final long serialVersionUID = 1L;


	private JButton exitButton;
	private JButton choosePicture;
	private JButton setBoardSizeButton;
	private Box labelsToggleContainer; //So you can hide the "show labels in corner of tile" if game type is set to numbers.
	private JTextField boardSizeChooser;
	private JSlider fpsSlider;

	private JRadioButton gameTypeNumbers;
	private JRadioButton gameTypePicture;
	private JRadioButton animationOn;
	private JRadioButton animationOff;
	private JRadioButton normalControls;
	private JRadioButton invertedControls;
	private JRadioButton labelsOn;
	private JRadioButton labelsOff;
	private JRadioButton slowAnimation;
	private JRadioButton mediumAnimation;
	private JRadioButton fastAnimation;
	private JRadioButton easyGame;
	private JRadioButton mediumGame;
	private JRadioButton hardGame;
	private JRadioButton smallWindow;
	private JRadioButton largeWindow;
	private JRadioButton mediumWindow;
	private JRadioButton scrambleAnimationOn;
	private JRadioButton scrambleAnimationOff;

	private NPuzzle window;
	private Settings settings;


	public SettingsPanel(Settings settings, NPuzzle window) {
		this.settings = settings;
		this.window = window;

        //Create the entire GUI. We use vertical glue to position evenly distrubuted on the y-axis.
		this.add(Box.createVerticalGlue());

		createFramesPerSecondChooser();
		this.add(Box.createVerticalGlue());

		createBoardSizeChooser();
		this.add(Box.createVerticalGlue());

		createDifficultyChooser();
		this.add(Box.createVerticalGlue());

		createControlsChooser();
		this.add(Box.createVerticalGlue());

		createAnimationToggle();
		this.add(Box.createVerticalGlue());

		createAnimationSpeedChooser();
		this.add(Box.createVerticalGlue());

		createScrambleAnimationChooser();
		this.add(Box.createVerticalGlue());

		createWindowSizeChooser();
		this.add(Box.createVerticalGlue());

		createGameTypeChooser();
		this.add(Box.createVerticalGlue());

		createLabelsOnPictureChooser();
		this.add(Box.createVerticalGlue());

		createPictureChooser();
		this.add(Box.createVerticalGlue());

		this.addButton(exitButton, "Close settings");
		this.add(Box.createVerticalGlue());

		this.loadSettings();

	}

	/// ActionListener from here ///

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {

		//animation on or off
		case "animationOn": 
			settings.setAnimationOn(true); 
			break;
		case "animationOff": 
			settings.setAnimationOn(false); 
			break;

			//Game type picture or numbers
		case "gameTypeNumbers": 
			settings.setPictureOn(false); 
			loadGameTypeSetting(); 
			break;
		case "gameTypePicture": 
			settings.setPictureOn(true); 
			loadGameTypeSetting(); 
			break;

			//Set labels in corners on or off
		case "labelsOn":
			settings.setLabelsOn(true);
			break;
		case "labelsOff":
			settings.setLabelsOn(false);
			break;

			//Set controls to normal/inverted
		case "normalControls":
			settings.setControlsNormal();
			break;
		case "invertedControls":
			settings.setControlsInverted();
			break;


			//Set board size
		case "setBoardSize": 
			//Regular expression to remove everything but digits from the string.
			String newBoardSize = boardSizeChooser.getText().replaceAll("[^0-9]+", "");

			//Try to convert to int. If it doesn't succeed set back to original num.
			//Should only happen if the field doesn't contain any integers at all. 
			try {
				int boardSize = Integer.parseInt(newBoardSize);

				//Check if it's a valid boardSize and set it or revert to the one before. 
				if (boardSize >= 3 && boardSize <= 100) {
					settings.setTilesPerRowInBoard(boardSize);
					boardSizeChooser.setText(newBoardSize);
				} else {
					boardSizeChooser.setText("" + settings.getTilesPerRowInBoard());
				}
			} catch (Exception exc){
				boardSizeChooser.setText("" + settings.getTilesPerRowInBoard());
			}
			break;

			//Set animation speed
		case "slowAnimation":
			settings.setAnimationSpeed(AnimationSpeed.SLOW);
			break;
		case "mediumAnimation":
			settings.setAnimationSpeed(AnimationSpeed.MEDIUM);
			break;
		case "fastAnimation":
			settings.setAnimationSpeed(AnimationSpeed.FAST);
			break;

		//Set difficulty
		case "easyGame":
			settings.setDifficulty(Difficulty.EASY.getValue());
			break;
		case "mediumGame":
			settings.setDifficulty(Difficulty.MEDIUM.getValue());
			break;
		case "hardGame":
			settings.setDifficulty(Difficulty.HARD.getValue());
			break;
			
		case "chooseNewPicture":
			String imagePath = ImageHandler.loadPicture();
			if(imagePath != null) {
				BufferedImage image = null;
				try {
					image = ImageIO.read(new File(imagePath));
				} catch (IOException e1) { }
				
				int boardSize = settings.getCurrWindowSize().getBOARD_SIZE();
				if(image != null && (image.getWidth() == boardSize && image.getHeight() == boardSize)) {
					settings.setGamePicture(imagePath);
				} else if(image != null && (image.getWidth() > boardSize && image.getHeight() >= boardSize)
						|| (image.getWidth() >= boardSize && image.getHeight() > boardSize)){
					NPuzzle.swapView("imageCrop");
					ImageCropPanel.setImageToCrop(image);
				} else {
					JOptionPane.showMessageDialog(null, "Image is too small for the current window size", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			break;
				
			//Close settings window
		case "Close settings":
			settings.saveSettings();
			NPuzzle.swapView("mainMenu");
			break;


			//Change size of window
		case "smallWindow":
			settings.setCurrWindowSize(WindowSize.SMALL);
			window.setNewSize(settings.getCurrWindowSize().getDimension());
			break;
		case "mediumWindow":
			settings.setCurrWindowSize(WindowSize.MEDIUM);
			window.setNewSize(settings.getCurrWindowSize().getDimension());
			break;
		case "largeWindow":
			settings.setCurrWindowSize(WindowSize.LARGE);
			window.setNewSize(settings.getCurrWindowSize().getDimension());
			break;


			//Set animation scramble to on or off. 
		case "scrambleAnimationOn": 
			settings.setAnimationScramblingOn(true);
			break;
		case "scrambleAnimationOff":
			settings.setAnimationScramblingOn(false);
			break;

		default: break;
		}

	}

	//This listens to the fps slider.
	@Override
	public void stateChanged(ChangeEvent e) {

		Object source = e.getSource();

		//Set fps when the slider is moved. 
		if (source instanceof JSlider) {
			if(!fpsSlider.getValueIsAdjusting() && fpsSlider.getValue() != settings.getFramesPerSecond()) {
				settings.setFramesPerSecond(fpsSlider.getValue());
			}
		}

	}


	/// Methods to set values to what they are in settings ///

	//Set all settings to what they are in settings class.
	public void loadSettings() {
		loadDifficultySetting();
		loadGameTypeSetting();
		loadFpsSetting();
		loadControlsSetting();
		loadBoardSizeSetting();
		loadLabelsSetting();
		loadAnimationSpeed();
		loadAnimationSetting();
		loadWindowSize();
		loadAnimationScrambleSetting();
	}

	private void loadAnimationScrambleSetting() {
		if (settings.isAnimationScramblingOn()) {
			scrambleAnimationOn.setSelected(true);
		} else {
			scrambleAnimationOff.setSelected(true);
		}
	}

	private void loadDifficultySetting() {
		if (settings.getDifficulty() == Difficulty.EASY.getValue()) {
			easyGame.setSelected(true);
		} else if (settings.getDifficulty() == Difficulty.MEDIUM.getValue()) {
			mediumGame.setSelected(true);
		} else {
			hardGame.setSelected(true);
		}
	}

	private void loadWindowSize() {
		if (settings.getCurrWindowSize() == WindowSize.SMALL) {
			smallWindow.setSelected(true);
		} else if (settings.getCurrWindowSize() == WindowSize.MEDIUM) {
			mediumWindow.setSelected(true);
		} else {
			largeWindow.setSelected(true);
		}
	}

	private void loadGameTypeSetting() {
		if (settings.isPictureOn()) {
			gameTypePicture.setSelected(true);
			choosePicture.setVisible(true);
			labelsToggleContainer.setVisible(true);
		} else {
			gameTypeNumbers.setSelected(true);
			choosePicture.setVisible(false);
			labelsToggleContainer.setVisible(false);
		}
	}

	private void loadFpsSetting() {
		fpsSlider.setValue(settings.getFramesPerSecond());
	}

	private void loadControlsSetting() {
		if (settings.isControlsNormal()) {
			normalControls.setSelected(true);
		} else {
			invertedControls.setSelected(true);
		}
	}

	private void loadAnimationSetting() {
		if (settings.isAnimationOn()) {
			animationOn.setSelected(true);
		} else {
			animationOff.setSelected(true);
		}
	}

	private void loadBoardSizeSetting() {
		boardSizeChooser.setText("" + settings.getTilesPerRowInBoard());
	}

	private void loadLabelsSetting() {
		if (settings.isLabelsOn()) {
			labelsOn.setSelected(true);
		} else {
			labelsOff.setSelected(true);
		}
	}

	private void loadAnimationSpeed() {
		if (settings.getAnimationSpeed().equals(AnimationSpeed.SLOW)) {
			slowAnimation.setSelected(true);
		} else if (settings.getAnimationSpeed().equals(AnimationSpeed.MEDIUM)) {
			mediumAnimation.setSelected(true);
		} else {
			fastAnimation.setSelected(true);
		}
	}


	/// Methods for creating UI ///

	private Box containerForComponents(JComponent[] components, String labelText) {
		ButtonGroup buttonGroup = new ButtonGroup();
		Box container = new Box(BoxLayout.LINE_AXIS);
		container.add(new JLabel(labelText));
		for (JComponent component: components) {
			//Add to container
			container.add(component);

			if (component instanceof JRadioButton) {
				//Add to its button group.
				buttonGroup.add((JRadioButton)component);

				//Add actionListener to radioButton
				((JRadioButton)component).addActionListener(this);
			} else if (component instanceof JButton) {
				((JButton)component).addActionListener(this);
			} else if (component instanceof JSlider) {
				((JSlider)component).addChangeListener(this);
			}
		}

		return container;
	}

	private void createScrambleAnimationChooser() {
		scrambleAnimationOn = new JRadioButton("On");
		scrambleAnimationOn.setActionCommand("scrambleAnimationOn");
		scrambleAnimationOff = new JRadioButton("Off");
		scrambleAnimationOff.setActionCommand("scrambleAnimationOff");

		this.add(containerForComponents(new JRadioButton[] {scrambleAnimationOn, scrambleAnimationOff}, "Show scramble animation: "));
	}

	private void createGameTypeChooser() {
		gameTypeNumbers = new JRadioButton("Numbers");
		gameTypeNumbers.setActionCommand("gameTypeNumbers");
		gameTypePicture = new JRadioButton("Picture");
		gameTypePicture.setActionCommand("gameTypePicture");

		this.add(containerForComponents(new JRadioButton[] {gameTypeNumbers, gameTypePicture}, "Game Type: "));	
	}

	private void createDifficultyChooser() {
		easyGame = new JRadioButton("Easy");
		easyGame.setActionCommand("easyGame");
		mediumGame = new JRadioButton("Medium");
		mediumGame.setActionCommand("mediumGame");
		hardGame = new JRadioButton("EXTREME");
		hardGame.setActionCommand("hardGame");

		this.add(containerForComponents(new JRadioButton[]{easyGame,mediumGame,hardGame}, "Difficulty: "));

	}

	private void createAnimationToggle() {
		animationOn = new JRadioButton("On");
		animationOn.setActionCommand("animationOn");
		animationOff = new JRadioButton("Off");
		animationOff.setActionCommand("animationOff");

		this.add(containerForComponents(new JRadioButton[] {animationOn, animationOff}, "Show move animation: "));

	}

	private void createControlsChooser() {
		normalControls = new JRadioButton("Normal");
		normalControls.setActionCommand("normalControls");
		invertedControls = new JRadioButton("Inverted");
		invertedControls.setActionCommand("invertedControls");

		this.add(containerForComponents(new JRadioButton[] {normalControls, invertedControls}, "Controls: "));	
	}

	private void createAnimationSpeedChooser() {
		slowAnimation = new JRadioButton("Slow");
		slowAnimation.setActionCommand("slowAnimation");
		mediumAnimation = new JRadioButton("Medium");
		mediumAnimation.setActionCommand("mediumAnimation");
		fastAnimation = new JRadioButton("Fast");
		fastAnimation.setActionCommand("fastAnimation");

		this.add(containerForComponents(new JRadioButton[] {slowAnimation, mediumAnimation, fastAnimation}, "Animation speed: "));
	}

	private void createLabelsOnPictureChooser() {
		labelsOn = new JRadioButton("On");
		labelsOn.setActionCommand("labelsOn");
		labelsOff = new JRadioButton("Off");
		labelsOff.setActionCommand("labelsOff");

		//This should be usable from outside this scope.
		labelsToggleContainer = containerForComponents(new JRadioButton[] {labelsOn, labelsOff}, "Number in corner of tiles: ");

		this.add(labelsToggleContainer);
	}

	private void createPictureChooser() {
		choosePicture = new JButton("Choose new picture");
		choosePicture.setActionCommand("chooseNewPicture");
		choosePicture.addActionListener(this);
		choosePicture.setAlignmentX(CENTER_ALIGNMENT);
		this.add(choosePicture);
	}

	private void createWindowSizeChooser() {
		smallWindow = new JRadioButton("Small");
		smallWindow.setActionCommand("smallWindow");
		mediumWindow = new JRadioButton("Medium");
		mediumWindow.setActionCommand("mediumWindow");
		largeWindow = new JRadioButton("Large");
		largeWindow.setActionCommand("largeWindow");

		this.add(containerForComponents(new JRadioButton[] {smallWindow, mediumWindow, largeWindow}, "Window size: "));
	}

	private void addButton(JButton button, String label) {
		button = new JButton(label) {

			private static final long serialVersionUID = 1L;

			{
				setSize(256, 48);
				setMaximumSize(getSize());
			}
		};
		button.addActionListener(this);
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(256, 48));
		button.setActionCommand(button.getName());

		this.add(Box.createVerticalGlue());
		this.add(button);
	}

	private void createBoardSizeChooser() {
		boardSizeChooser = new JTextField();
		boardSizeChooser.setPreferredSize(new Dimension(40, boardSizeChooser.getPreferredSize().height));
		boardSizeChooser.setMaximumSize(boardSizeChooser.getPreferredSize());
		boardSizeChooser.setHorizontalAlignment(JTextField.CENTER);

		setBoardSizeButton = new JButton("Set size");
		setBoardSizeButton.setActionCommand("setBoardSize");

		this.add(containerForComponents(new JComponent[] {boardSizeChooser, setBoardSizeButton}, "Board size: "));
	}

	private void createFramesPerSecondChooser() {
		fpsSlider = new JSlider(JSlider.HORIZONTAL, 30, 120, 60);
		fpsSlider.setMinorTickSpacing(30);
		fpsSlider.setMajorTickSpacing(30);
		fpsSlider.setSnapToTicks(true);
		fpsSlider.setPaintTicks(true);
		fpsSlider.setPaintLabels(true);
		fpsSlider.setPreferredSize(new Dimension(150, (int)fpsSlider.getPreferredSize().getHeight()));
		fpsSlider.setMaximumSize(fpsSlider.getPreferredSize());

		//Connect to control
		fpsSlider.addChangeListener(this);

		this.add(containerForComponents(new JComponent[] {fpsSlider}, "FPS: "));
	}


}
