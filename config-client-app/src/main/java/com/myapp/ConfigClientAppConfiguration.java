package com.myapp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="default")
public class ConfigClientAppConfiguration {
	
	private String property;

	@Override
	public String toString() {
		return "ConfigClientAppConfiguration [property=" + property + "]";
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

 
}
