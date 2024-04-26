package group89.photos.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.R;

public class ViewAlbum extends AppCompatActivity {

    private String albumName = "Album Name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_album);

        Toolbar viewAlbumToolbar = findViewById(R.id.viewAlbumToolbar);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            this.albumName = bundle.getString("albumName");
        }
        viewAlbumToolbar.setTitle(this.albumName);
        setSupportActionBar(viewAlbumToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void renameAlbum(MenuItem item) {

    }

    public void deleteAlbum(MenuItem item) {

    }

    public void addPhoto(MenuItem item) {

    }
}
