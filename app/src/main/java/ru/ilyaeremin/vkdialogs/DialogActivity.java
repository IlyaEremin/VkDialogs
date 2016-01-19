package ru.ilyaeremin.vkdialogs;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.ilyaeremin.vkdialogs.models.Dialog;
import ru.ilyaeremin.vkdialogs.utils.AndroidUtils;
import ru.ilyaeremin.vkdialogs.utils.Views;

public class DialogActivity extends AppCompatActivity {

    private View         progressBar;
    private RecyclerView dialogsRv;
    private View         loginBtn;

    private DialogAdapter adapter;

    private List<Dialog> dialogs = new ArrayList<Dialog>() {{
        add(new Dialog("First dialog", "This is last message", "20:40", new long[]{1, 2, 3, 4}));
        add(new Dialog("Second dialog", "Very long long long long long long long long long long long long", "20:30",
            new long[]{1, 2, 3, 4, 6}));
        add(new Dialog("Third dialog", "This is last message", "20:20", new long[]{1, 2, 3, 4, 5}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
        add(new Dialog("First dialog", "This is last message", "20:10", new long[]{1, 2, 3, 4, 5, 6}));
    }};

    private Map<Long, String> avatars = new HashMap<Long, String>() {{
        put(1l, "https://pp.vk.me/c633122/v633122420/1f6/zPsvRA4da5A.jpg");
        put(2l, "https://pp.vk.me/c633617/v633617571/75b8/of2b4_TRhTU.jpg");
        put(3l, "https://pp.vk.me/c625128/v625128965/47741/mmjH-qSJ7k0.jpg");
        put(4l, "https://pp.vk.me/c622826/v622826715/36090/5YZvdo1TXOo.jpg");
        put(5l, "https://pp.vk.me/c625729/v625729333/45330/NL31RuLJ9Qo.jpg");
        put(6l, "https://pp.vk.me/c613525/v613525456/1789e/sGWkI6PbHUk.jpg");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        progressBar = Views.findById(this, R.id.progress);
        dialogsRv = Views.findById(this, R.id.rv);
        loginBtn = Views.findById(this, R.id.login_button);
        dialogsRv.setLayoutManager(new LinearLayoutManager(this));
        dialogsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = AndroidUtils.dp(16);
                }
                outRect.left = AndroidUtils.dp(16);
                outRect.right = AndroidUtils.dp(16);
                outRect.bottom = AndroidUtils.dp(8);
            }
        });
        if (!VKSdk.isLoggedIn()) {
            Views.gone(dialogsRv);
            VKSdk.login(DialogActivity.this, VKScope.MESSAGES);
        } else {
            loginBtn.setVisibility(View.GONE);
            fetchDialogs();
        }
    }

    private void fetchDialogs() {
        adapter = new DialogAdapter(dialogs);
        dialogsRv.setAdapter(adapter);
        adapter.avatarsRetrieved(avatars);

//        progressBar.setVisibility(View.VISIBLE);
//        VKRequest dialogRequests = VKApi.messages().getDialogs();
//        dialogRequests.executeWithListener(new VKRequest.VKRequestListener() {
//            @Override public void onComplete(VKResponse response) {
//                hideProgress();
//                VKApiGetDialogResponse model = (VKApiGetDialogResponse) response.parsedModel;
//            }
//
//            @Override
//            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
//                super.attemptFailed(request, attemptNumber, totalAttempts);
//            }
//
//            @Override public void onError(VKError error) {
//                hideProgress();
//            }
//        });
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
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
}
