package ru.ilyaeremin.vkdialogs;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;
import java.util.Map;

import ru.ilyaeremin.vkdialogs.models.Dialog;
import ru.ilyaeremin.vkdialogs.utils.AndroidUtils;
import ru.ilyaeremin.vkdialogs.utils.Randoms;
import ru.ilyaeremin.vkdialogs.utils.Views;

/**
 * Created by Ilya Eremin on 17.01.2016.
 */
public class DialogAdapter extends RecyclerView.Adapter {

    private final List<Dialog>      items;
    private       Map<Long, String> avatars;

    public DialogAdapter(List<Dialog> items) {
        this.items = items;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, parent, false));
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DialogHolder) holder).draw(items.get(position));
        ((DialogHolder) holder).drawAvatars(grabAvatarsFor(position));
    }

    private String[] grabAvatarsFor(int position) {
        Dialog dialog = this.items.get(position);
        int avatarCount = Math.min(4, items.get(position).getUserIds().length);
        String[] usersPhotoUrls = new String[avatarCount];
        int[] randoms = Randoms.getNRandomNumberWithinRange(avatarCount, items.get(position).getUserIds().length);
        for (int i = 0; i < avatarCount; i++) {
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
        private CircleImageView image;

        public DialogHolder(View itemView) {
            super(itemView);
            title = Views.findById(itemView, R.id.title);
            time = Views.findById(itemView, R.id.time);
            lastMessage = Views.findById(itemView, R.id.last_message);
            image = Views.findById(itemView, R.id.image);
            multiDrawable = new MultiDrawable(itemView.getContext());
            multiDrawable.setBounds(0, 0, AndroidUtils.dp(64), AndroidUtils.dp(64));
        }

        public void draw(Dialog dialog) {
            title.setText(dialog.getTitle());
            time.setText(dialog.getTimeOfLastImage());
            lastMessage.setText(dialog.getLastMessage());
        }

        private MultiDrawable multiDrawable;

        public void drawAvatars(String[] urls) {
            int dp64 = AndroidUtils.dp(64);
            multiDrawable.setSize(urls.length);

            for (int i = 0; i < urls.length; i++) {
                final int finalI = i;
                Glide.with(itemView.getContext()).load(urls[i]).asBitmap()
                    .into(new SimpleTarget<Bitmap>(dp64, dp64) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            multiDrawable.onDownloadFinished(finalI, bitmap);
                            image.setImageDrawable(multiDrawable);
                        }
                    });
            }
        }
    }
}
