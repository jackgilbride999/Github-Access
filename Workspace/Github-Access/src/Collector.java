import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class Collector {
	
	List<User> userList;
	Map<User, List<Repository>> userRepositories;
	
	Collector(){
		userList = new ArrayList<User>();
		userRepositories = new HashMap<User, List<Repository>>();
	}
	
	/*
	 * 	Add a user to the user list.
	 */
	private void addUser(User user) {
		userList.add(user);
	}
	
	/*
	 * Add a user and a repository to the map from users to their repositories.
	 */
	private void addRepository(User user, Repository repo) {
		if(!userRepositories.containsKey(user)) {
			// if the user is not in the map yet, add them with a new empty list
			userRepositories.put(user, new ArrayList<Repository>());

		}
		userRepositories.get(user).add(repo);
	}
	
	/*
	 * Query the Github API and return a Collector with the relevant data.
	 */
	Collector queryGithub() {
		Scanner scanner = new Scanner(System.in);
		try {
			// Collect login credentials and authenticate
			System.out.println("Please enter your Github username");
			String username = scanner.nextLine();
			System.out.println("Welcome, " + username + "! Please enter your Github password");
			String password = scanner.nextLine();
			GitHubClient client = new GitHubClient();
			client.setCredentials(username, password);
			// Initialise Github API services
			UserService userService = new UserService(client);
			RepositoryService repoService = new RepositoryService(client);
			// Collect the users and repositories
			List<User> users = userService.getFollowing();
			for (User user : users) {
				this.addUser(user);
				List<Repository> repos = repoService.getRepositories(user.getLogin());
				for (Repository repo : repos) {
					this.addRepository(user, repo);
				}
			}
		} catch (Exception e) {
			System.out.println("Sorry, an unexpected error has occured.");
			e.printStackTrace();
		}
		scanner.close();
		return this;
	}

	public static void main(String[] args) {
		Collector collector = new Collector();
		collector.queryGithub();
		MongoClientURI uri = new MongoClientURI(
			    "mongodb+srv://jackgilbride999:73hog24Ghhdq6BWg@github-ueksk.mongodb.net/test?retryWrites=true&w=majority");
			
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("test");
		System.out.println(database.toString());
	}
}
