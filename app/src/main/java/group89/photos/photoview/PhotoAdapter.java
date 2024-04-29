package group89.photos.photoview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import group89.photos.Album;
import group89.photos.AlbumManager;
import group89.photos.Photo;
import group89.photos.R;
import group89.photos.activities.ViewAlbum;
import group89.photos.activities.ViewPhoto;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private final PhotoClickListener clickListener = new PhotoClickListener();
    private Context context;
    private List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
        clickListener.setContext(context);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo, parent, false);
        view.setOnClickListener(clickListener);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.imageView.setImageURI(photos.get(position).getImage());
        holder.personTags.setText(context.getString(R.string.people,
                photos.get(position).getPersonTags().toString()));
        holder.locationTags.setText(context.getString(R.string.locations,
                photos.get(position).getLocationTags().toString()));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        clickListener.setRecyclerView(recyclerView);
        clickListener.setPhotos(photos);
    }
}

class PhotoClickListener implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Photo> photos;
    private Context context;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    @Override
    public void onClick(View v) {
        if (recyclerView != null && photos != null) {
            int pos = recyclerView.getChildAdapterPosition(v);
            Photo selectedPhoto = photos.get(pos);
            ViewAlbum parentActivity = (ViewAlbum) context;
            parentActivity.viewPhoto(selectedPhoto);
        }
    }
}
