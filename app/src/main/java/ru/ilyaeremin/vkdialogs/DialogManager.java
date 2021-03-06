package ru.ilyaeremin.vkdialogs;

import com.google.gson.Gson;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import de.greenrobot.event.EventBus;
import ru.ilyaeremin.vkdialogs.models.Code;
import ru.ilyaeremin.vkdialogs.utils.Dates;
import ru.ilyaeremin.vkdialogs.utils.Users;
import ru.ilyaeremin.vkdialogs.models.VkChatResponse;
import ru.ilyaeremin.vkdialogs.utils.Threads;

/**
 * Created by Ilya Eremin on 25.01.2016.
 */
public class DialogManager {
    private static final int MAX_DIALOG_COUNT = 200;
    public boolean loading;
    public boolean isEndReached;
    private static Gson    gson;
    private static int currentOffset = 0;

    private static synchronized Gson getParser() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private static volatile DialogManager instance = null;

    public static DialogManager getInstance(){
        DialogManager localInstance = instance;
        if (localInstance == null) {
            synchronized (Dates.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DialogManager();
                }
            }
        }
        return localInstance;
    }

    public void loadDialogs() {
        if (loading) throw new IllegalStateException("can't start second loading");

        loading = true;
        VKRequest chatRequest = new VKRequest("execute.getChats", VKParameters.from("offset", String.valueOf(currentOffset)));
        chatRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override public void onComplete(final VKResponse response) {
                loading = false;

                Threads.worker.postRunnable(new Runnable() {
                    @Override public void run() {
                        String json = response.responseString.replaceFirst("\"dialogs\":\\[\\d*,", "\"dialogs\": [");
                        final VkChatResponse chatResponse = getParser().fromJson(json, VkChatResponse.class);
                        if (chatResponse.getDialogs().size() < MAX_DIALOG_COUNT) {
                            isEndReached = true;
                        } else {
                            currentOffset += chatResponse.getDialogs().size();
                        }
                        Users.getInstance().populate(chatResponse.getProfiles());
                        App.applicationHandler.post(new Runnable() {
                            @Override public void run() {
                                EventBus.getDefault().postSticky(new OnLoadFinished(chatResponse.getChats()));
                            }
                        });
                    }
                });
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

    public void resetState() {
        currentOffset = 0;
        isEndReached = false;
    }
}
