package com.example.Awx_automation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.Awx_automation.Entity.Credential;
import com.example.Awx_automation.Entity.CredentialResponse;
import com.example.Awx_automation.Entity.Host;
import com.example.Awx_automation.Entity.HostResponse;
import com.example.Awx_automation.Entity.Inventory;
import com.example.Awx_automation.Entity.InventoryResponse;
import com.example.Awx_automation.Entity.JobTemplateRequest;
import com.example.Awx_automation.Entity.JobTemplateResponse;
import com.example.Awx_automation.Entity.JobTemplateTrigger;
import com.example.Awx_automation.Entity.JobTriggerRequest;
import com.example.Awx_automation.Entity.JobtemplateResponseWrapper;
import com.example.Awx_automation.Entity.Notification_response;
import com.example.Awx_automation.Entity.Notification_temp;
import com.example.Awx_automation.Entity.Notification_templates;
import com.example.Awx_automation.Entity.Organization;
import com.example.Awx_automation.Entity.OrganizationResponse;
import com.example.Awx_automation.Entity.Project;
import com.example.Awx_automation.Entity.ProjectResponse;
import com.example.Awx_automation.Entity.ProjectResponseWrapper;
import com.example.Awx_automation.Repo.CredentialRespository;
import com.example.Awx_automation.Repo.HostRepository;
import com.example.Awx_automation.Repo.InventoryRepository;
import com.example.Awx_automation.Repo.JobTemplateRepository;
import com.example.Awx_automation.Repo.NotificationRep;
import com.example.Awx_automation.Repo.OrganizationRepository;
import com.example.Awx_automation.Repo.ProjectRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class AWXService {
	private static final Logger logger = Logger.getLogger(AWXService.class.getName());

	private final RestTemplate restTemplate;
	private final String awxApiUrl;
	private final String token;

	@Autowired
	private CredentialRespository credentialrepo;

	@Autowired
	private HostRepository hostRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private JobTemplateRepository jobTemplateRepository;

	@Autowired
	private NotificationRep notificationtemplate;
	
	@Autowired
 	private JavaMailSender mailsender;

	public AWXService(RestTemplate restTemplate, @Value("${awx.api.url}") String awxApiUrl,
			@Value("${awx.api.token}") String token) {
		this.restTemplate = restTemplate;
		this.awxApiUrl = awxApiUrl;
		this.token = token;
	}

	private HttpHeaders createAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		return headers;
	}

	public List<Host> fetchHosts() {
		HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders());
		ResponseEntity<HostResponse> response = restTemplate.exchange(awxApiUrl + "/hosts/", HttpMethod.GET, request,
				HostResponse.class);
		List<Host> hosts = response.getBody().getResults();
		hostRepository.saveAll(hosts);
		return hosts;
	}

	public List<Notification_templates> fetchnotification() {
		HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders());
		ResponseEntity<Notification_response> response = restTemplate.exchange(awxApiUrl + "/notification_templates/",
				HttpMethod.GET, request, Notification_response.class);

		List<Notification_templates> Notificationt = response.getBody().getResults();
		notificationtemplate.saveAll(Notificationt);
		return Notificationt;
	}

	public List<Inventory> fetchInventories() {
		HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders());
		ResponseEntity<InventoryResponse> response = restTemplate.exchange(awxApiUrl + "/inventories/", HttpMethod.GET,
				request, InventoryResponse.class);
		List<Inventory> inventories = response.getBody().getResults();
		inventoryRepository.saveAll(inventories);
		return inventories;
	}

	public List<Organization> fetchOrganizations() {
		HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders());
		ResponseEntity<OrganizationResponse> response = restTemplate.exchange(awxApiUrl + "/organizations/",
				HttpMethod.GET, request, OrganizationResponse.class);
		List<Organization> organizations = response.getBody().getResults();
		organizationRepository.saveAll(organizations);
		return organizations;
	}

	public List<Credential> fetchCredentials() {
		HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders());
		ResponseEntity<CredentialResponse> response = restTemplate.exchange(awxApiUrl + "/credentials/", HttpMethod.GET,
				request, CredentialResponse.class);
		List<Credential> credentials = response.getBody().getResults();
		credentialrepo.saveAll(credentials);
		return credentials;
	}

	public List<Project> fetchProjects() {
		HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders());
		ResponseEntity<ProjectResponseWrapper> response = restTemplate.exchange(awxApiUrl + "/projects/",
				HttpMethod.GET, request, ProjectResponseWrapper.class);
		List<ProjectResponse> projectResponses = response.getBody().getResults();

		// Map ProjectResponse to Project
		List<Project> projects = projectResponses.stream().map(pr -> {
			Project project = new Project();
			project.setId(pr.getId());
			project.setName(pr.getName());
			// Set other fields if needed
			return project;
		}).collect(Collectors.toList());

		projectRepository.saveAll(projects);
		return projects;
	}

	public List<JobTemplateResponse> fetchJobTemplates() {
		HttpEntity<Void> request = new HttpEntity<>(createAuthHeaders());
		ResponseEntity<JobtemplateResponseWrapper> response = restTemplate.exchange(awxApiUrl + "/job_templates/",
				HttpMethod.GET, request, JobtemplateResponseWrapper.class);
		List<JobTemplateResponse> jobTemplates = response.getBody().getResults();
		jobTemplateRepository.saveAll(jobTemplates);
		return jobTemplates;
	}

	public ProjectResponse createProject(Project project) {
		try {
			HttpEntity<Project> request = new HttpEntity<>(project, createAuthHeaders());
			ResponseEntity<ProjectResponse> response = restTemplate.postForEntity(awxApiUrl + "/projects/", request,
					ProjectResponse.class);
			ProjectResponse projectResponse = response.getBody();
			project.setId(projectResponse.getId());
			projectRepository.save(project);
			return projectResponse;
		} catch (Exception e) {
			logger.severe("Error creating project: " + e.getMessage());
			throw e;
		}
	}

	public Notification_temp createNotification(Notification_templates notification) {
		try {
			// Validate that the notification template has a name and organization ID
			if (notification.getName() == null || notification.getName().isEmpty()) {
				throw new IllegalArgumentException("Notification template name is required.");
			}
			if (notification.getOrganization() == null) {
				throw new IllegalArgumentException("Organization ID is required for notification template.");
			}

			// Log the start of the operation
			logger.info("Creating notification template: " + notification.getName());

			HttpEntity<Notification_templates> request = new HttpEntity<>(notification, createAuthHeaders());

			// Make the POST request to the AWX API
			ResponseEntity<Notification_temp> response = restTemplate
					.postForEntity(awxApiUrl + "/notification_templates/", request, Notification_temp.class);

			// Extract the response body
			Notification_temp notificationTemp = response.getBody();

			// Save the notification template to the database
			if (notificationTemp != null) {
				notification.setId(notificationTemp.getId());
				notificationtemplate.save(notification);
				logger.info("Successfully created notification template with ID: " + notificationTemp.getId());
			} else {
				logger.warning("Received empty response body while creating notification template.");
			}

			// Return the created notification template
			return notificationTemp;

		} catch (IllegalArgumentException e) {
			// Handle validation errors
			logger.severe("Validation error while creating notification template: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			// Handle other errors
			logger.severe("Error creating notification template: " + e.getMessage());
			throw e;
		}
	}

	public JobTemplateResponse createJobTemplate(JobTemplateRequest jobTemplateRequest) {
		try {
			HttpEntity<JobTemplateRequest> request = new HttpEntity<>(jobTemplateRequest, createAuthHeaders());
			ResponseEntity<JobTemplateResponse> response = restTemplate.postForEntity(awxApiUrl + "/job_templates/",
					request, JobTemplateResponse.class);
			JobTemplateResponse jobTemplateResponse = response.getBody();
			jobTemplateRepository.save(jobTemplateResponse);
			return jobTemplateResponse;
		} catch (Exception e) {
			logger.severe("Error creating job template: " + e.getMessage());
			throw e;
		}
	}

	public JsonNode triggerJob(int jobTemplateId) {
		String url = String.format("%s/job_templates/%d/launch/", awxApiUrl, jobTemplateId);
		HttpHeaders headers = createAuthHeaders();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, entity, JsonNode.class);

		return response.getBody();
	}

	public JsonNode getJobResult(int jobId) {
		String url = String.format("%s/jobs/%d/", awxApiUrl, jobId);
		HttpHeaders headers = createAuthHeaders();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

		return response.getBody();
	}

	public String getJobOutput(int jobId) {
		String url = String.format("%s/jobs/%d/stdout/?format=txt", awxApiUrl, jobId);
		HttpHeaders headers = createAuthHeaders();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		return response.getBody();
	}

	public String manageNotificationTemplate(Long jobTemplateId, Long notificationTemplateId,String type, boolean disassociate ) {
	 	   String endpoint;
	 	   switch(type.toLowerCase()) {
	 	   case "success":
	 		   endpoint = "notification_templates_success";
	 		   break;
	 	   case "started":
	 		   endpoint = "notification_templates_started";
	 		   break;
	 	   case "error":
	 		   endpoint = "notification_templates_error";
	 		   break;
	 	   default:
	 		   throw new IllegalArgumentException("Invalid notification type:"+ type);
	 	   }
	 	   
	 	   String url = String.format("%s/job_templates/%d/%s/", awxApiUrl, jobTemplateId, endpoint);
	 	   HttpHeaders header =  createAuthHeaders();
	 	   header.set("Content-Type", "application/json");
	 	   
	 	   Map<String, Object> request = new HashMap<>();
	 	   request.put("id", notificationTemplateId);
	 	   if(disassociate) {
	 		   request.put("disassociate", true);
	 	   }
	 	   
	 	   HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, header);
	 	   ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	 	   return response.getBody();
	    }
	    public void sendoutput(String to, Long jobId, String jobstatus) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject("AWX Job Notification");
	        message.setText("Job ID: " + jobId + "\nStatus: " + jobstatus);
	        mailsender.send(message);
	    }

}