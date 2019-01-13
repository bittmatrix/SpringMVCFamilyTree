package com.familytree.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.familytree.configuration.ConnectionUtils;
import com.familytree.model.Person;

@Repository
@Qualifier("personDAO")
public class PersonDAO {

	List<Person> persons = new ArrayList<Person>();

	public List list() {
		Connection con = ConnectionUtils.getConnection();
		persons = new ArrayList<Person>();
		if (null != con) {
			try {						 
				String sqlSelectQuery = "select * from person";
				PreparedStatement preparedStatement = con.prepareStatement(sqlSelectQuery);
				ResultSet  rs = preparedStatement.executeQuery();
				while(rs.next()) {
					Person person = new Person();
					person.setKey(rs.getInt("key"));							 
					person.setName(rs.getString("name"));
					person.setDateOfBirth(rs.getInt("dateOfBirth"));
					person.setGender(rs.getString("gender"));
					person.setMotherKey(rs.getInt("motherid"));
					person.setFatherKey(rs.getInt("fatherid"));
					persons.add(person);
				}
					

			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return persons;
	}

	public Person get(int key) {
		Connection con = ConnectionUtils.getConnection();
		Person person = new Person();
		if (null != con) {
			try {						 
				String sqlSelectQuery = 
						"select * from person where `key`=?";
				PreparedStatement preparedStatement = con.prepareStatement(sqlSelectQuery);
				preparedStatement.setLong(1,key);
				ResultSet  rs = preparedStatement.executeQuery();
				

				while(rs.next()) {
					person.setKey(rs.getInt("key"));							 
					person.setName(rs.getString("name"));
					person.setDateOfBirth(rs.getInt("dateOfBirth"));
					person.setGender(rs.getString("gender"));
					person.setMotherKey(rs.getInt("motherid"));
					person.setFatherKey(rs.getInt("fatherid"));
				}

			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return person;

	}
	
	public Map getAncestors(int id) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		Person person = get(id);
		map.put("key", person.getKey());
		map.put("parents", getParents(person.getFatherKey(), person.getMotherKey()));
		
		return map;
	}
	
	public Map getDescendants(int id) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put("key", id);
		map.put("childerns", getChildrens(id));
		
		return map;
	}
	
	public List<Integer> getChilds(int key) {
		Connection con = ConnectionUtils.getConnection();
		List<Integer> childs = new ArrayList<Integer>();
		if (null != con) {
			try {						 
				String sqlSelectQuery = 
						"select `key` from person where motherId = ? or fatherId = ?";
				PreparedStatement preparedStatement = con.prepareStatement(sqlSelectQuery);
				preparedStatement.setLong(1,key);
				preparedStatement.setLong(2,key);
				ResultSet  rs = preparedStatement.executeQuery();
				
				while(rs.next()) {
					childs.add(rs.getInt("key"));							 
				}

			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return childs;

	}
	
	public List<Map<String, Object>> getChildrens(int key) {
		List<Integer> childs = getChilds(key);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (childs.size() > 0) {
			for(int i: childs) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("key", i);
				if (getChildrens(i).size() > 0)
					map.put("childerns", getChildrens(i));
				list.add(map);
			}
		}
		
		return list;
	} 
	
	public Map getParents(int f, int m) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		if (m != 0) {
			Map<String, Object> innerMap = new LinkedHashMap<String, Object>();
			innerMap.put("key", m);
			Person person = get(m);
			Map<String, Object> parents =  getParents(person.getFatherKey(), person.getMotherKey());
			if(parents.size() > 0) 
				innerMap.put("parents", getParents(person.getFatherKey(), person.getMotherKey()));
			
			map.put("m", innerMap);
		}
		
		if (f != 0) {
			Map<String, Object> innerMap = new LinkedHashMap<String, Object>();
			innerMap.put("key", f);
			Person person = get(f);
			Map<String, Object> parents =  getParents(person.getFatherKey(), person.getMotherKey());
			if(parents.size() > 0) 
				innerMap.put("parents", getParents(person.getFatherKey(), person.getMotherKey()));
			
			map.put("f", innerMap);
		}
		
		return map;
	}

	public boolean create(Person person) {
		Connection con = ConnectionUtils.getConnection();
		if (null != con) {
			try {
				String ADD_QUERY = "insert into person(`key`, `name`, motherid, fatherid, gender, dateOfBirth) values (?,?,?,?,?,?)";
				PreparedStatement stmt = con.prepareStatement(ADD_QUERY);
				stmt.setInt(1, person.getKey());
				stmt.setString(2, person.getName());// 1 specifies the first parameter in the query
				stmt.setInt(3, person.getMotherKey());
				stmt.setInt(4, person.getFatherKey());
				stmt.setString(5, person.getGender());
				stmt.setInt(6, person.getDateOfBirth());
				int i = stmt.executeUpdate();
				con.close();
			} catch (Exception e) {
				System.out.println(e);
				return false;
			}
		}

		return true;
	}

	public int delete(int key) { 
		Connection con = ConnectionUtils.getConnection();
		Person person = new Person();
		if (null != con) {
			try {						 
				String sqlSelectQuery = 
						"delete  from person where `key`=?";
				PreparedStatement preparedStatement = con.prepareStatement(sqlSelectQuery);
				preparedStatement.setInt(1,key); 
				return preparedStatement.executeUpdate();
			} catch (Exception e) {
				System.out.println(e);
				return -1;
			} 
		}
		return -1;
	}

	public Person update(int id, Person person) {

		for (Person c : persons) {
			if (c.getKey() == id) {
				person.setKey(c.getKey());
				persons.remove(c);
				persons.add(person);
				return person;
			}
		}

		return null;
	}
}
