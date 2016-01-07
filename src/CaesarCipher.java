
public class CaesarCipher {
	
	public String encrypt(String str, int key) {
		return caesar(str, key, true);
	}
	
	public String decrypt(String str, int key) {
		return caesar(str, key, false);
	}
	
	//Helper method for encrypt and decrypt to avoid redundancy
	private String caesar(String str, int key, boolean encrypt) {
		String newString = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (encrypt) {
				newString += Character.toString((char)(c + key));
			} else {
				newString += Character.toString((char)(c - key));
			}
		}
		return newString;
	}
}
