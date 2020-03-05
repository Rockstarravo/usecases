package com.ravindra.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


@Autowired
private UserRepo userrep;

@PostMapping(value="/insert")
public String postvalues(@RequestParam String input)
{
 
	Users users = new Users();
	users.setInput(input);
	userrep.save(users);
	
	return "values inserted with "+input;
}


@GetMapping(value="/fetch")
public Iterable<Users> getvalues()
{
	return userrep.findAll();

	
	
}

	
}
