package dk.vigilddisciples.npuzzle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLoad {
	
	public static final String FILE_EXT = "BearnaiseSovs";

	private static File file;
	private static FileOutputStream f_out;
	private static FileInputStream f_in;
	private static ObjectOutputStream obj_out;
	private static ObjectInputStream obj_in;
	
	public static void saveToFile (Object object, String fileName) {
		try {
			file = new File(fileName + "." + FILE_EXT);
			f_out = new FileOutputStream(file);
			obj_out = new ObjectOutputStream (f_out);
			
			// if file doesn't exists; create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			obj_out.writeObject (object);
			
			obj_out.close();
			f_out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object loadFromFile(String fileName) throws Exception{
		
		file = new File(fileName + "." + FILE_EXT);
		f_in = new FileInputStream(file);
		obj_in = new ObjectInputStream (f_in);
		
		// continue only if file exists
		if (file.exists()) {
			return obj_in.readObject();
		}
		
		obj_out.close();
		f_out.close();
		
		return null;
	}
}