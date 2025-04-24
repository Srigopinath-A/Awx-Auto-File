package com.example.Awx_automation.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Document(collection = "Notification_templates")
public class Notification_templates {
	@Id
	private int id;
	private String name;
    private String description;
    private Integer organization;
    private String notificationType;

    @JsonProperty("notification_configuration")
    private Object notificationConfiguration;

    private Object messages;
}

