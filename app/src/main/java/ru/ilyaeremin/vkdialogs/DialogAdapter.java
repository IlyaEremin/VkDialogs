package ru.ilyaeremin.vkdialogs;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import ru.ilyaeremin.vkdialogs.models.Dialog;
import ru.ilyaeremin.vkdialogs.views.AvatarDrawable;
import ru.ilyaeremin.vkdialogs.views.DialogView;
import ru.ilyaeremin.vkdialogs.views.ProgressView;

/**
 * Created by Ilya Eremin on 17.01.2016.
 */
public class DialogAdapter extends RecyclerView.Adapter {

    private final     List<Dialog>       items;
    @Nullable private OnLoadMoreListener listener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public DialogAdapter(List<Dialog> items) {
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
        if (!DialogManager.getInstance().isEndReached && position > items.size() - 2 && listener != null && !DialogManager.getInstance().loading) {
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
        if (count == 0 && DialogManager.getInstance().loading) {
            return 0;
        }
        if (!DialogManager.getInstance().isEndReached) {
            count++;
        }
        return count;
    }

    public void updateItems(List<Dialog> dialogs) {
        this.items.addAll(dialogs);
        notifyDataSetChanged();
    }

    private static class DialogHolder extends RecyclerView.ViewHolder {

        private SimpleTarget[] targets;

        public DialogHolder(final View itemView) {
            super(itemView);
            targets = new SimpleTarget[4];
            for (int i = 0; i < 4; i++) {
                final int finalI = i;
                targets[i] = new SimpleTarget<Bitmap>(AvatarDrawable.width, AvatarDrawable.height) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        ((DialogView) itemView).updateImage(bitmap, finalI);
                    }
                };
            }
        }

        @SuppressWarnings("unchecked") public void draw(Dialog dialog) {
            final DialogView dialogView = (DialogView) itemView;
            dialogView.setDialog(dialog);
            String[] chatPhotoUrl = dialog.getChatPhotoUrl();
            dialogView.updateAvatarsCount(chatPhotoUrl.length);
            for (int i = 0; i < chatPhotoUrl.length; i++) {
                Glide
                    .with(this.itemView.getContext()).load(chatPhotoUrl[i]).asBitmap()
                    .into((SimpleTarget<Bitmap>) targets[i]);
            }
        }

    }

    private class ProgressHolder extends RecyclerView.ViewHolder {
        public ProgressHolder(View view) {
            super(view);
        }
    }

    public List<Dialog> getItems() {
        return items;
    }
}
