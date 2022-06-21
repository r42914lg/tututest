package com.r42914lg.tutu.model;

/**
 * Wraps title & text for a dialog. Used as a value of LiveData in ViewModel
 * to notify UI that dialog with given title & text should be shown
 */
public class TerminateDialogText {
    private final String title;
    private final String text;

    public TerminateDialogText(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }
    public String getText() {
        return text;
    }
}
