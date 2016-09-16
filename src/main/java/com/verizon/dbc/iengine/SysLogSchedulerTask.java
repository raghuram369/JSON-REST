package com.verizon.dbc.iengine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.verizon.dbc.iengine.config.CommonProperties;

@Component
public class SysLogSchedulerTask {

	@Autowired
	private CommonProperties commonProperty;

	@Autowired
	private FileParser fileParser;

	@Scheduled(fixedDelay = 10000)
	public void monitorFolder() {
		System.out.println("Scheduler Started");
		String directory = commonProperty.getMonitorFolder();
		String archiveFolder = commonProperty.getArchiveFolder();
		File destination = null;
		if(!archiveFolder.endsWith("/"))
			archiveFolder+="/";
		Map<String, Long> initialSize = new HashMap<String, Long>(), finalSize = new HashMap<String, Long>();
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				initialSize.put(listOfFiles[i].getName(), listOfFiles[i].length());				
			} 
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if(initialSize.get(listOfFiles[i].getName()) != null && ((Long)initialSize.get(listOfFiles[i].getName())).longValue() == listOfFiles[i].length()){
					System.out.println("Processing File:" + listOfFiles[i].getName());
					try {
						fileParser.parseFile(listOfFiles[i]);
						destination = new File(archiveFolder+listOfFiles[i].getName());
						Files.move(listOfFiles[i].toPath(), destination.toPath(),StandardCopyOption.REPLACE_EXISTING );
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} 
			
		}
		System.out.println("Scheduler Completed");
	}

}
