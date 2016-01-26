package ru.ilyaeremin.vkdialogs;

import java.util.List;

import ru.ilyaeremin.vkdialogs.models.Chat;

/**
 * Created by Ilya Eremin on 26.01.2016.
 */
public class OnLoadFinished {
    private final List<Chat> chats;
    public final Code code;

    public OnLoadFinished(List<Chat> chats) {
        this.chats = chats;
        this.code = Code.SUCCESS;
    }

    public OnLoadFinished(Code code) {
        this.code = code;
        this.chats = null;
    }

    public List<Chat> getChats() {
        return chats;
    }
}
