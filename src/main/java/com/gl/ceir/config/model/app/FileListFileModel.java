package com.gl.ceir.config.model.app;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class FileListFileModel {
	
	@CsvBindByName(column = "Created On")
	@CsvBindByPosition(position = 0)
	private String createdOn;
	
	@CsvBindByName(column = "Modified On")
	@CsvBindByPosition(position = 1)
	private String modifieddOn;
	
	@CsvBindByName(column = "File Name")
	@CsvBindByPosition(position = 2)
	private String fileName;
	
	@CsvBindByName(column = "File Path")
	@CsvBindByPosition(position = 3)
	private String filePath;
	
	@CsvBindByName(column = "Operator Name")
	@CsvBindByPosition(position = 4)
	private String operatorName;
	
	
	@CsvBindByName(column = "Count")
	@CsvBindByPosition(position = 5)
	private int count;
	
	@CsvBindByName(column = "File Copy Status")
	@CsvBindByPosition(position = 6)
	private String fileCopyInterp;
	
	@CsvBindByName(column = "File Type")
	@CsvBindByPosition(position = 7)
	private String fileType;


	public String getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}


	public String getModifieddOn() {
		return modifieddOn;
	}


	public void setModifieddOn(String modifieddOn) {
		this.modifieddOn = modifieddOn;
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


	public String getOperatorName() {
		return operatorName;
	}


	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}


	public String getFileCopyInterp() {
		return fileCopyInterp;
	}


	public void setFileCopyInterp(String fileCopyInterp) {
		this.fileCopyInterp = fileCopyInterp;
	}


	@Override
	public String toString() {
		return "FileListFileModel [createdOn=" + createdOn + ", modifieddOn=" + modifieddOn + ", fileName=" + fileName
				+ ", filePath=" + filePath + ", operatorName=" + operatorName + ", count=" + count + ", fileCopyInterp="
				+ fileCopyInterp + ", fileType=" + fileType + "]";
	}


	
	
	
}
