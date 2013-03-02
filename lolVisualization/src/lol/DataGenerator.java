package lol;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.utility.Iterate;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class DataGenerator {

	public static void main(String[] args) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
        	String hashtag = "#leagueoflegends Darius";
            Query query = new Query("#leagueoflegends Darius");
            List<Status> totalTweets = new ArrayList<Status>();
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                totalTweets.addAll(tweets);
                System.out.println("result count- "+tweets.size());
                for (Status tweet : tweets) {
					System.out.println("@" +tweet.getCreatedAt()+ " - " + tweet.getUser().getScreenName() + " - " + tweet.getText());
                }
            } while ((query = result.nextQuery()) != null);
            
           System.out.println("TOTAL COUNT for " +hashtag+ ": "+totalTweets.size());
//            Map<String, List<Status>> resultByDate = new HashMap<String, List<Status>>();
//            Iterate.addToMap(totalTweets,new Function<Status, String>(){
//				public String valueOf(Status tweet) {
//					Calendar calendar = Calendar.getInstance();
//					calendar.setTime(tweet.getCreatedAt());
//					String format = calendar.get(Calendar.MONTH) + 1 + "/" +calendar.get(Calendar.DATE)+ "/" + calendar.get(Calendar.YEAR) ;
//					return format;
//				}} , resultByDate);
//            
//            System.out.println("-------------TOTAL COUNT for " +hashtag+ "---------");
//            for(String date: resultByDate.keySet())
//            {
//            	System.out.println("KEY: "+date+" COUNT : " +resultByDate.get(date).size());
//            }
            
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }

}
