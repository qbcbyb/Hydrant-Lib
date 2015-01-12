package cn.qbcbyb.library.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by 秋云 on 2014/9/17.
 */
public class PopupMenu implements DialogInterface.OnClickListener {
    public static interface OnSelectMenu {

        void selectMenu(MenuItem menuItem);
    }

    private Context context;

    private AlertDialog listPopupWindow;
    private OnSelectMenu onSelectMenu;

    public PopupMenu(Context context, int menuResId) {
        this.context = context;
        listPopupWindow = new AlertDialog.Builder(context).setAdapter(new MenuAdapter(menuResId), this)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        MenuItem menuItem = (MenuItem) listPopupWindow.getListView().getAdapter().getItem(which);
        if (onSelectMenu != null) {
            onSelectMenu.selectMenu(menuItem);
        }
    }

    public OnSelectMenu getOnSelectMenu() {
        return onSelectMenu;
    }

    public void setOnSelectMenu(OnSelectMenu onSelectMenu) {
        this.onSelectMenu = onSelectMenu;
    }

    public void dismiss() {
        listPopupWindow.dismiss();
    }

    public void show() {
        listPopupWindow.show();
    }

    class MenuAdapter extends BaseAdapter {
        private MenuBuilder menuBuilder;

        MenuAdapter(int menuRes) {
            menuBuilder = new MenuBuilder(context);
            new MenuInflater(context).inflate(menuRes, menuBuilder);
        }

        @Override
        public int getCount() {
            return menuBuilder.size();
        }

        @Override
        public Object getItem(int position) {
            return menuBuilder.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return menuBuilder.getItem(position).getItemId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView textView = new TextView(context);
                textView.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
                textView.setPadding(10, 10, 10, 10);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setTextSize(16);
                textView.setCompoundDrawablePadding(10);
                convertView = textView;
            }
            TextView textView = ((TextView) convertView);
            MenuItem item = menuBuilder.getItem(position);
            textView.setText(item.getTitle());
            textView.setCompoundDrawablesWithIntrinsicBounds(item.getIcon(), null, null, null);
            return convertView;
        }
    }
}
