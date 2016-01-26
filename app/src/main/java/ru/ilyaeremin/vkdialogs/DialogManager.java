package ru.ilyaeremin.vkdialogs;

import com.google.gson.Gson;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import de.greenrobot.event.EventBus;
import ru.ilyaeremin.vkdialogs.models.Users;
import ru.ilyaeremin.vkdialogs.models.VkChatResponse;

/**
 * Created by Ilya Eremin on 25.01.2016.
 */
public class DialogManager {
    private static final int MAX_DIALOG_COUNT = 200;
    public static  boolean loading;
    public static  boolean isEndReached;
    private static Gson    gson;
    private static int currentOffset = 0;

    private static synchronized Gson getParser() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static void loadDialogs() {
        if (loading) throw new IllegalStateException("can't start second loading");

        DialogManager.loading = true;
        VKRequest chatRequest = new VKRequest("execute.getChats", VKParameters.from("offset", String.valueOf(currentOffset)));
        chatRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override public void onComplete(VKResponse response) {
                DialogManager.loading = false;
                // TODO parse in background
                String json = response.responseString.replaceFirst("\"dialogs\":\\[\\d*,", "\"dialogs\": [");
                VkChatResponse chatResponse = getParser().fromJson(json, VkChatResponse.class);
                if (chatResponse.getDialogs().size() < MAX_DIALOG_COUNT) {
                    DialogManager.isEndReached = true;
                } else {
                    currentOffset += chatResponse.getDialogs().size();
                }
                Users.getInstance().populate(chatResponse.getProfiles());
                EventBus.getDefault().postSticky(new OnLoadFinished(chatResponse.getChats()));
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override public void onError(VKError error) {
                EventBus.getDefault().postSticky(new OnLoadFinished(Code.FAIL));
            }
        });
    }
}
