package com.example.Awx_automation.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import com.example.Awx_automation.Entity.Credential;
import com.example.Awx_automation.Entity.Host;
import com.example.Awx_automation.Entity.Inventory;
import com.example.Awx_automation.Entity.JobTemplateRequest;
import com.example.Awx_automation.Entity.JobTemplateResponse;
import com.example.Awx_automation.Entity.Notification_temp;
import com.example.Awx_automation.Entity.Notification_templates;
import com.example.Awx_automation.Entity.Organization;
import com.example.Awx_automation.Entity.Project;
import com.example.Awx_automation.Entity.ProjectResponse;
import com.example.Awx_automation.Service.AWXService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


@RestController
@RequestMapping("/api/awx")
public class AWXController {
	
	private final AWXService awxService;

    @Autowired
    public AWXController(AWXService awxService) {
        this.awxService = awxService;
    }

    @GetMapping("/hosts")
    public List<Host> getHosts() {
        return awxService.fetchHosts();
    }
    
    @GetMapping("/notifications")
    public List<Notification_templates> getNotification(){
    	return awxService.fetchnotification();
    }

    @GetMapping("/inventories")
    public List<Inventory> getInventories() {
        return awxService.fetchInventories();
    }

    @GetMapping("/organizations")
    public List<Organization> getOrganizations() {
        return awxService.fetchOrganizations();
    }

    @GetMapping("/projects")
    public List<Project> getProjects() {
        return awxService.fetchProjects();
    }

    @GetMapping("/job-templates")
    public List<JobTemplateResponse> getJobTemplates() {
        return awxService.fetchJobTemplates();
    }
    
    @GetMapping("/credentials")
    public List<Credential> getCredentials(){
    	return awxService.fetchCredentials();
    }

    @PostMapping("/create-project")
    public ProjectResponse createProject(@RequestBody Project project) {
        return awxService.createProject(project);
    }
    
    @PostMapping("/create-job-template")
    public JobTemplateResponse createJobTemplate(@RequestBody JobTemplateRequest jobTemplateRequest) {
        return awxService.createJobTemplate(jobTemplateRequest);
    }
    
    @PostMapping("/trigger-job/{jobTemplateId}")
    public JsonNode triggerJob(@PathVariable int jobTemplateId) {
        return awxService.triggerJob(jobTemplateId);
    }
    @GetMapping("/job-result/{jobId}")
    public JsonNode getJobResult(@PathVariable int jobId) {
        return awxService.getJobResult(jobId);
    }
    @GetMapping("/job-output/{jobId}")
    public String getJobOutput(@PathVariable int jobId) {
        return awxService.getJobOutput(jobId);
    }
    
    
    @PostMapping("/notifications/manage")
	    public ResponseEntity<String> ManageNotification(@RequestBody Map<String, Object> payload) {
	        String result = awxService.manageNotificationTemplate(
	        		parseLong(payload.get("job_template_id")),
	        		parseLong(payload.get("notification_template_id")),
	        		(String) payload.get("type"),
	        		(Boolean)payload.get("disassociate")
	        );
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    }
    
    private Long parseLong(Object value) {
	        if (value instanceof Number) {
	            return ((Number) value).longValue();
	        }
	        if (value instanceof String) {
	            return Long.parseLong((String) value);
	        }
	        throw new IllegalArgumentException("Cannot parse value to Long: " + value);
	    }

        @PostMapping("/create-notification-template")
        public ResponseEntity<String> createNotificationTemplate(@RequestBody Map<String, Object> templateDetails) {
            // Validate required fields for email type
            if ("email".equalsIgnoreCase((String) templateDetails.get("notification_type"))) {
                @SuppressWarnings("unchecked")
                Map<String, Object> config = (Map<String, Object>) templateDetails.get("notification_configuration");
                if (config == null || !config.containsKey("host") || !config.containsKey("port") ||
                        !config.containsKey("username") || !config.containsKey("password") ||
                        !config.containsKey("sender_email") || !config.containsKey("recipient_list")) {
                    throw new IllegalArgumentException("Missing required fields for email notification type.");
                }
    
                // Add missing fields with default values if not present
                config.putIfAbsent("use_tls", false); // Default to false
                config.putIfAbsent("use_ssl", false); // Default to false
                config.putIfAbsent("sender", config.get("sender_email")); // Use sender_email as sender
                config.putIfAbsent("recipients", config.get("recipient_list")); // Use recipient_list as recipients
    
                // Ensure port is an integer
                if (!(config.get("port") instanceof Integer)) {
                    throw new IllegalArgumentException("Configuration field 'port' must be an integer.");
                }
            }
    
            // Validate organization field
            if (!templateDetails.containsKey("organization") || templateDetails.get("organization") == null) {
                throw new IllegalArgumentException("Missing required field: organization.");
            }
    
            // Call the service
            return awxService.createNotificationTemplate(templateDetails);
        }

        @PostMapping("/test-notification/{notificationTemplateId}")
    public ResponseEntity<String> testNotificationTemplate(@PathVariable Long notificationTemplateId) {
        return awxService.testNotificationTemplate(notificationTemplateId);
    }
    
}