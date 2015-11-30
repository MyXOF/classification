package xof.classification.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassificationConfig {
	private final static Logger logger = LoggerFactory.getLogger(ClassificationConfig.class);
	private final static String CONF_FILE_PATH = "/config.properties";

	public String KMeans_Set;
	public String KMeans_ResultPath;
	public String DBScan_ResultPath;
	public String DATA_PATH;
	
	private final static ClassificationConfig CONFIG = new ClassificationConfig();

	public static ClassificationConfig getInstance(){
		return CONFIG;
	}
	
	private ClassificationConfig(){
		readConfig();
	}
	
	private void readConfig(){
		Properties prop = new Properties(); 
		try (InputStream in = ClassificationConfig.class.getResourceAsStream(CONF_FILE_PATH)){
			prop.load(in);
			KMeans_Set = prop.getProperty("kmeans_set");
			KMeans_ResultPath = prop.getProperty("kmeans_resultpath");
			DBScan_ResultPath = prop.getProperty("dbscan_resultpath");
			DATA_PATH = prop.getProperty("data_path");
			in.close();
		} catch (IOException e) {
			logger.error("ClassificationConfig: fail to load configuration from {}",CONF_FILE_PATH,e);
		} 
	}
	
	public static void main(String[] args) {
		ClassificationConfig config = ClassificationConfig.getInstance();
		System.out.println(config.KMeans_Set);
		System.out.println(config.KMeans_ResultPath);
		System.out.println(config.DBScan_ResultPath);
		System.out.println(config.DATA_PATH);
	}

}
