package com.familytree.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.familytree.model.Person;
import com.familytree.service.PersonDAO;

@Controller
@RequestMapping("/")
public class IndexController {
	
	@Autowired
	PersonDAO personDao;

	  @RequestMapping(method = RequestMethod.GET)
	    public String getIndexPage() {
	        return "Home";
	    }
	  
	  @RequestMapping(value = "ancestors/{id}", method = RequestMethod.GET)
	    public String getAncestorsPage(@PathVariable int id, Model model) {
		  	Person p = personDao.get(id);
	        model.addAttribute("message", p.getName()+"'s Ancestors");
	        
	        String tree = generateTree(personDao.getAncestors(id));
	        model.addAttribute("ancestors", tree);
	        return "Ancestors";
	    }
	  
	  @RequestMapping(value = "descendants/{id}", method = RequestMethod.GET)
	    public String getDescendantsPage(@PathVariable int id, Model model) {
		  	Person p = personDao.get(id);
	        model.addAttribute("message", p.getName()+"'s Descendants");
	        String tree = generateDescendantsTree(personDao.getDescendants(id));
	        model.addAttribute("descendants", tree);

	        return "Descendants";
	    }
	  
	  public String generateTree(Map<String, Object> map) {
		  return "<div class='tree'>" + generateUL(map) + "</div>";
	  }
	  
	  public String generateUL(Map<String, Object> map) {
		  String html = "<ul><li>";
		  Person person = personDao.get((int) map.get("key"));
		  html += "<a href='#'>"+ person.getKey() + ". "+person.getName() + "</a>";

		  Map mother = ((Map<String, Object>) map.get("parents")) == null? null:(Map<String, Object>) ((Map<String, Object>) map.get("parents")).get("m");
		  Map father = ((Map<String, Object>) map.get("parents")) == null? null:(Map<String, Object>) ((Map<String, Object>) map.get("parents")).get("f");
		  
		  if(mother != null) {
			  html += generateUL(mother);
		  }
		  if(father != null ) {
			  html += generateUL(father);
		  }
		  html += "</li></ul>";
		  
		  return html;
	  }
	  
	  public String generateDescendantsTree(Map<String, Object> map) {
		  return "<div class='tree'>" + generateDescendantsUL(map) + "</div>";
	  }
	  
	  public String generateDescendantsUL(Map<String, Object> map) {
		  String html = "<ul><li>";
		  Person person = personDao.get((int) map.get("key"));
		  html += "<a href='#'>"+ person.getKey() + ". "+person.getName() + "</a>";
		  List childerns = (List) map.get("childerns");
		  if(childerns != null) {
			  for(Object m: childerns) {
				  html += generateDescendantsUL((Map<String, Object>) m);
			  }
		  }
		  html += "</li></ul>";
		  return html;
	  }

}