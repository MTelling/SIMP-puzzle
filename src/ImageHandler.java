import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageHandler {	
    
    public static BufferedImage[] getTilePics(int boardSize, int tileSize, String fileName, String fileExt) throws IOException {

		BufferedImage mainPic = ImageIO.read(new File(fileName + "." + fileExt));
		BufferedImage[] tilePics = new BufferedImage[(int) Math.pow(boardSize, 2)];
		int counter = 0;
    	for(int y = 0; y < boardSize; y++) {
    		for(int x = 0; x < boardSize; x++) {
    			
    			tilePics[counter] = mainPic.getSubimage(
    					x * tileSize, 	// x coordinates
    					y * tileSize, 	// y coordinates
    					tileSize, 		// width
    					tileSize);		// height
    			counter++;
    		}
    	}
    	
    	return tilePics;

    }
    
    public static void loadCropAndSave() {
    	
    }
    
    
	
}
