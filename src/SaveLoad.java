import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLoad {

	private File file;
	private FileOutputStream f_out;
	private FileInputStream f_in;
	private ObjectOutputStream obj_out;
	private ObjectInputStream obj_in;
	
	public void saveToFile (Object object, String fileName) {
		try {

			file 		= new File(fileName + ".data");
			f_out 		= new FileOutputStream(file);
			obj_out 	= new ObjectOutputStream (f_out);
			
			// if file doesn't exists, then create it
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
	
	public Object loadFromFile(String fileName) {
		try {

			file 		= new File(fileName + ".data");
			f_in 		= new FileInputStream(file);
			obj_in	 	= new ObjectInputStream (f_in);
			
			// continue only if file exists
			if (file.exists()) {
				return obj_in.readObject();
			}
			
			obj_out.close();
			f_out.close();
			
			return null;
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
