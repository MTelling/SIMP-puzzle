import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SettingsPanel extends JPanel implements ActionListener, ChangeListener{

	private static final long serialVersionUID = 1L;
	
	private ButtonGroup gameTypeToggle;
	private ButtonGroup difficultyToggle;
	private ButtonGroup animationToggle;
	private ButtonGroup controlsToggle;
	private ButtonGroup animationSpeedToggle;
	private ButtonGroup labelsToggle;
	private ButtonGroup windowSizeToggle;
	
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

	private Window window;
	private Settings settings;

	
	public SettingsPanel(Settings settings, Window window) {
		this.settings = settings;
		this.window = window;
		
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
		// TODO Auto-generated method stub
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
			
			//Set animation speed
			case "slowAnimation":
				settings.setAnimationSpeed(AnimationSpeed.SLOW.getValue());
				break;
			case "mediumAnimation":
				settings.setAnimationSpeed(AnimationSpeed.MEDIUM.getValue());
				break;
			case "fastAnimation":
				settings.setAnimationSpeed(AnimationSpeed.FAST.getValue());
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
				Window.swapView("imageCrop");
				break;
				
			//Close settings window
			case "Close settings":
				
				//TODO: Should do something different depending on where it's coming from. 
				Window.swapView("mainMenu");
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
				
			default: break;
		}
		
	}
	
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
			
			//TODO: Maybe this should be in the actionlistener? 
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
		if (settings.getAnimationSpeed() == AnimationSpeed.SLOW.getValue()) {
			slowAnimation.setSelected(true);
		} else if (settings.getAnimationSpeed() == AnimationSpeed.MEDIUM.getValue()) {
			mediumAnimation.setSelected(true);
		} else {
			fastAnimation.setSelected(true);
		}
	}
	
		
	/// Methods for creating UI ///

	private void createGameTypeChooser() {
		
		JLabel gameTypeLabel = new JLabel("Game Type: ");
		gameTypeNumbers = new JRadioButton("Numbers");
		gameTypeNumbers.setActionCommand("gameTypeNumbers");
		gameTypePicture = new JRadioButton("Picture");
		gameTypePicture.setActionCommand("gameTypePicture");
		
		//Connect to controls
		gameTypeNumbers.addActionListener(this);
		gameTypePicture.addActionListener(this);
		
		gameTypeToggle = new ButtonGroup();
		gameTypeToggle.add(gameTypeNumbers);
		gameTypeToggle.add(gameTypePicture);
		
		
		Box gameTypeContainer = new Box(BoxLayout.LINE_AXIS);
		gameTypeContainer.add(gameTypeLabel);
		gameTypeContainer.add(gameTypeNumbers);
		gameTypeContainer.add(gameTypePicture);
		
		this.add(gameTypeContainer);
		
		
		
	}
	
	private void createDifficultyChooser() {
		JLabel difficultyLabel = new JLabel("Difficulty: ");
		easyGame = new JRadioButton("Easy");
		easyGame.setActionCommand("easyGame");
		mediumGame = new JRadioButton("Medium");
		mediumGame.setActionCommand("mediumGame");
		hardGame = new JRadioButton("EXTREME");
		hardGame.setActionCommand("hardGame");
		
		//Connect to controls
		easyGame.addActionListener(this);
		mediumGame.addActionListener(this);
		hardGame.addActionListener(this);

		
		difficultyToggle = new ButtonGroup();
		difficultyToggle.add(easyGame);
		difficultyToggle.add(mediumGame);
		difficultyToggle.add(hardGame);
		
		
		Box difficultyContainer = new Box(BoxLayout.LINE_AXIS);
		difficultyContainer.add(difficultyLabel);
		difficultyContainer.add(easyGame);
		difficultyContainer.add(mediumGame);
		difficultyContainer.add(hardGame);
		
		this.add(difficultyContainer);
		
	}
	
	private void createAnimationToggle() {
		JLabel animationLabel = new JLabel("Animation on/off: ");
		animationOn = new JRadioButton("On");
		animationOn.setActionCommand("animationOn");
		animationOff = new JRadioButton("Off");
		animationOff.setActionCommand("animationOff");
		
		//Connect to control
		animationOn.addActionListener(this);
		animationOff.addActionListener(this);
		
		animationToggle = new ButtonGroup();
		animationToggle.add(animationOn);
		animationToggle.add(animationOff);
		
		Box animationToggleContainer = new Box(BoxLayout.LINE_AXIS);
		animationToggleContainer.add(animationLabel);
		animationToggleContainer.add(animationOn);
		animationToggleContainer.add(animationOff);
		
		this.add(animationToggleContainer);
	}
	
	private void createBoardSizeChooser() {
		JLabel boardSizeLabel = new JLabel("Board size: ");
		boardSizeChooser = new JTextField();
		boardSizeChooser.setPreferredSize(new Dimension(40, boardSizeChooser.getPreferredSize().height));
		boardSizeChooser.setMaximumSize(boardSizeChooser.getPreferredSize());
		boardSizeChooser.setHorizontalAlignment(JTextField.CENTER);
		
		setBoardSizeButton = new JButton("Set size");
		setBoardSizeButton.setActionCommand("setBoardSize");
		setBoardSizeButton.addActionListener(this);
		
		Box boardSizeChooserContainer = new Box(BoxLayout.LINE_AXIS);
		boardSizeChooserContainer.add(boardSizeLabel);
		boardSizeChooserContainer.add(boardSizeChooser);
		boardSizeChooserContainer.add(setBoardSizeButton);

		
		this.add(boardSizeChooserContainer);
	}
	
	private void createFramesPerSecondChooser() {
		JLabel fpsLabel = new JLabel("FPS: ");
		fpsSlider = new JSlider(JSlider.HORIZONTAL, 30, 120, 60);
		fpsSlider.setPreferredSize(new Dimension(150, 37));
		fpsSlider.setMaximumSize(fpsSlider.getPreferredSize());
		fpsSlider.setMinorTickSpacing(30);
		fpsSlider.setMajorTickSpacing(30);
		fpsSlider.setSnapToTicks(true);
	    fpsSlider.setPaintTicks(true);
	    fpsSlider.setPaintLabels(true);
	    
	    //Connect to control
	    fpsSlider.addChangeListener(this);
	    
	    Box fpsContainer = new Box(BoxLayout.LINE_AXIS);
		fpsContainer.add(fpsLabel);
		fpsContainer.add(fpsSlider);

		
		this.add(fpsContainer);
	}
	
	private void createControlsChooser() {
		JLabel controlsLabel = new JLabel("Controls: ");
		normalControls = new JRadioButton("Normal");
		normalControls.setActionCommand("normalControls");
		invertedControls = new JRadioButton("Inverted");
		invertedControls.setActionCommand("invertedControls");
		
		//Connect to control
		normalControls.addActionListener(this);
		invertedControls.addActionListener(this);
		
		controlsToggle = new ButtonGroup();
		controlsToggle.add(normalControls);
		controlsToggle.add(invertedControls);
		
		Box controlsToggleContainer = new Box(BoxLayout.LINE_AXIS);
		controlsToggleContainer.add(controlsLabel);
		controlsToggleContainer.add(normalControls);
		controlsToggleContainer.add(invertedControls);
		
		this.add(controlsToggleContainer);
	}
	
	private void createAnimationSpeedChooser() {
		JLabel animationSpeedLabel = new JLabel("Animation speed: ");
		slowAnimation = new JRadioButton("Slow");
		slowAnimation.setActionCommand("slowAnimation");
		mediumAnimation = new JRadioButton("Medium");
		mediumAnimation.setActionCommand("mediumAnimation");
		fastAnimation = new JRadioButton("Fast");
		fastAnimation.setActionCommand("fastAnimation");
		
		//Connect to controls
		slowAnimation.addActionListener(this);
		mediumAnimation.addActionListener(this);
		fastAnimation.addActionListener(this);
		
		animationSpeedToggle = new ButtonGroup();
		animationSpeedToggle.add(slowAnimation);
		animationSpeedToggle.add(mediumAnimation);
		animationSpeedToggle.add(fastAnimation);
		
		Box controlsToggleContainer = new Box(BoxLayout.LINE_AXIS);
		controlsToggleContainer.add(animationSpeedLabel);
		controlsToggleContainer.add(slowAnimation);
		controlsToggleContainer.add(mediumAnimation);
		controlsToggleContainer.add(fastAnimation);
		
		this.add(controlsToggleContainer);
	}
	
	private void createLabelsOnPictureChooser() {
		
		JLabel labelsOnLabel = new JLabel("Numbers in corner of tiles: ");
		labelsOn = new JRadioButton("On");
		labelsOn.setActionCommand("labelsOn");
		labelsOff = new JRadioButton("Off");
		labelsOff.setActionCommand("labelsOff");
		
		//Connect to controls
		labelsOn.addActionListener(this);
		labelsOff.addActionListener(this);
		
		labelsToggle = new ButtonGroup();
		labelsToggle.add(labelsOn);
		labelsToggle.add(labelsOff);
		
		labelsToggleContainer = new Box(BoxLayout.LINE_AXIS);
		labelsToggleContainer.add(labelsOnLabel);
		labelsToggleContainer.add(labelsOn);
		labelsToggleContainer.add(labelsOff);
		
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
		JLabel windowSizeLabel = new JLabel("Window size: ");
		smallWindow = new JRadioButton("Small");
		smallWindow.setActionCommand("smallWindow");
		mediumWindow = new JRadioButton("Medium");
		mediumWindow.setActionCommand("mediumWindow");
		largeWindow = new JRadioButton("Large");
		largeWindow.setActionCommand("largeWindow");
		
		//Connect to controls
		smallWindow.addActionListener(this);
		mediumWindow.addActionListener(this);
		largeWindow.addActionListener(this);
		
		windowSizeToggle = new ButtonGroup();
		windowSizeToggle.add(smallWindow);
		windowSizeToggle.add(mediumWindow);
		windowSizeToggle.add(largeWindow);
		
		
		Box windowSizeContainer = new Box(BoxLayout.LINE_AXIS);
		windowSizeContainer.add(windowSizeLabel);
		windowSizeContainer.add(smallWindow);
		windowSizeContainer.add(mediumWindow);
		windowSizeContainer.add(largeWindow);
		
		this.add(windowSizeContainer);
		
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






}
