import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageHandler {	
    
    public static BufferedImage[] getTilePics(int boardSize, int tileSize, String fileName, String fileExt) throws IOException {

		BufferedImage mainPic = ImageIO.read(new File(fileName + "." + fileExt));
		BufferedImage[] tilePics = new BufferedImage[(int) Math.pow(boardSize, 2)];
		
    	for(int i = 0; i < boardSize; i++) {
    		for(int j = 0; j < boardSize; j++) {
    			
    			tilePics[(i * boardSize) + j] = mainPic.getSubimage(
    					i * tileSize, 	// x coordinates
    					j * tileSize, 	// y coordinates
    					tileSize, 		// width
    					tileSize);		// height
    			
    		}
    	}
    	
    	return tilePics;

    }
    
    public static void loadCropAndSave() {
    	
    }
    
    
	
}
