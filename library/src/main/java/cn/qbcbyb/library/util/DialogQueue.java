package cn.qbcbyb.library.util;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DialogQueue {

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            nowShowingDialog = null;
            showNext();
        }
    };
    private static Queue<Dialog> showingDialogList = new LinkedBlockingQueue<Dialog>();
    private static Dialog nowShowingDialog = null;

    public static void showDialog(Dialog dialog) {
        Message msg = handler.obtainMessage();
        dialog.setCancelMessage(msg);
        dialog.setDismissMessage(msg);
        showingDialogList.offer(dialog);
        showNext();
    }

    private static void showNext() {
        if (nowShowingDialog == null) {
            if (showingDialogList.size() > 0) {
                nowShowingDialog = showingDialogList.poll();
                if (nowShowingDialog != null) {
                    nowShowingDialog.show();
                }
            }
        }
    }

}
