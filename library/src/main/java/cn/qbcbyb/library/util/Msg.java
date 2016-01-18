package cn.qbcbyb.library.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.qbcbyb.library.view.ProgressDialogCustom;

public class Msg {

    public enum Which {
        /**
         * 正，OK
         */
        POSITIVE(DialogInterface.BUTTON_POSITIVE),
        /**
         * 负，CANCEL
         */
        NEGATIVE(DialogInterface.BUTTON_NEGATIVE),
        /**
         * 中性
         */
        NEUTRAL(DialogInterface.BUTTON_NEUTRAL);
        final int value;

        private Which(int value) {
            this.value = value;
        }

        public static Which valueOf(int value) { // 从int到enum的转换函数
            for (Which which : Which.values()) {
                if (value == which.value()) {
                    return which;
                }
            }
            return null;
        }

        public int value() {
            return this.value;
        }
    }

    public interface MsgCallback {
        public void callback(DialogInterface dialog, Which which);
    }

    public interface MsgViewCallback {
        public boolean callback(DialogInterface dialog, Which which);
    }

    public interface MsgSingleCallback {
        public void callback(DialogInterface dialog, Which which, Object listItem, Integer index);
    }

    public interface MsgMultiCallback {
        public void callback(DialogInterface dialog, Which which, Object[] listItem, Integer[] indexs);
    }

    public static final int OK = android.R.string.ok;
    public static final int CANCEL = android.R.string.cancel;

    public static void showInfo(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showInfo(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog confirm(Context context, String title, String msg, MsgCallback callback) {
        return confirm(context, title, msg, context.getString(OK), context.getString(CANCEL), callback);
    }

    public static AlertDialog confirm(Context context, String title, String msg, String ok, String cancel,
                                      final MsgCallback callback) {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    callback.callback(dialog, Which.valueOf(which));
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(msg).setTitle(title);
        builder.setPositiveButton(ok, onClickListener);
        builder.setNegativeButton(cancel, onClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog showViewDialog(Context context, String title, View view, MsgViewCallback callback) {
        return showViewDialog(context, title, view, context.getString(OK), context.getString(CANCEL), callback);
    }

    public static AlertDialog showViewDialog(Context context, String title, View view, String ok, String cancel, final MsgViewCallback callback) {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        //设置mShowing值，欺骗android系统
                        field.set(dialog, !callback.callback(dialog, Which.valueOf(which)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title).setView(view);
        if (StringUtils.isNotEmpty(ok)) {
            builder.setPositiveButton(ok, onClickListener);
        }
        if (StringUtils.isNotEmpty(cancel)) {
            builder.setNegativeButton(cancel, onClickListener);
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static Dialog showProgressDialog(Context context, String message) {
        Dialog dialog = new ProgressDialogCustom(context, message);
        dialog.show();
        dialog.setCancelable(true);
        return dialog;
    }

    public static AlertDialog showSingleChoiceDialog(Context context, String title, int itemsId, int selected, final MsgSingleCallback callback) {
        return showSingleChoiceDialog(context, title, itemsId, selected, context.getString(CANCEL), callback);
    }

    public static AlertDialog showSingleChoiceDialog(Context context, String title, int itemsId, int selected, String cancel, final MsgSingleCallback callback) {
        return showSingleChoiceDialog(context, title, context.getResources().getStringArray(itemsId), selected, cancel, callback);
    }

    public static AlertDialog showSingleChoiceDialog(Context context, String title, CharSequence[] items, int selected, final MsgSingleCallback callback) {
        return showSingleChoiceDialog(context, title, items, selected, context.getString(CANCEL), callback);
    }

    public static AlertDialog showSingleChoiceDialog(Context context, String title, final CharSequence[] items, int selected, String cancel, final MsgSingleCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title);
        OnClickListener singleClickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    callback.callback(dialog, Which.valueOf(which), items, which);
                }
                dialog.dismiss();
            }
        };
        builder.setNegativeButton(cancel, singleClickListener);
        builder.setSingleChoiceItems(items, selected, singleClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }


    public static AlertDialog showMultiChoiceDialog(Context context, String title, int itemsId, boolean[] checked, final MsgMultiCallback callback) {
        return showMultiChoiceDialog(context, title, itemsId, checked, context.getString(OK), context.getString(CANCEL), callback);
    }

    public static AlertDialog showMultiChoiceDialog(Context context, String title, int itemsId, boolean[] checked, String ok, String cancel, final MsgMultiCallback callback) {
        return showMultiChoiceDialog(context, title, context.getResources().getStringArray(itemsId), checked, ok, cancel, callback);
    }

    public static AlertDialog showMultiChoiceDialog(Context context, String title, CharSequence[] items, boolean[] checked, final MsgMultiCallback callback) {
        return showMultiChoiceDialog(context, title, items, checked, context.getString(OK), context.getString(CANCEL), callback);
    }

    public static AlertDialog showMultiChoiceDialog(Context context, String title, final CharSequence[] items, boolean[] checked, String ok, String cancel, final MsgMultiCallback callback) {
        final List<Integer> selected = new ArrayList<Integer>();
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callback != null) {
                    callback.callback(dialog, Which.valueOf(which), items, selected.toArray(new Integer[]{}));
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title);
        builder.setPositiveButton(ok, onClickListener);
        builder.setNegativeButton(cancel, onClickListener);
        builder.setMultiChoiceItems(items, checked, new OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                if (!arg2 && selected.contains(arg1)) {
                    selected.remove(arg1);
                } else if (arg2 && !selected.contains(arg1)) {
                    selected.add(arg1);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

}
