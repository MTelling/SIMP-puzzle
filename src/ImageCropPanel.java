import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ImageCropPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField xStart;
	private JTextField yStart;
	private JTextField xEnd;
	private JTextField yEnd;
	
	public ImageCropPanel() {
		JLabel xStartLabel = new JLabel("Start X-Coordinate: ");
		xStart = new JTextField();
		JLabel yStartLabel = new JLabel("Start Y-Coordinate: ");
		yStart = new JTextField();
		
		Box leftSideLabels = new Box(BoxLayout.PAGE_AXIS);
		leftSideLabels.add(xStartLabel);
	}
}
