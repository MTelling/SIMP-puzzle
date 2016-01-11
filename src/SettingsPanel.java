import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;


public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ButtonGroup gameTypeToggle;
	private ButtonGroup difficultyToggle;
	private ButtonGroup animationToggle;
	private ButtonGroup controlsToggle;
	private ButtonGroup animationSpeedToggle;
	private ButtonGroup labelsToggle;
	private ButtonGroup windowSizeToggle;
	
	private JRadioButton gameTypeNumbers;
	private JRadioButton gameTypePicture;
	
	
	private JButton choosePicture;
	
	private Box labelsToggleContainer; //So you can hide the "show labels in corner of tile" if game type is set to numbers.
	
	private JButton setBoardSizeButton;
	private JTextField boardSizeChooser;
	
	private JSlider fpsSlider;

	private JButton exitButton;
	
	private Settings settings;

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

	
	public SettingsPanel(Settings settings) {
		this.settings = settings;
		
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
	}
	
	/// Methods to set values to what they are in settings ///
	
	private void loadDifficultySetting() {
		if (settings.getDifficulty() < 5) {
			easyGame.setSelected(true);
		} else if (settings.getDifficulty() < 20) {
			mediumGame.setSelected(true);
		} else {
			hardGame.setSelected(true);
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
		boardSizeChooser.setText(""+settings.getTilesPerRowInBoard());
	}
	
	private void loadLabelsSetting() {
		if (settings.isLabelsOn()) {
			labelsOn.setSelected(true);
		} else {
			labelsOff.setSelected(true);
		}
	}
	
	private void loadAnimationSpeed() {
		if (settings.getAnimationSpeed() <= 2) {
			slowAnimation.setSelected(true);
		} else if (settings.getAnimationSpeed() < 6) {
			mediumAnimation.setSelected(true);
		} else {
			fastAnimation.setSelected(true);
		}
	}
	
	
	/// Methods for creating the UI ///
	private void createGameTypeChooser() {
		
		JLabel gameTypeLabel = new JLabel("Game Type: ");
		gameTypeNumbers = new JRadioButton("Numbers");
		gameTypeNumbers.setSelected(true);
		gameTypePicture = new JRadioButton("Picture");
		
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
		easyGame.setSelected(true);
		mediumGame = new JRadioButton("Medium");
		hardGame = new JRadioButton("EXTREME");
		
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
		animationOn.setSelected(true);
		animationOff = new JRadioButton("Off");
		
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
		boardSizeChooser.setText("4");
		boardSizeChooser.setPreferredSize(new Dimension(40, boardSizeChooser.getPreferredSize().height));
		boardSizeChooser.setMaximumSize(boardSizeChooser.getPreferredSize());
		boardSizeChooser.setHorizontalAlignment(JTextField.CENTER);
		
		setBoardSizeButton = new JButton("Set size");
		
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
	    
	    Box fpsContainer = new Box(BoxLayout.LINE_AXIS);
		fpsContainer.add(fpsLabel);
		fpsContainer.add(fpsSlider);

		
		this.add(fpsContainer);
	}
	
	private void createControlsChooser() {
		JLabel controlsLabel = new JLabel("Controls: ");
		normalControls = new JRadioButton("Normal");
		normalControls.setSelected(true);
		invertedControls = new JRadioButton("Inverted");
		
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
		slowAnimation.setSelected(true);
		mediumAnimation = new JRadioButton("Medium");
		fastAnimation = new JRadioButton("Fast");
		
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
		labelsOn.setSelected(true);
		labelsOff = new JRadioButton("Off");
		
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
		choosePicture.setAlignmentX(CENTER_ALIGNMENT);
		this.add(choosePicture);
	}
	
	private void createWindowSizeChooser() {
		JLabel windowSizeLabel = new JLabel("Window size: ");
		JRadioButton small = new JRadioButton("Small");
		small.setSelected(true);
		JRadioButton medium = new JRadioButton("Medium");
		JRadioButton large = new JRadioButton("Large");
		
		windowSizeToggle = new ButtonGroup();
		windowSizeToggle.add(small);
		windowSizeToggle.add(medium);
		windowSizeToggle.add(large);
		
		
		Box windowSizeContainer = new Box(BoxLayout.LINE_AXIS);
		windowSizeContainer.add(windowSizeLabel);
		windowSizeContainer.add(small);
		windowSizeContainer.add(medium);
		windowSizeContainer.add(large);
		
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
		//TODO: CONNECT button.addActionListener(this);
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(256, 48));
		
		this.add(Box.createVerticalGlue());
		this.add(button);
	}
}
