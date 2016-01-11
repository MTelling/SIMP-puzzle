import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class ImageHandler {	
    
    public static BufferedImage[] getTilePics(int boardSize, int tileSize, String fileName, String fileExt) throws IOException {

		BufferedImage mainPic = ImageIO.read(new File(fileName + "." + fileExt));
		BufferedImage[] tilePics = new BufferedImage[(int) Math.pow(boardSize, 2)];
    	for(int y = 0; y < boardSize; y++) {
    		for(int x = 0; x < boardSize; x++) {
    			
    			tilePics[y*boardSize + x] = mainPic.getSubimage(
    					x * tileSize, 	// x coordinates
    					y * tileSize, 	// y coordinates
    					tileSize, 		// width
    					tileSize);		// height
    		}
    	}
    	
    	return tilePics;

    }
    
    public static void cropAndSave (BufferedImage buffPic, int x, int y, int size) {
    	    	
    	Random rand = new Random();
    	
    	try {
    		File file = new File("resources/pics/" + Integer.toString(rand.nextInt(999999999)));
    		if(!file.exists()) {
    			ImageIO.write(buffPic.getSubimage(x, y, size, size), "jpg", file);
    		}else{
    			cropAndSave(buffPic, x, y, size);
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static BufferedImage loadPicture () {
    	      
    	//Create a filedialog for the user to choose a picture.
    	FileDialog fileDialog = new FileDialog(new Frame(), "Choose a picture", FileDialog.LOAD);
    	fileDialog.setDirectory(null);
    	fileDialog.setFile("*.jpg;*.jpeg;*.gif;*.png");
    	fileDialog.setVisible(true);
    	
		String infoMessage = "Something terrible happened!\n"
				+" I must urge you to try again.\n"
				+" Please though, do something different next time.\n";
    	
		try {
    	// Get the chosen file
    	File theImage = new File(fileDialog.getFile());
    	
    	if(theImage.exists()) {
				return ImageIO.read(theImage);


    	} else {
    		JOptionPane.showMessageDialog(null, infoMessage, "Sorry bro, can't do it", JOptionPane.INFORMATION_MESSAGE);
    		
    		return loadPicture();
    	}
		} catch (Exception e) {
	 		JOptionPane.showMessageDialog(null, infoMessage, "Sorry bro, can't do it", JOptionPane.INFORMATION_MESSAGE);
		} 
		
		return null;
    }
    
    
    
    
    
    
	
}
