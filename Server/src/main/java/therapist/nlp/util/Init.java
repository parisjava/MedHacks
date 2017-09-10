package therapist.nlp.util;

import java.util.Properties;

import java.io.IOException;

public class Init {

    public static Properties pipeline;
    public static Properties database;
    
    static {
	initialize();
    }

    private static void initialize() {
	pipeline = new Properties();
	database = new Properties();
	try {
	    pipeline.load(
	        Init.class.getResourceAsStream("/pipeline.prop"));
	    database.load(
		Init.class.getResourceAsStream("/database.prop"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	try {
	    Class.forName(database.getProperty("driver"));
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	
    }
}
