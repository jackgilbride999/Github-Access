# Github Accecss
*"Interrogate the **GitHub API** to retrieve and display data regarding the logged in developer."*

## Implementation
### Development environment
The project was written in Java within the Eclipse IDE. The gitignore for the project is an Eclipse gitignore to simplify the file structure and stop the project from being IDE dependent. As a result the project only contains the relevant source code and executable.

### Libraries used
The project uses the [GitHub Java API (org.eclipse.egit.github.core)](https://github.com/eclipse/egit-github/tree/master/org.eclipse.egit.github.core) which is part of the [Github Mylyn Connector](https://github.com/eclipse/egit-github) and aims to support the entire Github v3 API. This library uses the [Google GSON library](https://github.com/google/gson) to serialize and deserialize its information, so Google GSON library is also a dependency. Both files are in the 'lib' folder and referenced by the project.

### Code and Approach
For this stage of the project it was neccessary just to retrieve and display data regarding the logged in developer. The program interacts with the user through System.in and System.out. It asks the user for their username and password. These credentials only exist for the lifetime of the program and are not stored. The user may then enter the name of the user whose repository information they would like to see. For example, a snippet of the output for my name would be as follows:
```
(...)
Repository name: VHDL-Processor
        - Language: VHDL
        - Description: Implementation of a 16-bit, 8 register processor in VHDL.
        - Size: 1740 kB
        - Number of watchers: 0
        - Number of forks: 0
        - Created at: Fri Mar 15 17:17:18 GMT 2019
        - Updated at: Fri Apr 12 13:52:05 BST 2019
        - URL: git://github.com/jackgilbride999/VHDL-Processor.git
Repository name: Yelp-Visualization-Project
        - Language: Java
        - Description: Project to construct an application to explore data on customer reviews of businesses using the processing library for Java.
        - Size: 18149 kB
        - Number of watchers: 2
        - Number of forks: 0
        - Created at: Mon Nov 11 21:30:23 GMT 2019
        - Updated at: Mon Nov 18 12:27:52 GMT 2019
        - URL: git://github.com/jackgilbride999/Yelp-Visualization-Project.git
(...)
```

## Usage
For ease of use the project, including dependencies, is compiled into an *executable JAR file*. The JAR file is compiled for Java 8 or later, so you must have that installed on your machine. To execute the program, navigate to `/Workspace`, then enter `java -j Github-Access.jar` on the command line. The program should then run, connected to standard input and standard output. Once you have entered your credentials, you may enter `quit` to exit the program.