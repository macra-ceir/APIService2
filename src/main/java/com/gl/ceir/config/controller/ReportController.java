package com.gl.ceir.config.controller;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.app.DBTableNames;
import com.gl.ceir.config.model.app.ReportDataSorting;
import com.gl.ceir.config.model.app.ReportTrend;
import com.gl.ceir.config.model.app.TableData;
import com.gl.ceir.config.model.app.TableFilterRequest;
import com.gl.ceir.config.service.impl.ReportServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class ReportController {
	
	private static final Logger logger = LogManager.getLogger(ReportController.class);
	
	@Autowired
	ReportServiceImpl reportServiceImpl;
	
	@ApiOperation(value = "Get all tables.", response = DBTableNames.class)
	@RequestMapping(path = "/report/list", method = {RequestMethod.POST})
	public MappingJacksonValue getAllReport( @RequestParam(value = "reportCategory", defaultValue="0") Integer reportCategory, 
			@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "userType") String userType,
			@RequestParam(value = "featureId") Long featureId,
			@RequestParam(value = "publicIp", defaultValue="NA") String publicIp,
			@RequestParam(value = "browser", defaultValue="NA") String browser) {
		return new MappingJacksonValue( reportServiceImpl.getAllReport( reportCategory, userId, userType, featureId,
				publicIp, browser) );
	}
	
	@ApiOperation(value = "Get all table columns.", response = DBTableNames.class)
	@RequestMapping(path = "/report/columnList", method = {RequestMethod.POST})
	public MappingJacksonValue getAllColumns( @RequestParam(value = "reportnameId") Long reportnameId,
			@RequestParam(value = "typeFlag", defaultValue="2", required=false ) Integer typeFlag ) {
		if( Objects.nonNull(typeFlag) && !typeFlag.equals(0))
			return new MappingJacksonValue( reportServiceImpl.getColumns(reportnameId, typeFlag ));
		else
			return new MappingJacksonValue( reportServiceImpl.getColumns(reportnameId));
	}
	
	@ApiOperation(value = "Get report trend list.", response = ReportTrend.class)
	@RequestMapping(path = "/report/details", method = {RequestMethod.POST})
	public MappingJacksonValue getTrendList( @RequestParam(value = "reportnameId") Long reportnameId ) {
		return new MappingJacksonValue( reportServiceImpl.getSingleReportDetail(  reportnameId ));
	}
	
	@ApiOperation(value = "Get report data.")
	@RequestMapping(path = "/report/data", method = {RequestMethod.POST})
	public MappingJacksonValue getTableData(@RequestBody TableFilterRequest filterRequest,
												@RequestParam(value = "pageNumber", defaultValue="0") int pageNumber,
												@RequestParam(value = "pageSize", defaultValue="10") int pageSize,
												@RequestParam(value = "file", defaultValue="0", required=false) int file) {
		logger.info("Report filter request:["+filterRequest.toString()+"]");
		if( file == 0 )
			return new MappingJacksonValue( reportServiceImpl.getReportData(filterRequest, pageNumber, pageSize) );
		else
			return new MappingJacksonValue( reportServiceImpl.downloadReportData(filterRequest, pageNumber, pageSize) );
	}
}
