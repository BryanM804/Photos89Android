package group89.photos.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.R;

public class Slideshow extends AppCompatActivity {

    private String[] uris;
    private int index;
    private ImageView slideshowImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow);

        Toolbar slideshowToolbar = findViewById(R.id.slideshowToolbar);
        setSupportActionBar(slideshowToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("albumName"));

            uris = bundle.getStringArray("photos");
            index = 0;
            slideshowImage = findViewById(R.id.slideshowImage);
            slideshowImage.setImageURI(Uri.parse(uris[index]));
        }
    }

    public void nextPhoto(View v) {
        if (index < uris.length - 1) {
            index++;
            slideshowImage.setImageURI(Uri.parse(uris[index]));
        }
    }

    public void previousPhoto(View v) {
        if (index > 0) {
            index--;
            slideshowImage.setImageURI(Uri.parse(uris[index]));
        }
    }
}
