package group89.photos.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.R;

public class ViewPhoto extends AppCompatActivity {

    private Photo viewingPhoto;
    private ActivityResultLauncher<Intent> startForResult;
    private TextView personTagsView;
    private TextView locationTagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_photo);

        Toolbar photoToolbar = findViewById(R.id.photoToolbar);
        setSupportActionBar(photoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ImageView imageView = findViewById(R.id.largeImageView);
            personTagsView = findViewById(R.id.personTagsText);
            locationTagsView = findViewById(R.id.locationTagsText);

            Photo photoCopy = bundle.getSerializable("photo", Photo.class);
            viewingPhoto = AlbumManager.getInstance().getMatchingPhoto(photoCopy);

            imageView.setImageURI(viewingPhoto.getImage());
            updateTags();
        }

        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        updateTags();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater foodPrices = getMenuInflater();
        foodPrices.inflate(R.menu.photo_menu, menu);
        return true;
    }

    public void addTag(MenuItem menuItem) {
        Intent addTagIntent = new Intent(this, AddTag.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("photo", viewingPhoto);
        addTagIntent.putExtras(bundle);

        startForResult.launch(addTagIntent);
    }

    public void movePhoto(MenuItem menuItem) {

    }

    public void removePhoto(MenuItem menuItem) {

    }

    public void updateTags() {
        String peopleString = viewingPhoto.getPersonTags().toString();
        String locationString = viewingPhoto.getLocationTags().toString();

        personTagsView.setText(getString(R.string.people, peopleString));
        locationTagsView.setText(getString(R.string.locations, locationString));
    }
}
