package ru.ilyaeremin.vkdialogs;

import java.util.List;

import ru.ilyaeremin.vkdialogs.models.Dialog;

/**
 * Created by Ilya Eremin on 26.01.2016.
 */
public class OnLoadFinished {
    private final List<Dialog> dialogs;
    public final Code          code;

    public OnLoadFinished(List<Dialog> dialogs) {
        this.dialogs = dialogs;
        this.code = Code.SUCCESS;
    }

    public OnLoadFinished(Code code) {
        this.code = code;
        this.dialogs = null;
    }

    public List<Dialog> getDialogs() {
        return dialogs;
    }
}
