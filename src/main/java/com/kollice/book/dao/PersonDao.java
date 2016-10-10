package com.kollice.book.dao;


import com.kollice.book.domain.Person;
import com.kollice.book.framework.base.CustomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonDao extends CustomRepository<Person, Long> {
	List<Person> findByAddress(String address);
	
	Person findByNameAndAddress(String name, String address);
	
	@Query("select p from Person p where p.name= :name and p.address= :address")
	Person withNameAndAddressQuery(@Param("name") String name, @Param("address") String address);
	
	Person withNameAndAddressNamedQuery(String name, String address);

}
