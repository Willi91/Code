import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.auth.RequestToken;
import com.sun.net.ssl.HttpsURLConnection;
/**
 * Class to extract tweets  of a specified user using Twitter api 
 * and display the user_name , no. of followers and the url to the profile
 * @author Willison
 *
 */
public class Twitterinfo {
	/**
	 * Main class to perform the required funcitons
	 * @param arg
	 */
	public static void main(String arg[]) {
		String user_key = new String();		//Store the key of the user
		IDs user_id;		// user id of the user given
		String user = "cablelabs";	//User name
		String token = new String();	
		String user_secret = new String();
		String HostUrl = new String();	
		Tweet tweet;	//Tweets that will be extracted
		Twitter twitt = new TwitterFactory().getInstance();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the user key:");
		user_key = sc.next();
		System.out.println("Enter the user secret:");
		user_secret = sc.next();
		System.out.println("Enter the host url");
		HostUrl = sc.next();
		//Token associated with the user_name
		token = gettoken(HostUrl, user_key, user_secret);	
		//Authenticate the user
		twitter.setOAuthConsumer(user_key,user_secret);	
		AccessToken access_token = new AccessToken("bearer",token);
		twitter.setOAuthAccessToken(access_token);
		Query tosearch = new Query(user);
		//set limit to the number of tweets that can be extracted
		tosearch.setRpp(300);
		try{
		QueryResult tweetsfound;		//tweets found on the timeline
		//search for tweets where the username is mentioned
		tweetsfound = twitter.search(tosearch);
		int No_of_followers = 0;
		//Store the tweets in a list
		List <Status> alltweets = tweetsfound.getTweets();
		for(Status twt: tweets){
			if(count == 5){	//get the specified information for the 5th tweet
				String name = tweet.getUser().getName();
				System.out.println(tweet.getId() +"Sceen name: " +name );
				user_id = getfollowers(user);
				System.out.println("Number of followers:" +user_id);
				URL user_url = name.getProfileImageURL();
				System.out.println("Users url" +user_url);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * Method to get the list of followers of the user
	 * @param user
	 * @return
	 */
	public static int getfollowers(String user){
		IDs followerid = 0;	//id of the followers
		int nextfollower = 0;	//pointer to the next follower
		int countfollowers = 0;	//number of followers
		do{	
			//get the list of follower ids for the user of the 5th tweet
			followerid = twitter.getFollowersIDs(user.getId(), nextfollower);
			for(long friendids : followerid.getIDs()){
				countfollowers++;  
			}
			//cursor points to the next follower id
			nextfollower = followerid.getNextCursor();
		}while(nextfollower != 0);
		return countfollowers;	//return the no of followers
	}
	/**
	 * Get token for the specified user
	 * 
	 * @param HostUrl
	 * @param user_key
	 * @param user_secret
	 * @return
	 */
	public static String gettoken(String HostUrl,String user_key, String user_secret){
		HttpsURLConnection connection = null;
		String token = new String();
		String token_type = new String();
		try {
			URL url = new URL(HostUrl);	//url to the webpage
			connection = (HttpsURLConnection)url.openConnection();	//Connect to the server
			connection.setRequestProperty("Host","api.twitter.com");
			//set authorization to the server
			connection.setRequestProperty("Authorization:"+user_key +" " +user_secret);
			writeRequest(connection, "grant=user_credentials");
			//The request returns an object with tokens in the object
			JSONObject jobj = (JSONObject)JSONValue.parse(readResponse(connection)));
			if(jobj != null){
				token = (String)jobj.get("token");
				token_type = (String)jobj.get("token_type");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
		

	}
}
