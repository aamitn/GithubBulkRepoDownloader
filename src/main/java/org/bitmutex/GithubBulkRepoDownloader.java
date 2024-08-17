package org.bitmutex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.eclipse.jgit.api.Git;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GithubBulkRepoDownloader extends JFrame {
    private JTextField orgNameField;
    private JRadioButton userRadioButton;
    private JRadioButton orgRadioButton;
    private JTextField targetUserField;
    private JPasswordField tokenField;
    private JButton chooseLocationButton;
    private JButton downloadButton;
    private JButton forkButton;
    private JLabel downloadSpeedLabel;
    private JLabel totalReposLabel;
    private JLabel statusLabel;
    private File downloadLocation;
    private JCheckBox showTokenCheckBox;
    private JCheckBox enableForkCheckBox;
    private JProgressBar progressBar;
    private JTextField locationField;
    private final String accessToken = "YOUR_TOKEN_HERE"; // Replace this with your access token https://github.com/settings/tokens/new

    public GithubBulkRepoDownloader() {
        setTitle("GBRD v0.2");
        setSize(660, 600);  // Adjusted size for better fit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem createGHPATMenuItem = new JMenuItem("Create GHPAT");
        createGHPATMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new java.net.URI("https://github.com/settings/tokens/new"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(createGHPATMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        // Add a heading
        JLabel headingLabel = new JLabel("<html><p>GitHub Bulk Repo Downloader <sup>(GBRDv0.2)</sup><h4>&copy; 2024  | Developed by Bitmutex Technologies</h4></p></html>", JLabel.CENTER);
        headingLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(headingLabel, BorderLayout.NORTH);

        // Create a panel for user input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Organization/User Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Source GitHub Organization/User Name:"), gbc);
        orgNameField = new JTextField(30);
        gbc.gridx = 1;
        inputPanel.add(orgNameField, gbc);

        // Account Type Radio Buttons
        JPanel accountTypePanel = new JPanel();
        accountTypePanel.setBorder(new TitledBorder("Account Type"));
        accountTypePanel.setLayout(new GridLayout(2, 1));
        userRadioButton = new JRadioButton("User Account");
        orgRadioButton = new JRadioButton("Organization Account");
        ButtonGroup accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(userRadioButton);
        accountTypeGroup.add(orgRadioButton);
        orgRadioButton.setSelected(true);
        accountTypePanel.add(userRadioButton);
        accountTypePanel.add(orgRadioButton);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(accountTypePanel, gbc);

        // Initialize the location text field
        locationField = new JTextField(35);
        locationField.setEditable(false); // Make it non-editable

        // Add a label and the text field to the panel
        gbc.gridx = 1;
        gbc.gridy = 6; // Adjust this as needed
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Download Location:"), gbc);
        gbc.gridy = 6;
        inputPanel.add(locationField, gbc);

        // Destination GitHub User for Forking
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Destination GitHub User for Forking:"), gbc);
        targetUserField = new JTextField(30);
        gbc.gridx = 1;
        inputPanel.add(targetUserField, gbc);

        // Destination GitHub Personal Access Token
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Destination GitHub Personal Access Token:"), gbc);
        tokenField = new JPasswordField(30);
        gbc.gridx = 1;
        inputPanel.add(tokenField, gbc);

        // Show Token Checkbox
        showTokenCheckBox = new JCheckBox("Show Token");
        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(showTokenCheckBox, gbc);

        // Enable Fork Feature Checkbox
        enableForkCheckBox = new JCheckBox("Enable Fork Feature");
        gbc.gridx = 1;
        gbc.gridy = 5;
        inputPanel.add(enableForkCheckBox, gbc);

        // Buttons
        chooseLocationButton = new JButton("Choose Download Location");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        inputPanel.add(chooseLocationButton, gbc);

        downloadButton = new JButton("Download All Repos");
        forkButton = new JButton("Fork All Repos");
        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(downloadButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(forkButton, gbc);

        // Status and Progress
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(3, 1));
        downloadSpeedLabel = new JLabel("Download Speed: ");
        totalReposLabel = new JLabel("Total Repos Found: ");
        statusLabel = new JLabel("Status: ");
        statusPanel.add(downloadSpeedLabel);
        statusPanel.add(totalReposLabel);
        statusPanel.add(statusLabel);

        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setStringPainted(true);
        statusPanel.add(progressBar);

        // Add panels to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);




        // Initialize buttons
        downloadButton.setEnabled(false);
        forkButton.setEnabled(false);

        // Action Listeners
        chooseLocationButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                downloadLocation = fileChooser.getSelectedFile();
                locationField.setText(downloadLocation.getAbsolutePath()); // Update the text field with the chosen location
                downloadButton.setEnabled(true);
                forkButton.setEnabled(enableForkCheckBox.isSelected());
            }
        });

        downloadButton.addActionListener(e -> {
            String orgName = orgNameField.getText();
            if (orgName.isEmpty() || downloadLocation == null) {
                JOptionPane.showMessageDialog(null, "Please enter organization/user name and choose download location.");
                return;
            }
            startDownloading(orgName);
        });

        forkButton.addActionListener(e -> {
            String orgName = orgNameField.getText();
            String targetUser = targetUserField.getText();
            String token = new String(tokenField.getPassword());

            if (orgName.isEmpty() || targetUser.isEmpty() || token.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter organization/user name, target user, and access token.");
                return;
            }

            startForking(orgName, targetUser, token);
        });

        showTokenCheckBox.addActionListener(e -> {
            if (showTokenCheckBox.isSelected()) {
                tokenField.setEchoChar((char) 0); // Show text
            } else {
                tokenField.setEchoChar('*'); // Hide text
            }
        });

        enableForkCheckBox.addActionListener(e -> {
            boolean isEnabled = enableForkCheckBox.isSelected();
            targetUserField.setEnabled(isEnabled);
            tokenField.setEnabled(isEnabled);
            forkButton.setEnabled(isEnabled);
        });

    }
    private void startDownloading(String orgName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            OkHttpClient client = new OkHttpClient();
            List<String> repoUrls = new ArrayList<>();
            String url = orgRadioButton.isSelected()
                    ? "https://api.github.com/orgs/" + orgName + "/repos?per_page=100"
                    : "https://api.github.com/users/" + orgName + "/repos?per_page=100";

            while (url != null) {
                Request request = buildGitHubRequest(url, accessToken);

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        String errorMsg = "Failed to fetch repos.";
                        if (response.code() == 403) {
                            errorMsg += " Rate limit exceeded.";
                        }
                        final String finalErrorMsg = errorMsg;
                        SwingUtilities.invokeLater(() -> statusLabel.setText(finalErrorMsg));
                        return;
                    }

                    if (response.body() == null) {
                        SwingUtilities.invokeLater(() -> statusLabel.setText("Error: No response body."));
                        return;
                    }

                    String json = response.body().string();
                    List<String> pageRepoUrls = parseRepoUrls(json);
                    repoUrls.addAll(pageRepoUrls);

                    // Check for the next page
                    String nextPageUrl = response.header("Link");
                    url = getNextPageUrl(nextPageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Error occurred during fetch."));
                    return;
                }
            }

            SwingUtilities.invokeLater(() -> {
                totalReposLabel.setText("Total Repos Found: " + repoUrls.size());
                downloadRepos(repoUrls);
            });
        });
    }

    private String getNextPageUrl(String linkHeader) {
        if (linkHeader == null) return null;
        String[] links = linkHeader.split(",");
        for (String link : links) {
            if (link.contains("rel=\"next\"")) {
                return link.substring(link.indexOf("<") + 1, link.indexOf(">"));
            }
        }
        return null;
    }
    private void startForking(String orgName, String targetUser, String token) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            OkHttpClient client = new OkHttpClient();
            List<String> repoUrls = new ArrayList<>();
            String url = orgRadioButton.isSelected()
                    ? "https://api.github.com/orgs/" + orgName + "/repos?per_page=100"
                    : "https://api.github.com/users/" + orgName + "/repos?per_page=100";

            while (url != null) {
                try {
                    Request request = buildGitHubRequest(url, token);
                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                            handleFetchError(response);
                            return;
                        }

                        assert response.body() != null;
                        String json = response.body().string();
                        List<String> pageRepoUrls = parseRepoUrls(json);
                        repoUrls.addAll(pageRepoUrls);

                        // Check for the next page
                        url = getNextPageUrl(response.header("Link"));
                    }

                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Error occurred during fetch: " + e.getMessage()));
                    e.printStackTrace();
                    return;
                }
            }

            forkRepositories(repoUrls, targetUser, token, client);
        });
    }

    private void handleFetchError(Response response) {
        String errorMsg = "Failed to fetch repos.";
        if (response.code() == 403) {
            errorMsg += " Rate limit exceeded.";
        }
        String finalErrorMsg = errorMsg;
        SwingUtilities.invokeLater(() -> statusLabel.setText(finalErrorMsg));
    }
    private void forkRepositories(List<String> repoUrls, String targetUser, String token, OkHttpClient client) {
        ExecutorService executor = Executors.newFixedThreadPool(5); // Using multiple threads for faster forking
        progressBar.setMaximum(repoUrls.size()); // Set maximum to number of repos
        progressBar.setValue(0);
        totalReposLabel.setText("Total Repos Found: " + repoUrls.size());

        for (String repoUrl : repoUrls) {
            executor.submit(() -> {
                try {
                    String repoName = getRepoNameFromUrl(repoUrl);
                    String forkUrl = "https://api.github.com/repos/" + getRepoOwnerFromUrl(repoUrl) + "/" + repoName + "/forks";

                    Request forkRequest = new Request.Builder()
                            .url(forkUrl)
                            .header("Authorization", "token " + token)
                            .post(RequestBody.create(new byte[0])) // Empty POST request
                            .build();

                    try (Response forkResponse = client.newCall(forkRequest).execute()) {
                        if (forkResponse.isSuccessful()) {
                            SwingUtilities.invokeLater(() -> {
                                statusLabel.setText("Forked " + repoName + " successfully to " + targetUser);
                                progressBar.setValue(progressBar.getValue() + 1); // Update progress bar
                                if (progressBar.getValue() >= progressBar.getMaximum()) {
                                    statusLabel.setText("Forking complete.");
                                }
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> statusLabel.setText("Failed to fork " + repoName + "."));
                        }
                    }

                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Error occurred during forking: " + e.getMessage()));
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown(); // Shutdown the executor service after all tasks are submitted
    }

    private List<String> parseRepoUrls(String json) {
        List<String> repoUrls = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(json);

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject repoObject = element.getAsJsonObject();
                String cloneUrl = repoObject.get("clone_url").getAsString();
                repoUrls.add(cloneUrl);
            }
        }

        return repoUrls;
    }

    private void downloadRepos(List<String> repoUrls) {
        int totalRepos = repoUrls.size();
        progressBar.setMaximum(totalRepos);
        progressBar.setValue(0);

        ExecutorService executor = Executors.newFixedThreadPool(5);  // Using multiple threads for faster cloning

        for (String repoUrl : repoUrls) {
            executor.submit(() -> {
                try {
                    String repoName = getRepoNameFromUrl(repoUrl);
                    File repoDir = new File(downloadLocation, repoName);

                    // Delete the directory if it exists
                    if (repoDir.exists()) {
                        deleteDirectory(repoDir);
                    }

                    long startTime = System.currentTimeMillis();
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Cloning: " + repoUrl);
                        downloadSpeedLabel.setText("Download Speed: Calculating..."); // Reset download speed label
                    });


                    Git.cloneRepository()
                            .setURI(repoUrl)
                            .setDirectory(repoDir)
                            .call();

                    long endTime = System.currentTimeMillis();
                    SwingUtilities.invokeLater(() -> {
                        updateDownloadSpeed(startTime, endTime, repoUrl);
                        progressBar.setValue(progressBar.getValue() + 1);
                        if (progressBar.getValue() >= progressBar.getMaximum()) {
                            statusLabel.setText("All repos downloaded successfully.");
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Error cloning repo: " + e.getMessage()));
                }
            });
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }

    private void updateDownloadSpeed(long startTime, long endTime, String repoUrl) {
        File repoDir = new File(downloadLocation, getRepoNameFromUrl(repoUrl));
        double repoSize = getSizeOfDirectory(repoDir); // Size in bytes
        double timeTaken = (endTime - startTime) / 1000.0; // Time in seconds
        double speedBps = repoSize / timeTaken; // Speed in bytes per second

        // Convert bytes per second to bits per second, then to megabits per second
        double speedMbps = (speedBps * 8) / (1024 * 1024);

        DecimalFormat df = new DecimalFormat("#.##");
        SwingUtilities.invokeLater(() -> downloadSpeedLabel.setText("Download Speed: " + df.format(speedMbps) + " Mbit/s"));
    }

    private double getSizeOfDirectory(File directory) {
        double size = 0;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    size += file.isFile() ? file.length() : getSizeOfDirectory(file);
                }
            }
        }
        return size;
    }

    private String getRepoNameFromUrl(String repoUrl) {
        String repoName = repoUrl.substring(repoUrl.lastIndexOf("/") + 1);
        if (repoName.endsWith(".git")) {
            repoName = repoName.substring(0, repoName.length() - 4);
        }
        return repoName;
    }

    private String getRepoOwnerFromUrl(String repoUrl) {
        String[] urlParts = repoUrl.split("/");
        return urlParts[urlParts.length - 2];  // Second to last part is the owner
    }

    private Request buildGitHubRequest(String url, String token) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github.v3+json");

        if (token != null && !token.trim().isEmpty()) {
            requestBuilder.header("Authorization", "token " + token);
        }

        return requestBuilder.build();
    }

}
