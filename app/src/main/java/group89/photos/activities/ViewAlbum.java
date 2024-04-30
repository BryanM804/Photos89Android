package group89.photos.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import group89.photos.Album;
import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.Photos;
import group89.photos.R;
import group89.photos.photoview.PhotoAdapter;

public class ViewAlbum extends AppCompatActivity {

    private String albumName = "Album Name";
    private AlbumManager albumManager;
    private RecyclerView photoList;
    private ActivityResultLauncher<Intent> startForResultAddRemove;
    private ActivityResultLauncher<Intent> startForResultRename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_album);

        Toolbar viewAlbumToolbar = findViewById(R.id.viewAlbumToolbar);
        Bundle bundle = getIntent().getExtras();
        albumManager = AlbumManager.getInstance();

        if (bundle != null) {
            albumName = bundle.getString("albumName");
        } else {
            // This is kind of a bad fix for this issue.
            // When you would open a photo and return to the open album it will lose the bundle
            // with the name of the album
            albumName = albumManager.getSelectedAlbum();
        }

        viewAlbumToolbar.setTitle(this.albumName);
        setSupportActionBar(viewAlbumToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Album album = albumManager.getAlbumByName(albumName);
        if (album != null) {
            List<Photo> photos = album.getPhotos();
            photoList = findViewById(R.id.photoList);
            photoList.setLayoutManager(new LinearLayoutManager(this));
            photoList.setAdapter(new PhotoAdapter(this, photos));
        }

        startForResultAddRemove = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        updateAlbumPhotos(result);
                    }
                });
        startForResultRename = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        updateAlbumName(result);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater newCarPrices = getMenuInflater();
        newCarPrices.inflate(R.menu.view_menu, menu);
        return true;
    }
    public void renameAlbum(MenuItem item) {
        Intent renameAlbumIntent = new Intent(this, AddEditAlbum.class);
        Bundle bundle = new Bundle();
        bundle.putString("albumName", albumName);

        renameAlbumIntent.putExtras(bundle);
        startForResultRename.launch(renameAlbumIntent);
    }

    public void updateAlbumName(ActivityResult res) {
        if (res.getData() == null) return;

        String newName = res.getData().getStringExtra("albumName");
        albumManager.getAlbumByName(albumName).rename(newName);
        albumManager.saveAlbums();

        getSupportActionBar().setTitle(newName);
    }

    public void deleteAlbum(MenuItem item) {
        // TODO: Add confirmation popup
        albumManager.deleteAlbum(albumName);
        albumManager.saveAlbums();

        finish();
    }

    public void addPhoto(MenuItem item) {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startForResultAddRemove.launch(pickPhotoIntent);
    }

    public void startSlideshow(MenuItem menuItem) {
        Intent slideshowIntent = new Intent(this, Slideshow.class);
        Bundle bundle = new Bundle();
        bundle.putString("albumName", albumName);

        List<String> uris = new ArrayList<>();
        for (Photo p : AlbumManager.getInstance().getAlbumByName(albumName).getPhotos()) {
            uris.add(p.getImage().toString());
        }

        bundle.putStringArray("photos", uris.toArray(new String[0]));
        slideshowIntent.putExtras(bundle);
        startActivity(slideshowIntent);
    }

    public void viewPhoto(Photo photo) {
        Intent viewPhotoIntent = new Intent(this, ViewPhoto.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("photo", photo);

        albumManager.saveAlbums();
        viewPhotoIntent.putExtras(bundle);
        startForResultAddRemove.launch(viewPhotoIntent);
    }

    private String isolateName(ContentResolver resolver, Uri uri) {
        Cursor cursor = resolver.query(uri, null, null, null, null);
        assert cursor != null;
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);

        cursor.close();

        return name;
    }

    private void updateAlbumPhotos(ActivityResult res) {
        if (res.getData() != null) {
            Uri imageUri = res.getData().getData();
            // Need persistent permissions for the URI or the app will crash when you try to enter the
            // album after the first time
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Photo newPhoto = new Photo(imageUri, isolateName(getContentResolver(), imageUri));
            albumManager.getAlbumByName(albumName).addPhoto(newPhoto);
            albumManager.saveAlbums();
        }

        List<Photo> photos = albumManager.getAlbumByName(albumName).getPhotos();
        photoList.setAdapter(new PhotoAdapter(this, photos));
        photoList.getAdapter().notifyDataSetChanged();
    }
}
