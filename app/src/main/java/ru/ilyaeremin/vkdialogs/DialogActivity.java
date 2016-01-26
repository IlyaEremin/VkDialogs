package ru.ilyaeremin.vkdialogs;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import ru.ilyaeremin.vkdialogs.models.Chat;
import ru.ilyaeremin.vkdialogs.utils.AndroidUtils;
import ru.ilyaeremin.vkdialogs.utils.Views;

public class DialogActivity extends AppCompatActivity implements OnLoadMoreListener {

    private static final String KEY_DIALOGS   = "dialogs";

    private ProgressBar  progressBar;
    private RecyclerView dialogsRv;
    private View         loginBtn;

    private DialogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_dialog);
        progressBar = Views.findById(this, R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF3F5D81, PorterDuff.Mode.MULTIPLY);
        dialogsRv = Views.findById(this, R.id.rv);
        loginBtn = Views.findById(this, R.id.login_button);

        dialogsRv.setLayoutManager(new LinearLayoutManager(this));
        dialogsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = AndroidUtils.dp(12);
                }
            }
        });

        if (savedState != null) {
            ArrayList<Chat> chats = savedState.getParcelableArrayList(KEY_DIALOGS);
            if (chats != null) {
                createAndSetAdapter(chats);
            } else {
                if (DialogManager.loading) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (!VKSdk.isLoggedIn()) {
                VKSdk.login(DialogActivity.this, VKScope.MESSAGES);
            } else {
                if (DialogManager.loading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    fetchDialogs();
                }
            }
        }
        EventBus.getDefault().registerSticky(this);
    }

    private void createAndSetAdapter(List<Chat> chats) {
        adapter = new DialogAdapter(chats);
        dialogsRv.setAdapter(adapter);
        adapter.setOnLoadMoreListener(DialogActivity.this);
        Views.gone(loginBtn, progressBar);
        dialogsRv.setVisibility(View.VISIBLE);
    }

    public void onEvent(OnLoadFinished event){
        if (event.code == Code.SUCCESS) {
            onLoadFinished(event.getChats());
        } else {
            if (adapter == null) {
                showRetryButton();
            }
        }
        EventBus.getDefault().removeStickyEvent(OnLoadFinished.class);
    }

    private void showRetryButton() {
        Toast.makeText(this, "error while loading", Toast.LENGTH_LONG).show();
    }

    private void onLoadFinished(List<Chat> chats) {
        if (adapter == null) {
            createAndSetAdapter(chats);
        } else {
            adapter.updateItems(chats);
        }
    }

    private void fetchDialogs() {
        if (adapter == null) { progressBar.setVisibility(View.VISIBLE) }
        DialogManager.loadDialogs();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null) {
            outState.putParcelableArrayList(KEY_DIALOGS, (ArrayList)adapter.getItems());
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                fetchDialogs();
            }

            @Override
            public void onError(VKError error) {
                Toast
                    .makeText(DialogActivity.this, "Ошибка при авторизации, попробуйте еще раз", Toast.LENGTH_LONG)
                    .show();
                loginBtn.setVisibility(View.VISIBLE);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override public void onLoadMore() {
        fetchDialogs();
    }

    @Override protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
