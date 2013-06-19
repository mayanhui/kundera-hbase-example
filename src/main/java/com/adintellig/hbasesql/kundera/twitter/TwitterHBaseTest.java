/*******************************************************************************
 * * Copyright 2012 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.adintellig.hbasesql.kundera.twitter;

import java.util.List;

import com.adintellig.hbasesql.kundera.twitter.dao.TwitterHbase;
import com.adintellig.hbasesql.kundera.twitter.dao.TwitterServiceHbase;
import com.adintellig.hbasesql.kundera.twitter.entities.ExternalLinkHBase;
import com.adintellig.hbasesql.kundera.twitter.entities.PersonalDetailHbase;
import com.adintellig.hbasesql.kundera.twitter.entities.PreferenceHBase;
import com.adintellig.hbasesql.kundera.twitter.entities.TweetHbase;
import com.adintellig.hbasesql.kundera.twitter.entities.UserHBase;


public class TwitterHBaseTest {
	public static final boolean RUN_IN_EMBEDDED_MODE = true;

	public static final boolean AUTO_MANAGE_SCHEMA = true;

	/** The user id1. */
	String userId1;

	/** The user id2. */
	String userId2;

	/** The twitter. */
	protected TwitterHbase twitter;

	private String persistenceUnitName;

	/**
	 * Sets the up internal.
	 * 
	 * @param persistenceUnitName
	 *            the new up internal
	 * @throws Exception
	 *             the exception
	 */
	protected void setUpInternal(String persistenceUnitName) throws Exception {
		this.persistenceUnitName = persistenceUnitName;
		userId1 = "0001";
		userId2 = "0002";

		twitter = new TwitterServiceHbase(persistenceUnitName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	/**
	 * Tear down internal.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	protected void tearDownInternal() throws Exception {
		if (twitter != null) {
			twitter.close();
		}

	}

	/**
	 * Execute suite.
	 */
	protected void executeTestSuite() {
		// Insert, Find and Update
		addAllUserInfo();
		getUserById();
		updateUser();

		// Queries
		getAllUsers();
		getAllTweets();

		// Remove Users
		removeUser();

	}

	protected void addAllUserInfo() {
		UserHBase user1 = buildUser1();
		UserHBase user2 = buildUser2();

		twitter.createEntityManager();
		twitter.addUser(user1);
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.addUser(user2);
		twitter.closeEntityManager();
	}

	protected void getUserById() {
		twitter.createEntityManager();
		UserHBase user1 = twitter.findUserById(userId1);

		UserHBase user2 = twitter.findUserById(userId2);

	}

	protected void updateUser() {
		twitter.createEntityManager();
		UserHBase user1 = twitter.findUserById(userId1);

		user1.setPersonalDetail(new PersonalDetailHbase("Vivek", "unknown",
				"Married"));
		user1.addTweet(new TweetHbase("My Third Tweet", "iPhone"));
		twitter.mergeUser(user1);

		UserHBase user1AfterMerge = twitter.findUserById(userId1);

		twitter.closeEntityManager();
	}

	protected void removeUser() {
		twitter.createEntityManager();
		UserHBase user1 = twitter.findUserById(userId1);

		twitter.removeUser(user1);

		UserHBase user1AfterRemoval = twitter.findUserById(userId1);

		twitter.closeEntityManager();

	}

	protected void getAllUsers() {
		twitter.createEntityManager();
		List<UserHBase> users = twitter.getAllUsers();

		for (UserHBase u : users) {
			if (u.getUserId().equals(userId1)) {
			} else if (u.getUserId().equals(userId2)) {
			}
		}
		twitter.closeEntityManager();
	}

	/**
	 * Adds the users.
	 */
	protected void addUsers() {
		twitter.createEntityManager();
		twitter.addUser(userId1, "Amresh", "password1", "married");
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.addUser(userId2, "Saurabh", "password2", "single");
		twitter.closeEntityManager();
	}

	/**
	 * Save preference.
	 */
	protected void savePreference() {
		twitter.createEntityManager();
		twitter.savePreference(userId1, new PreferenceHBase("P1", "Motif", "2"));
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.savePreference(userId2, new PreferenceHBase("P2",
				"High Contrast", "3"));
		twitter.closeEntityManager();
	}

	/**
	 * Adds the external links.
	 */
	protected void addExternalLinks() {
		twitter.createEntityManager();
		twitter.addExternalLink(userId1, "L1", "Facebook",
				"http://facebook.com/coolnerd");
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.addExternalLink(userId1, "L2", "LinkedIn",
				"http://linkedin.com/in/devilmate");
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.addExternalLink(userId2, "L3", "GooglePlus",
				"http://plus.google.com/inviteme");
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.addExternalLink(userId2, "L4", "Yahoo",
				"http://yahoo.com/profiles/itsmeamry");
		twitter.closeEntityManager();
	}

	/**
	 * Adds the tweets.
	 */
	protected void addTweets() {
		twitter.createEntityManager();
		twitter.addTweet(userId1, "Here is my first tweet", "Web");
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.addTweet(userId1, "Second Tweet from me", "Mobile");
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.addTweet(userId2, "Saurabh tweets for the first time", "Phone");
		twitter.closeEntityManager();

		twitter.createEntityManager();
		twitter.addTweet(userId2, "Another tweet from Saurabh", "text");
		twitter.closeEntityManager();
	}

	/**
	 * User1 follows user2.
	 */
	protected void user1FollowsUser2() {
		twitter.createEntityManager();
		twitter.startFollowing(userId1, userId2);
		twitter.closeEntityManager();
	}

	/**
	 * User1 adds user2 as follower.
	 */
	protected void user1AddsUser2AsFollower() {
		twitter.createEntityManager();
		twitter.addFollower(userId1, userId2);
		twitter.closeEntityManager();
	}

	/**
	 * Gets the all tweets.
	 * 
	 * @return the all tweets
	 */
	protected void getAllTweets() {
		twitter.createEntityManager();

		List<TweetHbase> tweetsUser1 = twitter.getAllTweets(userId1);
		List<TweetHbase> tweetsUser2 = twitter.getAllTweets(userId2);

		twitter.closeEntityManager();

		// assertNotNull(tweetsUser1);
		// assertNotNull(tweetsUser2);
		//
		// assertFalse(tweetsUser1.isEmpty());
		// assertFalse(tweetsUser2.isEmpty());
		//
		// assertEquals(3, tweetsUser1.size());
		// assertEquals(2, tweetsUser2.size());
	}

	/**
	 * Gets the tweets by body.
	 * 
	 * @return the tweets by body
	 */
	public void getTweetsByBody() {
		twitter.createEntityManager();
		List<TweetHbase> user1Tweet = twitter.findTweetByBody("Here");
		List<TweetHbase> user2Tweet = twitter.findTweetByBody("Saurabh");

		twitter.closeEntityManager();

		// assertNotNull(user1Tweet);
		// assertNotNull(user2Tweet);
		// assertEquals(1, user1Tweet.size());
		// assertEquals(1, user2Tweet.size());
	}

	/**
	 * Gets the tweet by device.
	 * 
	 * @return the tweet by device
	 */
	public void getTweetsByDevice() {
		twitter.createEntityManager();
		List<TweetHbase> webTweets = twitter.findTweetByDevice("Web");
		List<TweetHbase> mobileTweets = twitter.findTweetByDevice("Mobile");

		twitter.closeEntityManager();

		// assertNotNull(webTweets);
		// assertNotNull(mobileTweets);
		// assertEquals(1, webTweets.size());
		// assertEquals(1, mobileTweets.size());

	}

	/**
	 * Gets the all followers.
	 * 
	 * @return the all followers
	 */
	protected void getAllFollowers() {
		twitter.createEntityManager();
		List<UserHBase> follower1 = twitter.getFollowers(userId1);
		twitter.closeEntityManager();

		twitter.createEntityManager();
		List<UserHBase> follower2 = twitter.getFollowers(userId2);
		twitter.closeEntityManager();

		// assertNull(follower1);
		// assertNotNull(follower2);
	}

	/**
	 * @return
	 */
	private UserHBase buildUser1() {
		UserHBase user1 = new UserHBase(userId1, "Amresh", "password1",
				"married");

		user1.setPreference(new PreferenceHBase("P1", "Motif", "2"));

		user1.addExternalLink(new ExternalLinkHBase("L1", "Facebook",
				"http://facebook.com/coolnerd"));
		user1.addExternalLink(new ExternalLinkHBase("L2", "LinkedIn",
				"http://linkedin.com/in/devilmate"));

		user1.addTweet(new TweetHbase("Here is my first tweet", "Web"));
		user1.addTweet(new TweetHbase("Second Tweet from me", "Mobile"));
		return user1;
	}

	/**
	 * @return
	 */
	private UserHBase buildUser2() {
		UserHBase user2 = new UserHBase(userId2, "Saurabh", "password2",
				"single");

		user2.setPreference(new PreferenceHBase("P2", "High Contrast", "3"));

		user2.addExternalLink(new ExternalLinkHBase("L3", "GooglePlus",
				"http://plus.google.com/inviteme"));
		user2.addExternalLink(new ExternalLinkHBase("L4", "Yahoo",
				"http://yahoo.com/profiles/itsmeamry"));

		user2.addTweet(new TweetHbase("Saurabh tweets for the first time",
				"Phone"));
		user2.addTweet(new TweetHbase("Another tweet from Saurabh", "text"));
		return user2;
	}

	public static void main(String[] args) throws Exception {
		String userId1 = "0001";
		String userId2 = "0002";

		System.out.println("1....");
		TwitterServiceHbase twitter = new TwitterServiceHbase("twibaseTest");
		twitter.createEntityManager();
		System.out.println("2....");

		TwitterHBaseTest test = new TwitterHBaseTest();
		test.setUpInternal("twibaseTest");
		test.addAllUserInfo();
		test.addTweets();
		test.addUsers();
		test.addExternalLinks();

		System.out.println("2....");

		List<TweetHbase> tweetsUser1 = twitter.getAllTweets(userId1);
		List<TweetHbase> tweetsUser2 = twitter.getAllTweets(userId2);

		System.out.println(tweetsUser1.size());
		System.out.println(tweetsUser2.size());

		twitter.closeEntityManager();
	}
}
