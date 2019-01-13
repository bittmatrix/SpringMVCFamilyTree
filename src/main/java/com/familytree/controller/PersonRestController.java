package com.familytree.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.familytree.configuration.Result;
import com.familytree.model.Person;
import com.familytree.service.PersonDAO;

@RestController
@RequestMapping("/GE")
public class PersonRestController {

	@Autowired
	private PersonDAO personDAO;


	@GetMapping("/persons")
	public List getPersons() {
		return personDAO.list();
	}

	@GetMapping("/person/get/{id}")
	public ResponseEntity  getPerson(@PathVariable int id) {

		Person person = personDAO.get(id);
		Result resultObj = new Result();
		if (person.getName() != null) {
			resultObj.setResult("true");
			return new ResponseEntity(person, HttpStatus.OK);
		} else {
			resultObj.setResult("false");
			resultObj.setMessage("key " + id + " does not exist");
			return new ResponseEntity(resultObj, HttpStatus.OK);
		}
	}
	
	@GetMapping("/person/ancestors/{id}")
	public ResponseEntity getAncestors(@PathVariable int id) {
		Map<String, Object> map = personDAO.getAncestors(id);
		Result resultObj = new Result();
		if (map.size() > 0) {
			resultObj.setResult("true");
			return new ResponseEntity(map, HttpStatus.OK);
		} else {
			resultObj.setResult("false");
			resultObj.setMessage("key " + id + " does not exist");
			return new ResponseEntity(resultObj, HttpStatus.OK);
		}
	}
	
	@GetMapping("/person/descendants/{id}")
	public ResponseEntity getDescendants(@PathVariable int id) {
		Map<String, Object> map = personDAO.getDescendants(id);
		Result resultObj = new Result();
		if (map.size() > 0) {
			resultObj.setResult("true");
			return new ResponseEntity(map, HttpStatus.OK);
		} else {
			resultObj.setResult("false");
			resultObj.setMessage("key " + id + " does not exist");
			return new ResponseEntity(resultObj, HttpStatus.OK);
		}
	}

	@RequestMapping(value="/person/add",method=RequestMethod.GET)
	public ResponseEntity  addPerson(@RequestParam("key") int key,@RequestParam("name") String name, @RequestParam(value = "dob", required=false, defaultValue = "0") int dob
			,@RequestParam(value = "m" , required=false, defaultValue = "0") int m, @RequestParam(value = "f", required=false, defaultValue = "0") int f, @RequestParam(value = "g", required=false) String g) {
		boolean result=personDAO.create(new Person(key, name, m, f, dob, g));
		Result resultObj = new Result();
		if(result) {
			resultObj.setResult("true");
		}else {
			resultObj.setResult("false");
			resultObj.setMessage("person id already exists (or m/f id does not exist)");
		}
		return new ResponseEntity(resultObj, HttpStatus.OK);
	}

	@PostMapping(value = "/person/addJson")
	public ResponseEntity createPerson(@RequestBody Person person) {

		boolean result=personDAO.create(person);
		Result resultObj = new Result();
		if(result) {
			resultObj.setResult("true");
		}else {
			resultObj.setResult("false");
			resultObj.setMessage("person id already exists (or m/f id does not exist)");
		}
		return new ResponseEntity(resultObj, HttpStatus.OK);
	}

	@GetMapping("/person/delete/{id}")
	public ResponseEntity deletePerson(@PathVariable int id) {
		Result resultObj = new Result();
		int retValue = personDAO.delete(id);
		if (retValue == 1) {
			resultObj.setResult("true");
		}else {
			resultObj.setResult("false");
			resultObj.setMessage("key "+ id + " does not exist");
		}

		return new ResponseEntity(resultObj, HttpStatus.OK);

	}

	@PutMapping("/person/update/{id}")
	public ResponseEntity updatePerson(@PathVariable int id, @RequestBody Person person) {

		person = personDAO.update(id, person);

		if (null == person) {
			return new ResponseEntity("No Person found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(person, HttpStatus.OK);
	}

}
