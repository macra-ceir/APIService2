package com.gl.ceir.config.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.app.FileDetails;
import com.gl.ceir.config.model.app.FileDumpFilterRequest;
import com.gl.ceir.config.model.app.FileDumpMgmt;
import com.gl.ceir.config.model.app.FileListFileModel;
import com.gl.ceir.config.model.app.OperatorDumpMgmt;
import com.gl.ceir.config.model.app.SearchCriteria;
import com.gl.ceir.config.model.app.SystemConfigurationDb;
import com.gl.ceir.config.model.app.User;
import com.gl.ceir.config.model.aud.AuditTrail;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.FileDumpOrderColumnMapping;
import com.gl.ceir.config.model.constants.FileDumpType;
import com.gl.ceir.config.model.constants.FileType;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.repository.app.FileDumpMgmtRepository;
import com.gl.ceir.config.repository.app.OperatorFileDumpRepository;
import com.gl.ceir.config.repository.app.StakeholderFeatureRepository;
import com.gl.ceir.config.repository.app.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.app.UserRepository;
import com.gl.ceir.config.repository.aud.AuditTrailRepository;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.gl.ceir.config.util.InterpSetter;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class OperatorFileDetailsImpl {


	private static final Logger logger = LogManager.getLogger(ListFileDetailsImpl.class);


	@Autowired
	FileDumpMgmtRepository fileDumpMgmtRepository;
	@Autowired
	OperatorFileDumpRepository operatorFileDumpRepository;
	@Autowired
	PropertiesReader propertiesReader;
	@Autowired
	FileStorageProperties fileStorageProperties;
	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;
	@Autowired
	AuditTrailRepository auditTrailRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	StakeholderFeatureRepository stakeholderFeatureRepository;

	@Autowired
	InterpSetter interpSetter;
	
	public List<FileDumpMgmt> getByListType(String listType){
		try {

			return fileDumpMgmtRepository.getByServiceDump(listType);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	
	public Page<OperatorDumpMgmt> getFilterPagination( FileDumpFilterRequest filterRequest, Integer pageNo, Integer pageSize) {
		boolean isDefaultFilter = true;
		Pageable pageable = null;
		try {
			Sort.Direction direction;
			if ( Objects.nonNull(filterRequest.getOrder()) && filterRequest.getOrder().equalsIgnoreCase("asc") ) {
				direction = Sort.Direction.ASC;
			} else {
				direction = Sort.Direction.DESC;				
			}
			if( Objects.nonNull( filterRequest.getOrderColumnName()) && 
					Objects.nonNull(FileDumpOrderColumnMapping.getColumnMapping(filterRequest.getOrderColumnName()))) {
				pageable = PageRequest.of(pageNo, pageSize,
						new Sort(direction, FileDumpOrderColumnMapping.getColumnMapping(filterRequest.getOrderColumnName()).name()));
			} else {
				pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, "modifiedOn"));
			}
//			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.DESC, "modifiedOn"));
			GenericSpecificationBuilder<OperatorDumpMgmt> osb = new GenericSpecificationBuilder<OperatorDumpMgmt>(propertiesReader.dialect);
			if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().equals("")) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("createdOn", filterRequest.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));
			}
			
			if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().equals("")) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("createdOn",filterRequest.getEndDate() , SearchOperation.LESS_THAN, Datatype.DATE));
			}
			
			
			
			if(Objects.nonNull(filterRequest.getFileType()) && !filterRequest.getFileType().equals("")) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("fileType", filterRequest.getFileType(), SearchOperation.EQUALITY, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getFileName()) && !filterRequest.getFileName().equals("")) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("fileName", filterRequest.getFileName(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			if(Objects.nonNull(filterRequest.getCopyStatus()) && !filterRequest.getCopyStatus().equals(null)) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("copyStatus", filterRequest.getCopyStatus(), SearchOperation.EQUALITY, Datatype.INT));
			}
			
		
			if(Objects.nonNull(filterRequest.getListType()) && !filterRequest.getListType().equals("")) {
				isDefaultFilter = false;
				osb.with(new SearchCriteria("listType", filterRequest.getListType(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			

			if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().equals("")){
				if( filterRequest.getListType() == null || filterRequest.getListType().equals("") ) {
					if( filterRequest.getListType().toString().toLowerCase().contains( filterRequest.getSearchString().toLowerCase() )) {
						osb.orSearch(new SearchCriteria("listType",  filterRequest.getListType(), SearchOperation.EQUALITY, Datatype.STRING));
					}
				}
				osb.orSearch(new SearchCriteria("listType", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			}
			
			Page<OperatorDumpMgmt> page =operatorFileDumpRepository.findAll(osb.build(), pageable);
			for (OperatorDumpMgmt operatorDumpMgmt : page.getContent()) {
				operatorDumpMgmt.setFileTypeInterp(interpSetter.setConfigInterpDump("FileTypeInterp", operatorDumpMgmt.getFileType()));
				operatorDumpMgmt.setFileCopyInterp(interpSetter.setCopyInterpDump("FileCopyInterp", operatorDumpMgmt.getCopyStatus()));
			}

			
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
			if( !isDefaultFilter )
				auditTrail.setSubFeature("Filter");
			else
				auditTrail.setSubFeature("View All");
			auditTrail.setFeatureId(filterRequest.getFeatureId());
			if( Objects.nonNull(filterRequest.getPublicIp()))
				auditTrail.setPublicIp(filterRequest.getPublicIp());
			if( Objects.nonNull(filterRequest.getBrowser()))
				auditTrail.setBrowser(filterRequest.getBrowser());
			if( Objects.nonNull(filterRequest.getUserId()) ) {
				User user = userRepository.getByid( filterRequest.getUserId());
				auditTrail.setUserId( filterRequest.getUserId() );
				auditTrail.setUserName( user.getUsername());
			}else {
				auditTrail.setUserName( "NA");
			}
			if( Objects.nonNull(filterRequest.getUserType()) ) {
				auditTrail.setUserType( filterRequest.getUserType());
				auditTrail.setRoleType( filterRequest.getUserType() );
			}else {
				auditTrail.setUserType( "NA" );
				auditTrail.setRoleType( "NA" );
			}
			auditTrail.setTxnId("NA");
			auditTrailRepository.save(auditTrail);
			return page;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	
	public FileDetails getFilterInFileV2(FileDumpFilterRequest filterRequest, Integer pageNo, Integer pageSize) {
		String fileName = null;
		Writer writer   = null;
		FileListFileModel flm = null;
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String filePath  = systemConfigurationDbRepository.getByTag("file.download-dir").getValue();
		StatefulBeanToCsvBuilder<FileListFileModel> builder = null;
		StatefulBeanToCsv<FileListFileModel> csvWriter      = null;
		List<OperatorDumpMgmt> fileRecords = null;
		List<FileListFileModel> fileDetails  = null;
		CustomMappingStrategy<FileListFileModel> mappingStrategy = new CustomMappingStrategy<FileListFileModel>();
		try {
			pageNo = 0;
			pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
			fileRecords = this.getFilterPagination(filterRequest, pageNo, pageSize).getContent();
			/*
			 * if( FileDumpType.getActionNames( filterRequest.getServiceDump()
			 * ).toString().equalsIgnoreCase("black") ) fileName =
			 * LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).
			 * replace(" ", "_")+"_Black_List.csv"; else fileName =
			 * LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).
			 * replace(" ", "_")+"_Gray_List.csv";
			 */
			logger.info("filter request for ");
			if(filterRequest.getListType().contains("EXCEPTIONLIST")) {
				fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).replace(" ", "_")+"_Exception_List_Dump.csv";
			}
			else if(filterRequest.getListType().contains("BLACKLIST")) {
				fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).replace(" ", "_")+"_Black_List_Dump.csv";
			}
			else if(filterRequest.getListType().contains("BLOCKEDTACLIST")) {
				fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).replace(" ", "_")+"_BlockedTAC_List_Dump.csv";
			}
			else if(filterRequest.getListType().contains("TRACKEDLIST")) {
				fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).replace(" ", "_")+"_Tracked_List_Dump.csv";
			}
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(FileListFileModel.class);
			builder = new StatefulBeanToCsvBuilder<FileListFileModel>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			if( fileRecords.size() > 0 ) {
				fileDetails = new ArrayList<FileListFileModel>();
				for( OperatorDumpMgmt fdm : fileRecords ) {
					flm = new FileListFileModel();
					flm.setCreatedOn( fdm.getCreatedOn().format(dtf));
					flm.setModifieddOn(fdm.getCreatedOn().format(dtf));
					flm.setFileName( fdm.getFileName() );
					flm.setFilePath(fdm.getFilePath());
					flm.setOperatorName(fdm.getOperatorName());
					flm.setCount(fdm.getRecordCount());
					flm.setFileCopyInterp(fdm.getFileCopyInterp());
					flm.setFileType( fdm.getFileTypeInterp() );
					fileDetails.add(flm);
				}
				csvWriter.write(fileDetails);
			}else {
				csvWriter.write( new FileListFileModel());
			}
			AuditTrail auditTrail = new AuditTrail();
			auditTrail.setFeatureName(stakeholderFeatureRepository.findById(filterRequest.getFeatureId()).get().getName());
			auditTrail.setSubFeature("Export");
			auditTrail.setFeatureId(filterRequest.getFeatureId());
			if( Objects.nonNull(filterRequest.getPublicIp()))
				auditTrail.setPublicIp(filterRequest.getPublicIp());
			if( Objects.nonNull(filterRequest.getBrowser()))
				auditTrail.setBrowser(filterRequest.getBrowser());
			if( Objects.nonNull(filterRequest.getUserId()) ) {
				User user = userRepository.getByid( filterRequest.getUserId());
				auditTrail.setUserId( filterRequest.getUserId() );
				auditTrail.setUserName( user.getUsername());
			}else {
				auditTrail.setUserName( "NA");
			}
			if( Objects.nonNull(filterRequest.getUserType()) ) {
				auditTrail.setUserType( filterRequest.getUserType());
				auditTrail.setRoleType( filterRequest.getUserType() );
			}else {
				auditTrail.setUserType( "NA" );
				auditTrail.setRoleType( "NA" );
			}
			auditTrail.setTxnId("NA");
			auditTrailRepository.save(auditTrail);
			return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("file.download-link").getValue().replace("$LOCAL_IP",
					propertiesReader.localIp)+fileName );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( writer != null )
					writer.close();
			} catch (IOException e) {}
		}

	}

	public FileDetails getFile(String fileName) {
		//String filePath  = fileStorageProperties.getFileDumpDownloadDir();
		
		try {
			//return new FileDetails( fileName, filePath, fileStorageProperties.getFileDumpDownloadLink()+fileName );
			if( fileName.toLowerCase().contains("black")) {
				String filePath  = systemConfigurationDbRepository.getByTag("BLACKLIST_FILEPATH").getValue();
				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("BLACKLIST_DOWNLOAD_LINK").getValue().replace("$LOCAL_IP",
						propertiesReader.localIp)+fileName );
			}
			else if( fileName.toLowerCase().contains("exception")) {
				String filePath  = systemConfigurationDbRepository.getByTag("EXCEPTIONLIST_FILEPATH").getValue();
				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("EXCEPTIONLIST_DOWNLOAD_LINK").getValue().replace("$LOCAL_IP",
						propertiesReader.localIp)+fileName );
			}
			else if( fileName.toLowerCase().contains("blacklist")) {
				String filePath  = systemConfigurationDbRepository.getByTag("BLACKLIST_FILEPATH").getValue();
				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("BLACKLIST_DOWNLOAD_LINK").getValue().replace("$LOCAL_IP",
						propertiesReader.localIp)+fileName );
			}
			else if( fileName.toLowerCase().contains("blockedtaclist")) {
				String filePath  = systemConfigurationDbRepository.getByTag("BLOCKEDTACLIST_FILEPATH").getValue();
				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("BLOCKEDTACLIST_DOWNLOAD_LINK").getValue().replace("$LOCAL_IP",
						propertiesReader.localIp)+fileName );
			}
			else if( fileName.toLowerCase().contains("allowedtaclist")) {
				String filePath  = systemConfigurationDbRepository.getByTag("ALLOWEDTACLIST_FILEPATH").getValue();
				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("ALLOWEDTACLIST_DOWNLOAD_LINK").getValue().replace("$LOCAL_IP",
						propertiesReader.localIp)+fileName );
			}
			else {
//				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("GREYLIST_DOWNLOAD_LINK").getValue()+fileName );
				String filePath  = systemConfigurationDbRepository.getByTag("GREYLIST_FILEPATH").getValue();
				return new FileDetails( fileName, filePath, systemConfigurationDbRepository.getByTag("GREYLIST_DOWNLOAD_LINK").getValue().replace("$LOCAL_IP",
						propertiesReader.localIp)+fileName );
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	

}
