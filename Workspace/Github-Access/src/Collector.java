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
			System.out.println("Please enter your Github password");
			String password = scanner.nextLine();
			client.setCredentials(username, password);
			RepositoryService service = new RepositoryService();
			while (username != "quit") {
				System.out.println("Please enter a username to view their repos");
				username = scanner.nextLine();
				for (Repository repo : service.getRepositories("defunkt"))
					System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());

			}
		} catch (Exception e) {
			System.out.println("Error.");
		}

		scanner.close();
	}
}
