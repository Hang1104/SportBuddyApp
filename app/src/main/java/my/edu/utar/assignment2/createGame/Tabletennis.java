package my.edu.utar.sportsbuddy;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Tabletennis extends AppCompatActivity {

    private WebView webView1;
    private WebView webView2;
    private WebView webView3;

    private String[] descriptions = {
            "Basic Rules of Table Tennis",
            "Table Tennis Ball Tossing Etiquette. The Do's and Don'ts!",

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabletennis);

        webView1 = findViewById(R.id.webView1);
        webView2 = findViewById(R.id.webView2);


        // Embed YouTube video URLs
        String[] videoUrls = {
                "https://www.youtube.com/embed/SPO57-MouV8?si=lVxQmcQL6M8dwS-U",
                "https://www.youtube.com/embed/PDlgAPn-1ZY?si=Toudl-eh_vfkCi00"

        };

        // Load the HTML content with the base URL for each WebView
        for (int i = 0; i < videoUrls.length; i++) {
            String htmlContent = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrls[i] + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";
            WebView webView = null;
            switch (i) {
                case 0:
                    webView = webView1;
                    break;
                case 1:
                    webView = webView2;
                    break;
                case 2:
                    webView = webView3;
                    break;
            }
            if (webView != null) {
                webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            }
        }

        // Set descriptions
        TextView description1 = findViewById(R.id.description1);
        TextView description2 = findViewById(R.id.description2);

        description1.setText(descriptions[0]);
        description2.setText(descriptions[1]);

    }
}
