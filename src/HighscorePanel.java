import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class HighscorePanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JButton selectSizeButton;
	private JTextField selectSizeTextField;
	private JRadioButton easyDifficulty;
	private JRadioButton mediumDifficulty;
	private JRadioButton hardDifficulty;
	private Box container;
	private JLabel[] numbers;
	private JLabel[] names;
	private JLabel[] scores;
	private Highscore mediumHighscore;
	private Highscore hardHighscore;
	private Highscore easyHighscore;
	private int lookingAtBoardSize;

	public HighscorePanel(Highscore easyHighscore, Highscore mediumHighscore, Highscore hardHighscore) {
		this.easyHighscore = easyHighscore;
		this.mediumHighscore = mediumHighscore;
		this.hardHighscore = hardHighscore;
		
		this.lookingAtBoardSize = Window.getSettings().getTilesPerRowInBoard();
		
		this.setPreferredSize(Window.getSettings().getCurrWindowSize().getDimension());
		container = new Box(BoxLayout.Y_AXIS);
		container.setPreferredSize(new Dimension(WindowSize.SMALL.getWINDOW_WIDTH(), Window.getSettings().getCurrWindowSize().getWINDOW_HEIGHT()));
		
		JLabel title = new JLabel("HIGHSCORE");
		title.setFont(new Font("Monospaced", Font.BOLD, 50));
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		container.add(title);
		
		container.add(sizeChooser());
		
		container.add(difficultyChooser());

		Box highscore = highscorePresenter();
		highscore.setMaximumSize(container.getPreferredSize());
		container.add(highscore);
		
		container.add(closeButton());
		//Add some space below the close button. 
		container.add(Box.createVerticalStrut(WindowSize.LARGE.getGAME_BORDER()));
		
		initLabelText();
		
		this.add(container);
		this.setVisible(true);
	}
	
	private void initLabelText() {
		for (int i = 0; i < numbers.length; i++) {
			numbers[i].setText((i+1)+":");
		}
		
		loadHighscore();
		
		selectSizeTextField.setText(this.lookingAtBoardSize+"");
		if (Window.getSettings().getDifficulty() == Difficulty.EASY.getValue()) {
			easyDifficulty.setSelected(true);
		} else if(Window.getSettings().getDifficulty() == Difficulty.MEDIUM.getValue()) {
			mediumDifficulty.setSelected(true);
		} else {
			hardDifficulty.setSelected(true);
		}
		
	}
	
	//Selects which highscore to 
	private void loadHighscore() {
		if (easyDifficulty.isSelected()) {
			loadHighscore(easyHighscore);
		} else if(mediumDifficulty.isSelected()) {
			loadHighscore(mediumHighscore);
		} else { //Must be hard difficulty
			loadHighscore(hardHighscore);
		}
	}
	
	//Helper method for loadHighscore()
	private void loadHighscore(Highscore highscore) {
		//collect and set highscore from the current highscorelist. 
		for (int i = 0; i < names.length; i++) {
			String[] currHighscore = highscore.getHighscoreAt(lookingAtBoardSize, i);
			
			names[i].setText(currHighscore[0]);
			scores[i].setText(currHighscore[1]);
		}
		
	}
	
	//Method to reset size for this view. 
	public void resetSize() {
		this.setPreferredSize(Window.getSettings().getCurrWindowSize().getDimension());
		container.setPreferredSize(new Dimension(WindowSize.SMALL.getWINDOW_WIDTH(), Window.getSettings().getCurrWindowSize().getWINDOW_HEIGHT()));
	}
	
	private JButton closeButton() {
		JButton button = new JButton("Close") {

			private static final long serialVersionUID = 1L;

			{
				setSize(256, 48);
				setMaximumSize(getSize());
			}
		};
		button.addActionListener(this);
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(256, 48));
		button.setActionCommand("closeButton");

		return button;
	}
	
	private Box highscorePresenter() {
		
		int fieldsToShow = 5;
		
		//Create a box for numbers (number 1-fieldsToShow)
		numbers = new JLabel[fieldsToShow];
		Box numberBox = createBoxWithLabels(numbers, "1:");
		
		//Create a box for the names
		names = new JLabel[fieldsToShow];
		Box nameBox = createBoxWithLabels(names, "Robert");

		//Create a box for the scores. 
		scores = new JLabel[fieldsToShow];
		Box scoreBox = createBoxWithLabels(scores, "10000");
		
		//Make a container for the three boxes and align the boxes side by side. 
		Box container = new Box(BoxLayout.X_AXIS);
		
		//Set the maximum width so that the three boxes lie equally beside each other. 
		//The maximum height is just set to larger than they will ever be. 
		//The numberBox is 1/9 of the width, the names 6/9 and the scores 2/9 
		numberBox.setMaximumSize(new Dimension(WindowSize.SMALL.getWINDOW_WIDTH()/9, WindowSize.LARGE.getWINDOW_HEIGHT()));
		nameBox.setMaximumSize(new Dimension((WindowSize.SMALL.getWINDOW_WIDTH()*6)/9, WindowSize.LARGE.getWINDOW_HEIGHT()));
		scoreBox.setMaximumSize(new Dimension((WindowSize.SMALL.getWINDOW_WIDTH()*2)/9, WindowSize.LARGE.getWINDOW_HEIGHT()));

		//Add all boxes to the container.
		container.add(numberBox);
		container.add(nameBox);
		container.add(scoreBox);	
		
		return container;
	}
	
	private Box createBoxWithLabels(JLabel[] labels, String text) {
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalGlue());
		//Make each label in the labels array and set its properties.
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(text);
			labels[i].setAlignmentX(CENTER_ALIGNMENT);
			labels[i].setFont(new Font("sans serif", Font.PLAIN, 25));
			box.add(labels[i]);
			//Add verticalglue so it will be aligned vertically. 
			box.add(Box.createVerticalGlue());
		}
		return box;
	}
	
	private Box sizeChooser() {
		selectSizeTextField = new JTextField();
		selectSizeTextField.setPreferredSize(new Dimension(40, selectSizeTextField.getPreferredSize().height));
		selectSizeTextField.setMaximumSize(selectSizeTextField.getPreferredSize());
		selectSizeTextField.setHorizontalAlignment(JTextField.CENTER);
		selectSizeButton = new JButton("Select size");
		selectSizeButton.setActionCommand("selectSizeButton");
		
		return containerForComponents(new JComponent[] {selectSizeTextField, selectSizeButton}, "Show highscore for size: ");
	}
	
	private Box difficultyChooser() {
		easyDifficulty = new JRadioButton("easy");
		easyDifficulty.setActionCommand("easyDifficulty");
		mediumDifficulty = new JRadioButton("medium");
		mediumDifficulty.setActionCommand("mediumDifficulty");
		hardDifficulty = new JRadioButton("hard");
		hardDifficulty.setActionCommand("hardDifficulty");
		
		return containerForComponents(new JComponent[] {easyDifficulty, mediumDifficulty, hardDifficulty}, "Select difficulty: ");
	}
	
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
			} 
		}

		return container;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {
			case "closeButton":
				//TODO: Should do something different depending on where it's coming from. 
				Window.swapView("mainMenu");
				break;
			
			case "easyDifficulty":
				easyDifficulty.setSelected(true);
				loadHighscore();
				break;
			case "mediumDifficulty":
				mediumDifficulty.setSelected(true);
				loadHighscore();
				break;
			case "hardDifficulty":
				hardDifficulty.setSelected(true);
				loadHighscore();
				break;
			
			case "selectSizeButton": 
				//Regular expression to remove everything but digits from the string.
				String newBoardSize = selectSizeTextField.getText().replaceAll("[^0-9]+", "");

				//Try to convert to int. If it doesn't succeed set back to original num.
				//Should only happen if the field doesn't contain any integers at all. 
				try {
					int boardSize = Integer.parseInt(newBoardSize);

					//Check if it's a valid boardSize and set it or revert to the one before. 
					if (boardSize >= 3 && boardSize <= 100) {
						this.lookingAtBoardSize = boardSize;
						selectSizeTextField.setText(newBoardSize);
						loadHighscore();
					} else {
						selectSizeTextField.setText("" + this.lookingAtBoardSize);
					}
				} catch (Exception exc){
					selectSizeTextField.setText("" + this.lookingAtBoardSize);
				}
				break;
				
			default: break;
		}
		
	}
}
