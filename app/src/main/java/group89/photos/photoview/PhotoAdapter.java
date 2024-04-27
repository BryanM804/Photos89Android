package group89.photos.photoview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import group89.photos.Photo;
import group89.photos.R;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private Context context;
    private List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.imageView.setImageURI(photos.get(position).getImage());
        holder.personTags.setText(photos.get(position).getPersonTags().toString());
        holder.locationTags.setText(photos.get(position).getLocationTags().toString());
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
