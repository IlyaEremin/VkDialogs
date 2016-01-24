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

import com.google.gson.Gson;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.List;

import ru.ilyaeremin.vkdialogs.models.Dialog;
import ru.ilyaeremin.vkdialogs.models.Users;
import ru.ilyaeremin.vkdialogs.models.VkChatResponse;
import ru.ilyaeremin.vkdialogs.utils.AndroidUtils;
import ru.ilyaeremin.vkdialogs.utils.Views;

public class DialogActivity extends AppCompatActivity implements OnLoadMoreListener {

    private static final int MAX_DIALOG_COUNT = 200;
    private ProgressBar  progressBar;
    private RecyclerView dialogsRv;
    private View         loginBtn;

    private Gson gson;

    private DialogAdapter adapter;
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (!VKSdk.isLoggedIn()) {
            Views.gone(dialogsRv);
            VKSdk.login(DialogActivity.this, VKScope.MESSAGES);
        } else {
            Views.gone(loginBtn, dialogsRv);
            fetchDialogs(0);
        }
    }

    private synchronized Gson getParser(){
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private void fetchDialogs(final int offset) {
        if (offset == 0) {
            showProgress();
        }
        VKRequest chatRequest = new VKRequest("execute.getChats", VKParameters.from("offset", String.valueOf(offset)));
        DialogManager.loading = true;

        chatRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override public void onComplete(VKResponse response) {
                DialogManager.loading = false;
                hideProgress();
                dialogsRv.setVisibility(View.VISIBLE);
                // TODO parse in background
                String json = response.responseString.replaceFirst("\"dialogs\":\\[\\d*,", "\"dialogs\": [");
                VkChatResponse chatResponse = getParser().fromJson(json, VkChatResponse.class);
                if (chatResponse.getDialogs().size() < MAX_DIALOG_COUNT) {
                    DialogManager.isEndReached = true;
                } else {
                    DialogActivity.this.offset += chatResponse.getDialogs().size();
                }
                Users.getInstance().populate(chatResponse.getProfiles());
                List<Dialog> chats = chatResponse.getChats();
                if (offset == 0) {
                    adapter = new DialogAdapter(chats);
                    adapter.setOnLoadMoreListener(DialogActivity.this);
                    dialogsRv.setAdapter(adapter);
                } else {
                    adapter.updateItems(chats);
                }
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override public void onError(VKError error) {
                hideProgress();
            }
        });
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                fetchDialogs(0);
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
        fetchDialogs(offset);
    }
}
