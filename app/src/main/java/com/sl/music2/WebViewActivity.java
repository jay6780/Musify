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
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "music_url";
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    private WebView webView;
    private EditText editText;
    private Button pasteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

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
        showDownloadPromptDialog();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isYouTubeLink(url)) {
                    webView.loadUrl("https://www.y2mate.com/en1842/youtube-mp3");
                }
            }
        });

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

                // Create a DownloadManager request
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimetype);

                // Dynamically generate the file name based on the download link
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                // Get download service and enqueue the request
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
            }
        });

        // Handle the "Paste" button click
        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pastedText = editText.getText().toString().trim();
                if (isYouTubeLink(pastedText)) {
                    showDownloadConfirmationDialog(pastedText,musicUrl); // Show confirmation dialog for YouTube link
                } else {
                    loadUrl(pastedText); // Load the pasted URL
                }
            }
        });
    }

    private void showDownloadPromptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Download Music")
                .setMessage("To download music, paste a YouTube URL below:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dialog is dismissed
                    }
                })
                .show();
    }

    private void showDownloadConfirmationDialog(final String youtubeUrl, final String musicUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Download Music")
                .setMessage("Do you want to download the music from the YouTube link?")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Proceed with downloading
                        loadUrl("https://www.y2mate.com/en1842/youtube-mp3"); // Load the conversion site
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the download
                        loadUrl(musicUrl); // Load the original music URL
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // Handle canceling the dialog (if the user presses the back button)
                        loadUrl(musicUrl); // Load the original music URL
                    }
                })
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the download
                webView.reload(); // Reload the WebView to trigger the download again
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(0, 0);
            super.onBackPressed();
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
