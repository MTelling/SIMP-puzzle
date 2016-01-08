import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel gameTypeLabel;
	private ButtonGroup gameType;
	
	public SettingsPanel() {
		gameTypeLabel = new JLabel("Choose Game Type:");
		JRadioButton gameTypeNumbers = new JRadioButton("Numbers");
		gameTypeNumbers.setSelected(true);
		JRadioButton gameTypePicture = new JRadioButton("Picture");
		
		gameType = new ButtonGroup();
		gameType.add(gameTypeNumbers);
		gameType.add(gameTypePicture);
		
		this.add(gameTypeLabel);
		this.add(gameTypeNumbers);
		this.add(gameTypePicture);
	}
	
}
