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
	
	private void addUser(User user) {
		userList.add(user);
	}
	
	private void addRepository(User user, Repository repo) {
		if(userRepositories.containsKey(user)) {
			(userRepositories.get(user)).add(repo);
		} else {
			userRepositories.put(user, new ArrayList<Repository>());
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		MongoClientURI uri = new MongoClientURI(
			    "mongodb+srv://jackgilbride999:73hog24Ghhdq6BWg@github-ueksk.mongodb.net/test?retryWrites=true&w=majority");
			
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("test");
		System.out.println(database.toString());
		try {
			GitHubClient client = new GitHubClient();
			System.out.println("Welcome to Github Access. This program interrogates the Github API for data about those you are following.");
			System.out.println("Please enter your Github username");
			String username = scanner.nextLine();
			System.out.println("Welcome, " + username + "! Please enter your Github password");
			String password = scanner.nextLine();
			client.setCredentials(username, password);
			RepositoryService repoService = new RepositoryService(client);
			UserService userService = new UserService(client);
			List<User> users = userService.getFollowing();
			for (User user : users) {
				System.out.println(user.getLogin());
				System.out.println("	Follower count:" + user.getFollowers());
				System.out.println("	Following count:" + user.getFollowers());
				List<Repository> repos = repoService.getRepositories(user.getLogin());
				for (Repository repo : repos) {
					System.out.println("	Repository name: " + repo.getName());
					System.out.println("		- Language: " + repo.getLanguage());
					System.out.println("		- Description: " + repo.getDescription());
					System.out.println("		- Size: " + repo.getSize() + " kB");
					System.out.println("		- Number of watchers: " + repo.getWatchers());
					System.out.println("		- Number of forks: " + repo.getForks());
					System.out.println("		- Created at: " + repo.getCreatedAt());
					System.out.println("		- Updated at: " + repo.getUpdatedAt());
					System.out.println("		- URL: " + repo.getGitUrl());
				}
			}
		} catch (Exception e) {
			System.out.println("Sorry, an unexpected error has occured.");
			e.printStackTrace();
		}
		scanner.close();
	}
}
