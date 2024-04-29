package group89.photos.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.R;

public class AddTag extends AppCompatActivity {

    private Photo taggingPhoto;
    private TextView tagValueInput;
    private RadioButton personRadioButton;
    private RadioButton locationRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tag);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) finish();

        Photo photoCopy = bundle.getSerializable("photo", Photo.class);
        taggingPhoto = AlbumManager.getInstance().getMatchingPhoto(photoCopy);

        Toolbar addTagToolbar = findViewById(R.id.addTagToolbar);
        setSupportActionBar(addTagToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagValueInput = findViewById(R.id.tagValueInput);
        personRadioButton = findViewById(R.id.personRadioButton);
        locationRadioButton = findViewById(R.id.locationRadioButton);
    }

    public void saveTag(View view) {
        String tag = tagValueInput.getText().toString();

        if (!tag.isEmpty()) {
            if (personRadioButton.isChecked()) {
                taggingPhoto.addTag("person", tag);
                AlbumManager.getInstance().saveAlbums();
                Log.d("INFO", "Added person tag");

                setResult(RESULT_OK);
                finish();
            } else if (locationRadioButton.isChecked()) {
                taggingPhoto.addTag("location", tag);
                AlbumManager.getInstance().saveAlbums();
                Log.d("INFO", "Added location tag");

                setResult(RESULT_OK);
                finish();
            }
        }

        // TODO: make error message
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}
