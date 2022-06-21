package com.r42914lg.tutu.ui;

import com.r42914lg.tutu.model.TerminateDialogText;

/**
 * This interface defines what Main Activity should render on UI
 */
public interface ICoreView {
    void showNetworkStatus(String text);
    void showFabIcon(boolean flag);
    void showToast(String msg);
    void startProgressOverlay();
    void stopProgressOverlay();
    void showTerminateDialog(TerminateDialogText terminateDialogText);
}
