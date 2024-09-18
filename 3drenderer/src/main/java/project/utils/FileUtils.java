package project.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import project.Application;
import project.shared.logger.Logger;

public final class FileUtils {

	public static String readTextFile(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			StringBuilder sourceBuilder = new StringBuilder();
			String line;
			
			while( (line = reader.readLine()) != null ) {
				sourceBuilder.append(line);
				sourceBuilder.append('\n');
			}
			
			reader.close();
			
			return sourceBuilder.toString();
			
		} catch( Exception e ) {
			Logger.get().error(
				"[FileUtils.readTextFile(String)]", 
				"ERROR: Unable to read file: ",
				path
			);
		}
		
		return "";
	}
	
	public static String getResourcePath(String relativePath) {
		URL url = Application.class.getResource("/" + relativePath);
		
		if( url == null ) {
			Logger.get().error(
				"[FileUtils.getResourcePath(String)]", 
				"ERROR: Unable to resolve relative resource path: ",
				relativePath
			);
			return null;
		}
		
		File file = new File(url.getPath());
		return file.getAbsolutePath();
	}
}
