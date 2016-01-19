package ru.ilyaeremin.vkdialogs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import ru.ilyaeremin.vkdialogs.models.Dialog;
import ru.ilyaeremin.vkdialogs.utils.Randoms;
import ru.ilyaeremin.vkdialogs.utils.Views;

/**
 * Created by Ilya Eremin on 17.01.2016.
 */
public class DialogAdapter extends RecyclerView.Adapter {

    private final List<Dialog> items;
    private Map<Long, String> avatars;

    public DialogAdapter(List<Dialog> items) {
        this.items = items;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, parent, false));
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DialogHolder)holder).draw(items.get(position));
        ((DialogHolder) holder).drawAvatars(grabAvatarsFor(position));
    }

    private String[] grabAvatarsFor(int position) {
        Dialog dialog = this.items.get(position);
        int avatarCount = Math.min(4, items.get(position).getUserIds().length);
        String[] usersPhotoUrls = new String[4];
        int[] randoms = Randoms.getNRandomNumberWithinRange(avatarCount, items.get(position).getUserIds().length);
        for (int i = 0; i < 4; i++) {
            usersPhotoUrls[i] = avatars.get(dialog.getUserIds()[randoms[i]]);
        }
        return usersPhotoUrls;
    }

    public void avatarsRetrieved(Map<Long, String> avatars) {
        this.avatars = avatars;
        notifyDataSetChanged();
    }

    @Override public int getItemCount() {
        return items.size();
    }

    private static class DialogHolder extends RecyclerView.ViewHolder {

        private TextView  title;
        private TextView  time;
        private TextView  lastMessage;
        private ImageView image1;
        private ImageView image2;
        private ImageView image3;
        private ImageView image4;

        public DialogHolder(View itemView) {
            super(itemView);
            title = Views.findById(itemView, R.id.title);
            time = Views.findById(itemView, R.id.time);
            lastMessage = Views.findById(itemView, R.id.last_message);
            image1 = Views.findById(itemView, R.id.image);
            image2 = Views.findById(itemView, R.id.image1);
            image3 = Views.findById(itemView, R.id.image2);
            image4 = Views.findById(itemView, R.id.image3);
        }

        public void draw(Dialog dialog) {
            title.setText(dialog.getTitle());
            time.setText(dialog.getTimeOfLastImage());
            lastMessage.setText(dialog.getLastMessage());
        }

        public void drawAvatars(String[] urls){
            if (urls[0] != null) {
                Glide.with(itemView.getContext()).load(urls[0]).into(image1);
            }
            if (urls[1] != null) {
                Glide.with(itemView.getContext()).load(urls[1]).into(image2);
            }
            if (urls[2] != null) {
                Glide.with(itemView.getContext()).load(urls[2]).into(image3);
            }
            if (urls[3] != null) {
                Glide.with(itemView.getContext()).load(urls[3]).into(image4);
            }
        }
    }
}
