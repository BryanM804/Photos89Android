package group89.photos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.Album;
import group89.photos.AlbumManager;
import group89.photos.R;

public class AddEditAlbum extends AppCompatActivity {

    private EditText albumNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_album);

        Toolbar addAlbumToolbar = findViewById(R.id.addAlbumToolbar);
        setSupportActionBar(addAlbumToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Adds the back button

        albumNameInput = findViewById(R.id.albumNameInput);

        // If there is a bundle this is a rename window instead of new album
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            getSupportActionBar().setTitle("Rename Album");
            albumNameInput.setText(bundle.getString("albumName"));
        } else {
            getSupportActionBar().setTitle("Add Album");
        }
    }

    public void saveAlbum(View view) {
        String albumName = albumNameInput.getText().toString();
        boolean exists = false;

        if (albumName.isEmpty()) {
            Toast.makeText(this, "Please enter an album name!", Toast.LENGTH_LONG).show();
            return;
        }

        for (Album a : AlbumManager.getInstance().getAlbums()) {
            if (a.getName().equals(albumName)) {
                exists = true;
                break;
            }
        }

        if (exists) {
            Toast.makeText(this, "Album already exists!", Toast.LENGTH_LONG).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("albumName", albumName);

        Intent resultIntent = new Intent();
        resultIntent.putExtras(bundle);

        setResult(RESULT_OK, resultIntent);

        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
