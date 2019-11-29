import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bson.Document;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Collector {
	
	List<User> userList;
	Map<User, List<Repository>> userRepositories;
	private UserService userService;
	
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
			this.userService = new UserService(client);
			RepositoryService repoService = new RepositoryService(client);
			// Collect the users and repositories
			List<User> users = userService.getFollowing();
			users.add(userService.getUser()); 
			for (User user : users) {
				this.addUser(user);
				List<Repository> repos = repoService.getRepositories(user.getLogin());
				for (Repository repo : repos) {
					System.out.println("" + repo.getName());
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
	
	/*
	 * Populate the MongoDB database.
	 */
	private void populate() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		List<Document> userDocuments = new ArrayList<Document>();
		// Connect to the Mongo database
		MongoClientURI uri = new MongoClientURI(
			    "mongodb+srv://jackgilbride999:73hog24Ghhdq6BWg@github-ueksk.mongodb.net/test?retryWrites=true&w=majority");
			
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("Github");
		System.out.println(database.toString());
		
		// Populate the document using data from the collector
		MongoCollection<Document> collection = database.getCollection("repos");
		for(User user : this.userList) {
			Document userDocument = new Document("login", user.getLogin());
			try {
				userDocument.append("followers", this.userService.getFollowers(user.getLogin()).size());
				userDocument.append("following", this.userService.getFollowing(user.getLogin()).size());			
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<Repository> repos = this.userRepositories.get(user);
			if(repos != null) {
				List<Document> repoDocs = new ArrayList<Document>();
				for(Repository repo : repos) {
					System.out.println("" + repo.getName());
					repoDocs.add(getRepoDocument(repo));
				}
				userDocument.append("repositories", repoDocs);
			}
			userDocuments.add(userDocument);
		}
		collection.insertMany(userDocuments);
		mongoClient.close();
	}
	
	/*
	 * Return a new document representing a repository, containing its name, language,
	 * description, size, watchers, forks, creation date, update data and git URL.
	 */
	private Document getRepoDocument(Repository repo) {
		return new Document("name", repo.getName())
				.append("language", repo.getLanguage())
				.append("description", repo.getDescription())
				.append("size", repo.getSize())
				.append("watchers", repo.getWatchers())
				.append("forks", repo.getForks())
				.append("created", repo.getCreatedAt())
				.append("updated",repo.getUpdatedAt())
				.append("url", repo.getGitUrl())			
				;	
	}

	public static void main(String[] args) {
		Collector collector = new Collector();
		collector.queryGithub();
		collector.populate();
	}
}
