package com.verizon.dbc.iengine.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="common")
public class CommonProperties {
	
	private String commitBatchSize;
	private String monitorFolder;
	private String eventType;
	private String archiveFolder;
	private String sevOneUrl;
	
	
	public String getSevOneUrl() {
		return sevOneUrl;
	}
	public void setSevOneUrl(String sevOneUrl) {
		this.sevOneUrl = sevOneUrl;
	}
	public String getArchiveFolder() {
		return archiveFolder;
	}
	public void setArchiveFolder(String archiveFolder) {
		this.archiveFolder = archiveFolder;
	}
	public String getCommitBatchSize() {
		return commitBatchSize;
	}
	public void setCommitBatchSize(String commitBatchSize) {
		this.commitBatchSize = commitBatchSize;
	}
	public String getMonitorFolder() {
		return monitorFolder;
	}
	public void setMonitorFolder(String monitorFolder) {
		this.monitorFolder = monitorFolder;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

}
