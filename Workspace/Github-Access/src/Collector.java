import java.util.Scanner;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

public class Collector {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
			GitHubClient client = new GitHubClient();
			System.out.println("Please enter your Github username");
			String username = scanner.nextLine();
			System.out.println("Welcome, " + username + "! Please enter your Github password");
			String password = scanner.nextLine();
			client.setCredentials(username, password);
			RepositoryService repoService = new RepositoryService();
			UserService userService = new UserService(client);
			while (true) {
				System.out.println(
						"Please enter a username to view information about a user and who they are following, or type 'quit' to exit.");
				username = scanner.nextLine();
				if (username.equalsIgnoreCase("quit"))
					return;
				for (User user : userService.getFollowing()) {
					System.out.println(user.getLogin());
					System.out.println("	Follower count:" + user.getFollowers());
					System.out.println("	Following count:" + user.getFollowers());
					for (Repository repo : repoService.getRepositories(user.getLogin())) {
						System.out.println("	Repository name: " + repo.getName());
						System.out.println("		- Language: " + repo.getLanguage());
						System.out.println("		- Description: " + repo.getDescription());
						System.out.println("		- Size: " + repo.getSize() + " kB");
						System.out.println("		- Number of watchers: " + repo.getWatchers());
						System.out.println("		- Number of forks: " + repo.getForks());
						System.out.println("		- Created at: " + repo.getCreatedAt());
						System.out.println("		- Updated at: " + repo.getUpdatedAt());
						System.out.println("		- URL: " + repo.getGitUrl());
						System.out.println("		- Commit comments: ");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Sorry, an unexpected error has occured.");
			e.printStackTrace();
		}
		scanner.close();
	}
}
