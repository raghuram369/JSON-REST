package com.verizon.dbc.iengine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FolderMonitor{	

	public static void main(String[] args) {		
		SpringApplication.run(FolderMonitor.class, args);
	}
	
	
}