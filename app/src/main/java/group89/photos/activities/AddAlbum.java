package group89.photos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.R;

public class AddAlbum extends AppCompatActivity {

    private EditText albumNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_album);

        Toolbar addAlbumToolbar = findViewById(R.id.addAlbumToolbar);
        addAlbumToolbar.setTitle("Add Album");
        setSupportActionBar(addAlbumToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Adds the back button

        albumNameInput = findViewById(R.id.albumNameInput);
    }

    public void saveAlbum(View view) {
        String albumName = albumNameInput.getText().toString();

        if (albumName.isEmpty()) {
            // TODO: Display Error
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
