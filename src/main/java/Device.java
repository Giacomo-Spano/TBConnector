//package com.baeldung.jackson.yaml;

import java.math.BigDecimal;

public class Device {
    
    private String name;
    private String type;
    private String id;
    private String token;
    private String powertopic;
    
    
    public Device() {
        
    }

    public Device(String name, String type, String token) {
        super();
        this.name = name;
        this.type = type;
        this.token = token;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPowertopic() {
		return powertopic;
	}

	public void setPowertopic(String powertopic) {
		this.powertopic = powertopic;
	}

	@Override
    public String toString() {
        return "Devicee [name=" + name + ", type=" + type + ", token=" + token + "]";
    }
}