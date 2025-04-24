package com.example.Awx_automation.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection= "inventories")
public class Inventory {
	
	@Id
	private int id;
	private String name;
}
