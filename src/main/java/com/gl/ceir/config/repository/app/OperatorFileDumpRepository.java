package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import com.gl.ceir.config.model.app.OperatorDumpMgmt;
import com.gl.ceir.config.model.app.ResponseCountAndQuantity;

import io.lettuce.core.dynamic.annotation.Param;

public interface OperatorFileDumpRepository extends JpaRepository<OperatorDumpMgmt, Long>, JpaSpecificationExecutor<OperatorDumpMgmt> {

	//public List<OperatorDumpMgmt> getByServiceDump(String serviceDump);
	
	/*
	 * @Query(
	 * value="select new com.gl.ceir.config.model.app.ResponseCountAndQuantity(count(f.id) as count) from OperatorDumpMgmt f "
	 * + "where f.serviceDump =:serviceDump") public ResponseCountAndQuantity
	 * getFileDumpCount( @Param("serviceDump") Integer serviceDump);
	 */
 

}
