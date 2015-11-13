package droid.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class FileIO {
	public static void writeToFile(Context context, String fileName, String data) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(
					context.getFilesDir(), fileName));

			fos.write(data.getBytes());

			fos.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFromFile(Context context, String fileName) {
		String contents = "";
		byte[] buffer = new byte[1024];
		try {
			FileInputStream fis = new FileInputStream(new File(
					context.getFilesDir(), fileName));

			int newByte = 0;
			while((newByte = fis.read()) != -1) {
				contents += (char) newByte;
			}
			fis.read(buffer);

			fis.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return contents;
	}
}
