package com.asg.bbc.countmeup;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterController {
	
	@RequestMapping("/CountMeUp")
	public Map<String, Integer> countMeUp(){
		HashMap<String, Integer> map = new HashMap<>();
		map.put("Candidate-1", 200);
		map.put("Candidate-2", 300);
		map.put("Candidate-3", 400);
		return map;
	}
}
