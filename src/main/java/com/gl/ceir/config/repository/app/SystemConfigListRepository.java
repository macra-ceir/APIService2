package com.gl.ceir.config.repository.app;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gl.ceir.config.model.app.SystemConfigListDb;

public interface SystemConfigListRepository extends CrudRepository<SystemConfigListDb, Long>, 
JpaRepository<SystemConfigListDb, Long>, JpaSpecificationExecutor<SystemConfigListDb>{
	
	public List<SystemConfigListDb> findByTag(String tag, Sort sort);
	
	public List<SystemConfigListDb> findByTag(String tag);
	
	@Query("SELECT DISTINCT a.tag FROM SystemConfigListDb a")
	List<String> findDistinctTags();
	
	@Query("SELECT NEW com.gl.ceir.config.model.app.SystemConfigListDb(a.tag, a.description, a.displayName) FROM SystemConfigListDb a group by a.tag, a.description, a.displayName")
	List<SystemConfigListDb> findDistinctTagsWithDescription();
	
	public SystemConfigListDb getById(long id);
	
}
