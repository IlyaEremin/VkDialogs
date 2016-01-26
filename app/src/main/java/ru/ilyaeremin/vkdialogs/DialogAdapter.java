package ru.ilyaeremin.vkdialogs;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;
import java.util.Map;

import ru.ilyaeremin.vkdialogs.models.Chat;

/**
 * Created by Ilya Eremin on 17.01.2016.
 */
public class DialogAdapter extends RecyclerView.Adapter {

    private final List<Chat>         items;
    private       Map<Long, String>  avatars;
    private       OnLoadMoreListener listener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public DialogAdapter(List<Chat> items) {
        this.items = items;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new DialogHolder(new DialogView(parent.getContext()));
        } else {
            return new ProgressHolder(new ProgressView(parent.getContext()));
        }
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DialogHolder) {
            ((DialogHolder) holder).draw(items.get(position));
        }
        if (!DialogManager.isEndReached && position > items.size() - 2 && listener != null && !DialogManager.loading) {
            listener.onLoadMore();
        }
    }

    @Override public int getItemViewType(int position) {
        if (position == items.size()) {
            return 1;
        }
        return 0;
    }

    @Override public int getItemCount() {
        int count = items.size();
        if (count == 0 && DialogManager.loading) {
            return 0;
        }
        if (!DialogManager.isEndReached) {
            count++;
        }
        return count;
    }

    public void updateItems(List<Chat> chats) {
        this.items.addAll(chats);
        notifyDataSetChanged();
    }

    private static class DialogHolder extends RecyclerView.ViewHolder {

        public DialogHolder(View itemView) {
            super(itemView);
        }

        public void draw(Chat chat) {
            final DialogView dialogView = (DialogView) itemView;
            dialogView.setChat(chat);
            String[] chatPhotoUrl = chat.getChatPhotoUrl();
            dialogView.updateAvatarsCount(chatPhotoUrl.length);
            for (int i = 0; i < chatPhotoUrl.length; i++) {
                final int finalI = i;
                Glide.with(this.itemView.getContext()).load(chatPhotoUrl[i]).asBitmap()
                    .into(new SimpleTarget<Bitmap>(AvatarDrawable.width, AvatarDrawable.height) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            (dialogView).updateImage(bitmap, finalI);
                        }
                    });
            }
        }

    }

    private class ProgressHolder extends RecyclerView.ViewHolder {
        public ProgressHolder(View view) {
            super(view);
        }
    }

    public List<Chat> getItems(){
        return items;
    }
}
