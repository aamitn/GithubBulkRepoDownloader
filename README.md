# Github Bulk Repository Downloader (GBRD)

A Java-based desktop application that allows users to download and fork GitHub repositories. This application uses the GitHub API for repository management, OkHttpClient for API requests, and JGit for handling repository cloning.

## Features

- Download GitHub repositories by organization or user.
- Fork repositories to a target GitHub account.
- Handle API pagination to fetch more than 30 repositories.
- Progress bar to indicate downloading or forking progress.
- Error handling and success status updates.
- Customizable UI built using Java Swing.
- Supports both `.exe` and `.jar` release formats.

## Installation

### Prerequisites

- Java 8 or above installed on your machine.
- A GitHub token with appropriate permissions (e.g., `repo` scope for private repositories).

### Download

- **Windows Users**: Download the `.exe` release from the [Releases](#releases) section.
- **Java Users**: Download the `.jar` release from the [Releases](#releases) section.

### Running the Application

#### On Windows (using `.exe`):

1. Download the `.exe` file from the [Releases](#releases) section.
2. Double-click the `.exe` to run the application.

#### On any OS with Java (using `.jar`):

1. Ensure you have Java installed on your machine.
2. Download the `.jar` file from the [Releases](#releases) section.
3. Run the application using the following command:

   ```bash
   java -jar GitHubRepoDownloader.jar

Usage
-----

### GitHub Token

1.  Generate a GitHub personal access token from [GitHub Settings](https://github.com/settings/tokens).
2.  Copy the token for use within the application.

### Forking Repositories

1.  Enter the organization or username in the input field.
2.  Input the target username (the user to whom the repositories should be forked).
3.  Provide your GitHub token.
4.  Choose whether you want to fork repositories from a user or organization.
5.  Click the "Start Forking" button.
6.  The progress bar will show the forking progress, and the status will update upon success or failure.

### Downloading Repositories

1.  Enter the organization or username in the input field.
2.  Provide your GitHub token.
3.  Click the "Download" button to download the repositories to a local directory.
4.  Monitor the progress via the progress bar.

Build
-----

1.  Clone the repository:
     ```bash
    git clone https://github.com/your-repo/GitHubRepoDownloader.git
    cd GitHubRepoDownloader`

   2.  Build using Maven or your preferred build tool.
       bash
       ```bash
        mvn -X clean package
3. Get the jars and exe from `target` directory after build.

Technologies Used
-----------------

-   **Java**
-   **Swing** for the graphical user interface.
-   **OkHttp** for making HTTP requests.
-   **JGit** for handling Git repository cloning.
-   **GitHub API** for interacting with repositories.

Releases
--------

You can download the latest releases here:

-   [Download .exe](https://github.com/your-repo/releases/download/v1.0/GitHubRepoDownloader.exe)
-   [Download .jar](https://github.com/your-repo/releases/download/v1.0/GitHubRepoDownloader.jar)

License
-------

This project is licensed under the MIT License. See the LICENSE file for details.

Contributing
------------

Feel free to contribute to the project by submitting pull requests or reporting issues.