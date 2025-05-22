package com.gl.ceir.config.model.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "list_file_mgmt")
public class OperatorDumpMgmt implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdOn;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@UpdateTimestamp
	private LocalDateTime modifiedOn;
	private String fileName;
	private String filePath;
	private String sourceServer;
	private String listType;
	private String operatorName;
	private String fileType;
	private Integer fileState;
	private Integer recordCount;
	private Integer copyStatus;
	@Transient
	private String dumpTypeInterp;
	@Transient
	private String fileTypeInterp;
	
	@Transient
	private String fileCopyInterp;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSourceServer() {
		return sourceServer;
	}

	public void setSourceServer(String sourceServer) {
		this.sourceServer = sourceServer;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getFileState() {
		return fileState;
	}

	public void setFileState(Integer fileState) {
		this.fileState = fileState;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public Integer getCopyStatus() {
		return copyStatus;
	}

	public void setCopyStatus(Integer copyStatus) {
		this.copyStatus = copyStatus;
	}

	public String getDumpTypeInterp() {
		return dumpTypeInterp;
	}

	public void setDumpTypeInterp(String dumpTypeInterp) {
		this.dumpTypeInterp = dumpTypeInterp;
	}

	public String getFileTypeInterp() {
		return fileTypeInterp;
	}

	public void setFileTypeInterp(String fileTypeInterp) {
		this.fileTypeInterp = fileTypeInterp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFileCopyInterp() {
		return fileCopyInterp;
	}

	public void setFileCopyInterp(String fileCopyInterp) {
		this.fileCopyInterp = fileCopyInterp;
	}

	@Override
	public String toString() {
		return "OperatorDumpMgmt [id=" + id + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", fileName="
				+ fileName + ", filePath=" + filePath + ", sourceServer=" + sourceServer + ", listType=" + listType
				+ ", operatorName=" + operatorName + ", fileType=" + fileType + ", fileState=" + fileState
				+ ", recordCount=" + recordCount + ", copyStatus=" + copyStatus + ", dumpTypeInterp=" + dumpTypeInterp
				+ ", fileTypeInterp=" + fileTypeInterp + ", fileCopyInterp=" + fileCopyInterp + "]";
	}

	
	
	
	
}
