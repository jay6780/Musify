package com.sl.music2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;

public class WebViewActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "music_url";
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    private WebView webView;
    private EditText editText;
    private Button pasteButton;
    private String youtubeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        showDownloadPromptDialog();
        webView = findViewById(R.id.webview);
        editText = findViewById(R.id.editText);
        pasteButton = findViewById(R.id.pasteButton);

        // Load the URL passed through the intent
        String musicUrl = getIntent().getStringExtra(EXTRA_URL);
        loadUrl(musicUrl);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webView.setInitialScale(1);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient());

        // Set up the WebView download listener
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                // Check if the app has necessary permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {
                    // Request permission to write to external storage
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
                    return;
                }

                // Start the download
                downloadFile(url, mimetype);
            }
        });

        // Handle the "Paste" button click
        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtubeUrl = editText.getText().toString().trim();
                if (isYouTubeLink(youtubeUrl)) {
                    downloadYouTubeVideo(youtubeUrl);
                }
            }
        });
    }

    // Download a YouTube video's MP3 audio
    private void downloadYouTubeVideo(String youtubeUrl) {
        YouTubeExtractor youTubeExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles != null) {
                    // Choose the format you want to download (e.g., itag 140 for MP3)
                    YtFile mp3File = ytFiles.get(140);
                    if (mp3File != null) {
                        // Start the download
                        downloadFile(mp3File.getUrl(), "audio/mpeg");
                    } else {
                        Toast.makeText(WebViewActivity.this, "No suitable MP3 format found for download", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(WebViewActivity.this, "Failed to extract video links", Toast.LENGTH_SHORT).show();
                }
            }
        };
        youTubeExtractor.execute(youtubeUrl);
    }

    // Download a file
// Download a file
    private void downloadFile(String url, String mimetype) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType(mimetype);

        // Dynamically generate the file name based on the download link
        String fileName = URLUtil.guessFileName(url, null, mimetype);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        request.setDestinationUri(Uri.fromFile(file));

        // Set the notification title
        request.setTitle("Mp3 Download");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Get download service and enqueue the request
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }



    private void showDownloadPromptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Download Music")
                .setMessage("To download music, paste a URL below:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dialog is dismissed
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadFile(youtubeUrl, "audio/mpeg");
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private void loadUrl(String url) {
        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
        }
    }

    private boolean isYouTubeLink(String url) {
        return url.contains("youtube.com") || url.contains("youtu.be");
    }
}


