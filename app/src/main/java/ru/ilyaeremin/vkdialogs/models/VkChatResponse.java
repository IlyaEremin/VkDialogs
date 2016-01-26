package ru.ilyaeremin.vkdialogs.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.ilyaeremin.vkdialogs.utils.Randoms;

/**
 * Created by Ilya Eremin on 24.01.2016.
 */
public class VkChatResponse {
    RootLevelResponse response;

    private class RootLevelResponse {
        List<User> profiles;
        List<Chat> dialogs;
    }

    public List<User> getProfiles() {
        return response.profiles;
    }

    public List<Chat> getChats() {
        List<Chat> chatsOnly = new ArrayList<>();
        for (Chat chat : response.dialogs) {
            if (chat.isChat() && !chat.isDeleted()) {
                if (!chat.hasPhoto()) {
                    pickRandomAvatarsForChatPic(chat);
                }
                chatsOnly.add(chat);
            }
        }
        return chatsOnly;
    }

    public List<Chat> getDialogs(){
        return response.dialogs;
    }

    private void pickRandomAvatarsForChatPic(Chat chat) {
        int[] userIds = chat.getUserIds();
        int userPicsCount = Math.min(4, userIds.length);
        chat.userPics = new String[userPicsCount];
        if (userIds.length <= 4) {
            for (int i = 0; i < userIds.length; i++) {
                chat.userPics[i] = Users.getInstance().getPhotoForId(userIds[i]);
            }
        } else {
            int[] randomPics = Randoms.getNRandomNumberWithinRange(userPicsCount, userIds.length);
            for (int i = 0; i < userPicsCount; i++) {
                try {
                    chat.userPics[i] = Users.getInstance().getPhotoForId(userIds[randomPics[i]]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    String randomPicsStr = "";
                    for (int randomPic : randomPics) {
                        randomPicsStr += randomPic + ",";
                    }
                    String userIdsStr = "";
                    for (int userId : userIds) {
                        userIdsStr += userId + ",";
                    }
                    Log.d("VKDialogs", "randomPics: [" + randomPicsStr + "], userIds: [" + userIdsStr + "]"
                        + "userPicscount: " + userPicsCount + ", userCountWithoutMe: " + userIds.length);
                    throw e;
                }

            }
        }
    }

}
