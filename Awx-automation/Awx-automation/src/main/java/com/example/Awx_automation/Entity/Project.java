package com.example.Awx_automation.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document( collection = "project")
public class Project {
	
	
	@Id
	private Long id;
    private String name;
    private String description = "";
    private String scmType = "";
    private String localPath = "";
    private String scmUrl = "";
    private String scmBranch = "";
    private String scmRefspec = "";
    private boolean scmClean = false;
    private boolean scmTrackSubmodules = false;
    private boolean scmDeleteOnUpdate = false;
    private Long credential;
    private int timeout = 0;
    private Long organization;
    private boolean scmUpdateOnLaunch = false;
    private int scmUpdateCacheTimeout = 0;
    private boolean allowOverride = false;
    private Long defaultEnvironment;
    private Long signatureValidationCredential;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getScmType() {
		return scmType;
	}
	public void setScmType(String scmType) {
		this.scmType = scmType;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getScmUrl() {
		return scmUrl;
	}
	public void setScmUrl(String scmUrl) {
		this.scmUrl = scmUrl;
	}
	public String getScmBranch() {
		return scmBranch;
	}
	public void setScmBranch(String scmBranch) {
		this.scmBranch = scmBranch;
	}
	public String getScmRefspec() {
		return scmRefspec;
	}
	public void setScmRefspec(String scmRefspec) {
		this.scmRefspec = scmRefspec;
	}
	public boolean isScmClean() {
		return scmClean;
	}
	public void setScmClean(boolean scmClean) {
		this.scmClean = scmClean;
	}
	public boolean isScmTrackSubmodules() {
		return scmTrackSubmodules;
	}
	public void setScmTrackSubmodules(boolean scmTrackSubmodules) {
		this.scmTrackSubmodules = scmTrackSubmodules;
	}
	public boolean isScmDeleteOnUpdate() {
		return scmDeleteOnUpdate;
	}
	public void setScmDeleteOnUpdate(boolean scmDeleteOnUpdate) {
		this.scmDeleteOnUpdate = scmDeleteOnUpdate;
	}
	public Long getCredential() {
		return credential;
	}
	public void setCredential(Long credential) {
		this.credential = credential;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public Long getOrganization() {
		return organization;
	}
	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	public boolean isScmUpdateOnLaunch() {
		return scmUpdateOnLaunch;
	}
	public void setScmUpdateOnLaunch(boolean scmUpdateOnLaunch) {
		this.scmUpdateOnLaunch = scmUpdateOnLaunch;
	}
	public int getScmUpdateCacheTimeout() {
		return scmUpdateCacheTimeout;
	}
	public void setScmUpdateCacheTimeout(int scmUpdateCacheTimeout) {
		this.scmUpdateCacheTimeout = scmUpdateCacheTimeout;
	}
	public boolean isAllowOverride() {
		return allowOverride;
	}
	public void setAllowOverride(boolean allowOverride) {
		this.allowOverride = allowOverride;
	}
	public Long getDefaultEnvironment() {
		return defaultEnvironment;
	}
	public void setDefaultEnvironment(Long defaultEnvironment) {
		this.defaultEnvironment = defaultEnvironment;
	}
	public Long getSignatureValidationCredential() {
		return signatureValidationCredential;
	}
	public void setSignatureValidationCredential(Long signatureValidationCredential) {
		this.signatureValidationCredential = signatureValidationCredential;
	}
    
    
}
