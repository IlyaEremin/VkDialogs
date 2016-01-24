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
        List<User>   profiles;
        List<Dialog> dialogs;
    }

    public List<User> getProfiles() {
        return response.profiles;
    }

    public List<Dialog> getChats() {
        List<Dialog> chatsOnly = new ArrayList<>();
        for (Dialog dialog : response.dialogs) {
            if (dialog.isChat() && !dialog.isDeleted()) {
                if (!dialog.hasPhoto()) {
                    pickRandomAvatarsForChatPic(dialog);
                }
                chatsOnly.add(dialog);
            }
        }
        return chatsOnly;
    }

    public List<Dialog> getDialogs(){
        return response.dialogs;
    }


    private void pickRandomAvatarsForChatPic(Dialog dialog) {
        int[] userIds = dialog.getUserIds();
        int userPicsCount = Math.min(4, userIds.length);
        dialog.userPics = new String[userPicsCount];
        if (userIds.length <= 4) {
            for (int i = 0; i < userIds.length; i++) {
                dialog.userPics[i] = Users.getInstance().getPhotoForId(userIds[i]);
            }
        } else {
            int[] randomPics = Randoms.getNRandomNumberWithinRange(userPicsCount, userIds.length);
            for (int i = 0; i < userPicsCount; i++) {
                try {
                    dialog.userPics[i] = Users.getInstance().getPhotoForId(userIds[randomPics[i]]);
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