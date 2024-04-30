package group89.photos.photoview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import group89.photos.R;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    protected ImageView imageView;
    protected TextView personTags;
    protected TextView locationTags;
    protected TextView photoName;
    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);

        this.imageView = itemView.findViewById(R.id.imageView);
        this.personTags = itemView.findViewById(R.id.personTags);
        this.locationTags = itemView.findViewById(R.id.locationTags);
        this.photoName = itemView.findViewById(R.id.photoName);
    }
}
