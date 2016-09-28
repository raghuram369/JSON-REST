package com.verizon.dbc.iengine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableScheduling
public class FolderMonitor  implements CommandLineRunner{	
	
	@Autowired
	private FileParser fileParser;
		
	public static void main(String[] args) {		
		SpringApplication.run(FolderMonitor.class, args);	
		
	}
	
	@Override
	public void run(String... args) {
		this.fileParser.fileparser();		
	}
	
	
}
