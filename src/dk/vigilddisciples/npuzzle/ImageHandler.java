package dk.vigilddisciples.npuzzle;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ImageHandler {	
    
    public static BufferedImage[] getTilePics(int tilesPerRow, int tileSize, String fileName) throws IOException {

		BufferedImage mainPic = ImageIO.read(new File(fileName));
		BufferedImage[] tilePics = new BufferedImage[(int) Math.pow(tilesPerRow, 2)];
    	try { //Try to slice the image into pieces to fit to the individual tiles.
			for(int y = 0; y < tilesPerRow; y++) {
	    		for(int x = 0; x < tilesPerRow; x++) {
	    			
	    			tilePics[y*tilesPerRow + x] = mainPic.getSubimage(
	    					x * tileSize, 	// x coordinates
	    					y * tileSize, 	// y coordinates
	    					tileSize, 		// width
	    					tileSize);		// height
	    		}
	    	}
    	} catch (RasterFormatException e) {
            //Throw error (We'll handle it in the gamepanel)
    		throw new RasterFormatException("Error choosing image");
    	}
    	
    	return tilePics;
    }
    
    public static String cropAndSave (BufferedImage buffPic, int x, int y) {
    	Random rand = new Random();
    	int size = NPuzzle.getSettings().getCurrWindowSize().getBOARD_SIZE();
    	try {
    		File file = new File("resources/pics/" + Integer.toString(rand.nextInt(999999999)) + ".jpg");
    		if(!file.exists()) {
    			ImageIO.write(buffPic.getSubimage(x, y, size, size), "jpg", file);
    			return file.getName();
    		} else {
    			cropAndSave(buffPic, x, y);
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static String loadPicture () {
    	//Create a filedialog for the user to choose a picture.
    	FileDialog fileDialog = new FileDialog(new Frame(), "Choose a picture", FileDialog.LOAD);
    	fileDialog.setDirectory("resources/pics");
    	fileDialog.setFile("*.jpg;*.jpeg;*.png");
    	fileDialog.setFilenameFilter((File dir, String name) -> name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
    	fileDialog.setVisible(true);
    	
    	// Get the chosen file
    	File theImage = new File(fileDialog.getDirectory() + fileDialog.getFile());
    	
	    if(theImage.exists()) {
			return theImage.getAbsolutePath();
		} else {
	 		JOptionPane.showMessageDialog(null, "Error: Can't load image ", "Image Load Failed", JOptionPane.ERROR_MESSAGE);
		} 
		
		return null;
    }	
}