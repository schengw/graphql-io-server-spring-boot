
package com.thinkenterprise.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RouteRepository extends CrudRepository<Route, Long>{
	
	@Query("select a from Route a")
	List<Route> findAll();
}