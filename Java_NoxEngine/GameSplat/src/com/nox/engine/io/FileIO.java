package com.nox.engine.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {
	public static void writeToFile(String fileName, String data) {
		try {
			FileWriter fw = new FileWriter(fileName);

			fw.write(data);

			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFromFile(String fileName) {
		String contents = "";
		try {
			FileReader fr = new FileReader(fileName);

			int newByte = 0;
			while((newByte = fr.read()) != -1) {
				contents += (char) newByte;
			}

			fr.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return contents;
	}
}
