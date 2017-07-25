package com.asg.bbc.countmeup;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VoteControllerTest extends TestBase {

	private static final String USER = "user";
	private static final String ADMIN = "ADMIN";
	private static final String CANDIDATE_1 = "candidate-1";
	private static final String CANDIDATE_2 = "candidate-2";
	private static final String CANDIDATE_3 = "candidate-3";
	private static final String CANDIDATE_4 = "candidate-4";
	private static final String CANDIDATE_5 = "candidate-5";

	private AtomicInteger userPostfix;

	@Before
	public void setup() {
		userPostfix = new AtomicInteger();
	}

	@Test
	public void successfulVoteTest() throws Exception {
		mockMvc.perform(post("/vote/" + CANDIDATE_1).with(httpBasic(USER, "password")));
	}

	@Test
	@WithMockUser(roles = { ADMIN })
	public void countMeUpEmptyTest() throws Exception {
		mockMvc.perform(get("/CountMeUp")).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void moreThan3VoteTest() throws Exception {
		// given
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Integer> result = new HashMap<>();
		result.put(CANDIDATE_1, 3);

		// when
		for (int i = 0; i < 5; i++) {
			mockMvc.perform(post("/vote/" + CANDIDATE_1)).andExpect(status().isOk());
		}

		// than
		mockMvc.perform(get("/CountMeUp").with(user(USER).roles(ADMIN)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().string(mapper.writeValueAsString(result)));
	}

	@Test
	@WithMockUser
	public void moreThanOneCandidateTest() throws Exception {
		// given
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Integer> result = new HashMap<>();
		result.put(CANDIDATE_1, 1);
		result.put(CANDIDATE_2, 2);

		// when
		mockMvc.perform(post("/vote/" + CANDIDATE_1)).andExpect(status().isOk());
		mockMvc.perform(post("/vote/" + CANDIDATE_2)).andExpect(status().isOk());
		mockMvc.perform(post("/vote/" + CANDIDATE_2)).andExpect(status().isOk());

		// than
		mockMvc.perform(get("/CountMeUp").with(user(USER).roles(ADMIN)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().string(mapper.writeValueAsString(result)));
	}

	@Test
	public void bbcScenarioTest() throws Exception {
		// given
		String exectedResult = createExpectedResult();

		// when
		sendVotes();

		// than
		long start = System.currentTimeMillis();
		mockMvc.perform(get("/CountMeUp").with(user(USER).roles(ADMIN)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().string(exectedResult));

		assertThat((System.currentTimeMillis() - start), lessThan(1000L));
	}

	private String createExpectedResult() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Integer> result = new HashMap<>();
		result.put(CANDIDATE_1, 500_000);
		result.put(CANDIDATE_2, 1_000_000);
		result.put(CANDIDATE_3, 2_000_000);
		result.put(CANDIDATE_4, 2_000_000);
		result.put(CANDIDATE_5, 3_000_000);
		return mapper.writeValueAsString(result);
	}

	private void sendVotes() throws Exception {
		sendEvenlyDistributedVotes(2_500_000, 1, 5);
		sendEvenlyDistributedVotes(2_000_000, 2, 5);
		sendEvenlyDistributedVotes(3_000_000, 3, 5);
		sendEvenlyDistributedVotes(1_000_000, 5, 5);

		// reset user postfix counter to use users who already vote 3
		userPostfix.set(0);

		sendEvenlyDistributedVotes(1_000_000, 4, 5);
		sendEvenlyDistributedVotes(500_000, 5, 5);
	}

	/**
	 * Send votes evenly distributed from candidatePostFixFrom to candidatePostFixTo
	 * @param voteCount The summary of votes to be send
	 * @param candidatePostFixFrom The postFix of the candidate to start from
	 * @param candidatePostFixTo The postFix of the last candidate to vote for
	 */
	private void sendEvenlyDistributedVotes(int voteCount, int candidatePostFixFrom, int candidatePostFixTo) throws Exception {
		int userPostFix = userPostfix.getAndIncrement();
		String candidate = "candidate-";
		int votePerUser = 0;
		int candidatePostFix = candidatePostFixFrom;

		for (int i = 0; i < voteCount; i++) {
			if (candidatePostFix > candidatePostFixTo) {
				candidatePostFix = candidatePostFixFrom;
			}

			if (votePerUser > 2) {
				votePerUser = 0;
				userPostFix = userPostfix.getAndIncrement();
			}

			performVoteRequest(USER + userPostFix, candidate + candidatePostFix++);
			votePerUser++;
		}
	}

	private void performVoteRequest(String user, String candidate) throws Exception {
		mockMvc.perform(post("/vote/" + candidate).with(user(user))).andExpect(status().isOk());
	}

}
