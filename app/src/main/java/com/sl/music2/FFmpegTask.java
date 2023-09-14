package com.sl.music2;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFmpegTask extends AsyncTask<String, Void, Integer> {

    private FFmpegCallback callback;

    public FFmpegTask(FFmpegCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(String... commands) {
        try {
            Process process = new ProcessBuilder(commands).redirectErrorStream(true).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Process FFmpeg output, if needed
            }
            reader.close();
            process.waitFor();
            return process.exitValue();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (callback != null) {
            callback.onFFmpegComplete(result);
        }
    }

    public interface FFmpegCallback {
        void onFFmpegComplete(int resultCode);
    }
}
