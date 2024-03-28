package org.coolstyles.thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sử dụng Thread để tải file từ internet
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadFileUsingThread("https://example.com/file-to-download.txt", "downloaded_file_thread.txt");
            }
        }).start();

        // Sử dụng AsyncTask để tải file từ internet
        new FileDownloaderAsyncTask().execute("https://example.com/file-to-download.txt", "downloaded_file_asynctask.txt");
    }

    private void downloadFileUsingThread(String fileUrl, String destinationPath) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            FileOutputStream fileOutputStream = openFileOutput(destinationPath, MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();
            connection.disconnect();

            Log.d("MainActivity", "File downloaded successfully using Thread.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class FileDownloaderAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String destinationPath = strings[1];

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                inputStream.close();
                connection.disconnect();

                Log.d("MainActivity", "File downloaded successfully using AsyncTask.");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
