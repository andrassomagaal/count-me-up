package com.asg.bbc.countmeup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {

	@Autowired
	private VoteCounter voteCounter;

	/**
	 * Gives back the countMeUp result in JSON </br>
	 * Only users with ADMIN role can request this
	 */
	@RequestMapping("/CountMeUp")
	public Map<String, Integer> countMeUp(HttpServletRequest request) {
		return voteCounter.countMeUp();
	}

	/**
	 * Vote for the candidate </br>
	 * Only authenticated users can vote
	 */
	@RequestMapping(value = "/vote/{candidate}", method = RequestMethod.POST)
	public void addVote(@PathVariable String candidate, Authentication authentication) {
		voteCounter.voteIfCan(authentication.getName(), candidate);
	}
}
