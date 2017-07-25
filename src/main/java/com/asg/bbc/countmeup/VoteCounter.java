package com.asg.bbc.countmeup;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * This component is to record the votes for each candidate </br>
 * It is also record the vote count for each user to prevent more than 3 notes
 * per user
 */
@Component
public class VoteCounter {

	private static final int MAX_VOTE_PER_USER = 3;

	private Map<String, Integer> overallVoteCounter = new ConcurrentHashMap<>();

	private Map<String, Integer> voteCounterByUser = new ConcurrentHashMap<>();

	public Map<String, Integer> countMeUp() {
		return unmodifiableMap(overallVoteCounter);
	}

	/**
	 * The user vote for the candidate if it has not voted more than 3 already
	 * @param user the user who request the vote
	 * @param candidate the candidate to vote for
	 */
	public void voteIfCan(String user, String candidate) {
		if (voteCounterByUser.containsKey(user)) {
			if (voteCounterByUser.get(user) < MAX_VOTE_PER_USER) {
				voteCounterByUser.put(user, voteCounterByUser.get(user) + 1);
				overallVoteCounter.put(candidate,
						overallVoteCounter.containsKey(candidate) ? overallVoteCounter.get(candidate) + 1 : 1);
			}
		} else {
			voteCounterByUser.put(user, 1);
			overallVoteCounter.put(candidate,
					overallVoteCounter.containsKey(candidate) ? overallVoteCounter.get(candidate) + 1 : 1);
		}
	}

}
