package br.svcdev.weatherapp;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {

	public String readJsonFile(String jsonFileName) {
		String jsonString = "";
		File file = new File(String.format("%s/%s",
				Environment.getExternalStorageDirectory().toString(), jsonFileName));
		try (FileInputStream fis = new FileInputStream(file);
			 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis))) {

			bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}
