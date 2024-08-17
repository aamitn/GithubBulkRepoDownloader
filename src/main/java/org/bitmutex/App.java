package org.bitmutex;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GithubBulkRepoDownloader frame = new GithubBulkRepoDownloader();
            frame.setVisible(true);
        });
    }
}
