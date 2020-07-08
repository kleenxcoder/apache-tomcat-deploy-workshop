package com.kleenxcoder.workshop.apache.tomcat.deploy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kleenxcoder")
public class KleenXCoderController {
	
	@GetMapping("/")
	public String getAllDatabases(){
		return "I love it when a plan comes together.";
	}

}
