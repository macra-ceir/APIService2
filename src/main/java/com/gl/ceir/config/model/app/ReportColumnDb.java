package com.gl.ceir.config.model.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "report_display_detail")
public class ReportColumnDb implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long report_column_id;
	
	@Nonnull
	@Column(name="report_name_id")
	private Long reportnameId;
	
	@Nonnull
	@Column(length=50)
	private String columnName;
	
	//@NotNull
	@Column(name="mysql_query", length=1000)
	private String columnQuery;
	
	@Column(name="oracle_query", length=1000)
	private String oracleQuery;
	
//	private Integer reportColumnId;
	
	private Integer source;
	
	@Nonnull
	@Column(length=50)
	private String headerName;
	
//	@Nonnull
	@Column(length=50)
	private String insertParameter;
	
	private Integer columnOrder;
	
	@CreationTimestamp
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@JsonFormat(pattern="dd-MM-yyyy")
	private LocalDateTime createdOn;


	@UpdateTimestamp
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@JsonFormat(pattern="dd-MM-yyyy")
	private LocalDateTime modifiedOn;
	
	private Integer typeFlag;
	
	@ManyToOne
	@JoinColumn(name="report_name_id",insertable = false, updatable = false)
	@JsonIgnore
	private ReportDb report;

	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	public Long getReport_column_id() {
		return report_column_id;
	}

	public void setReport_column_id(Long report_column_id) {
		this.report_column_id = report_column_id;
	}

	@Nonnull
	public Long getReportnameId() {
		return reportnameId;
	}

	public void setReportnameId(@Nonnull Long reportnameId) {
		this.reportnameId = reportnameId;
	}

	@Nonnull
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(@Nonnull String columnName) {
		this.columnName = columnName;
	}

	public String getColumnQuery() {
		return columnQuery;
	}

	public void setColumnQuery(String columnQuery) {
		this.columnQuery = columnQuery;
	}

	public String getOracleQuery() {
		return oracleQuery;
	}

	public void setOracleQuery(String oracleQuery) {
		this.oracleQuery = oracleQuery;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	@Nonnull
	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(@Nonnull String headerName) {
		this.headerName = headerName;
	}

	public String getInsertParameter() {
		return insertParameter;
	}

	public void setInsertParameter(String insertParameter) {
		this.insertParameter = insertParameter;
	}

	public Integer getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(Integer columnOrder) {
		this.columnOrder = columnOrder;
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

	public ReportDb getReport() {
		return report;
	}

	public void setReport(ReportDb report) {
		this.report = report;
	}

	@Override
	public String toString() {
		return "ReportColumnDb{" +
				"report_column_id=" + report_column_id +
				", reportnameId=" + reportnameId +
				", columnName='" + columnName + '\'' +
				", columnQuery='" + columnQuery + '\'' +
				", oracleQuery='" + oracleQuery + '\'' +
				", source=" + source +
				", headerName='" + headerName + '\'' +
				", insertParameter='" + insertParameter + '\'' +
				", columnOrder=" + columnOrder +
				", createdOn=" + createdOn +
				", modifiedOn=" + modifiedOn +
				", typeFlag=" + typeFlag +
				", report=" + report +
				'}';
	}
}
