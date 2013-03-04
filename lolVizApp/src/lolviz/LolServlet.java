package lolviz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Servlet implementation class ShoutoutServlet
 */
public class LolServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LolServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out.println("GET");
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("index.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST");
		RequestDispatcher dispatcher = request.getRequestDispatcher("failure.jsp");
		String championNames = request.getParameter("champions");
		if (null != championNames && !championNames.isEmpty()) {
			String[] champions = championNames.split(",");
			
			Map<String, Map<String, List<Status>>> tweetsByChampion = getTwitterData( champions, request);
			request.setAttribute("TWEETS_BY_CHAMPION", tweetsByChampion);
			dispatcher = request.getRequestDispatcher("success.jsp");
		}
		dispatcher.forward(request, response);
	}

	private static Map<String, Map<String, List<Status>>> getTwitterData(String[] champions, HttpServletRequest request) {
		Map<String, Map<String, List<Status>>> tweetsByChampion = new HashMap<String, Map<String, List<Status>>>();
		
		//ConfigurationBuilder cb = new ConfigurationBuilder();
		//cb.setDebugEnabled(false).setOAuthConsumerKey(oauthConsumerKey).setOAuthConsumerSecret(oauthConsumerSecret).setOAuthAccessToken(oauthAccessToken).setOAuthAccessTokenSecret(oauthAccessTokenSecret);
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			//Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");

				
	
			String hashtag = "#leagueoflegends";
			// List<String> champions = Arrays.asList("Amumu", "Cho Gath",
			// "Darius", "Draven", "Garen", "Jarvan",
			// "Jayce", "Karthus", "Katarina", "Lee Sin", "Lux", "Nidalee",
			// "Olaf", "Poppy", "Rengar", "Varus", "Wukong", "Xin");
			for (String champion : champions) {

				System.out.println("Fetching tweets for " + champion);
				String queryString = hashtag + " " + champion;
				Query query = new Query(queryString);
				List<Status> totalTweets = getTweetsForAChampion(twitter, queryString, query);
				if (!totalTweets.isEmpty()) {

					Map<String, List<Status>> resultByDate = aggregateResultsByDate(totalTweets);

					System.out.println("-------------DATA for " + champion
							+ "---------");
					for (String date : resultByDate.keySet()) {
						System.out.println("KEY: " + date + " COUNT : "
								+ resultByDate.get(date).size());
					}
					tweetsByChampion.put(champion, resultByDate);
				}

			}

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
		}
		return tweetsByChampion;
	}

	private static List<Status> getTweetsForAChampion(Twitter twitter,
			String queryString, Query query) throws TwitterException {
		List<Status> totalTweets = new ArrayList<Status>();
		QueryResult result;
		do {
			result = twitter.search(query);
			List<Status> tweets = result.getTweets();
			totalTweets.addAll(tweets);
			System.out.println("Each query count- " + tweets.size());
			// for (Status tweet : tweets) {
			// System.out.println("@" + tweet.getCreatedAt() + " - "
			// + tweet.getUser().getScreenName() + " - " + tweet.getText());
			// }
		} while ((query = result.nextQuery()) != null);

		System.out.println("TOTAL COUNT for " + queryString + ": "
				+ totalTweets.size());
		return totalTweets;
	}

	private static Map<String, List<Status>> aggregateResultsByDate(
			List<Status> totalTweets) {
		Map<String, List<Status>> resultsByDate = new HashMap<String, List<Status>>();
		for (Status tweet : totalTweets) {
			String dateKey = getFormattedDate(tweet.getCreatedAt());
			List<Status> tweetsPerDate = resultsByDate.get(dateKey);
			if (tweetsPerDate == null || tweetsPerDate.isEmpty()) {
				List<Status> tweets = new ArrayList<Status>();
				tweets.add(tweet);
				resultsByDate.put(dateKey, tweets);
			} else {
				tweetsPerDate.add(tweet);
			}

		}
		return resultsByDate;

	}

	private static String getFormattedDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String format = calendar.get(Calendar.MONTH) + 1 + "/"
				+ calendar.get(Calendar.DATE) + "/"
				+ calendar.get(Calendar.YEAR);
		return format;
	}

}
