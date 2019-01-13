package com.familytree.model;

//import javax.persistence.Entity;
//import javax.persistence.EntityListeners;
//import javax.persistence.Table;

//import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

//@Entity
//@Table(name="Person")
//@EntityListeners(AuditingEntityListener.class)
public class Person {

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty("m")
	public int getMotherKey() {
		return motherKey;
	}

	public void setMotherKey(int motherKey) {
		this.motherKey = motherKey;
	}
	
	@JsonProperty("f")
	public int getFatherKey() {
		return fatherKey;
	}

	public void setFatherKey(int fatherKey) {
		this.fatherKey = fatherKey;
	}
	
	@JsonProperty("dob")
	public int getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(int dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	@JsonProperty("g")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Person() {
		super();
	}

	public Person(int key, String name) {
		super();
		this.key = key;
		this.name = name;
	}

	public Person(int key, String name, int motherKey, int fatherKey, int dateOfBirth, String gender) {
		super();
		this.key = key;
		this.name = name;
		this.motherKey = motherKey;
		this.fatherKey = fatherKey;
		DateOfBirth = dateOfBirth;
		this.gender = gender;
	}

	int key;	
	String name;
	int motherKey;
	int fatherKey;
	int DateOfBirth; //19921210->December 10th 1992 
	String gender;

}