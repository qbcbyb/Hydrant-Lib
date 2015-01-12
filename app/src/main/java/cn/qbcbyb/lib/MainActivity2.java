package cn.qbcbyb.lib;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ryg.expandable.ui.PinnedHeaderExpandableListView;
import com.woozzu.android.widget.IndexableGroupListView;

import java.util.ArrayList;
import java.util.List;

import cn.qbcbyb.library.adapter.ModelGroupAdapter;
import cn.qbcbyb.library.model.BaseModel;
import cn.qbcbyb.library.model.BaseParentModel;


public class MainActivity2 extends ActionBarActivity implements ExpandableListView.OnGroupClickListener, PinnedHeaderExpandableListView.OnHeaderUpdateListener {

    IndexableGroupListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        listview = (IndexableGroupListView) findViewById(R.id.listview);
        final ArrayList<People> peoples = new ArrayList<People>() {
            {
                add(new People("0"));
                add(new People("1"));
                add(new People("2"));
                add(new People("3"));
                add(new People("4"));
                add(new People("5"));
                add(new People("6"));
                add(new People("7"));
                add(new People("8"));
                add(new People("9"));
            }
        };
        final ArrayList<Group> groupData = new ArrayList<Group>() {
            {
                add(new Group("a", peoples));
                add(new Group("b", peoples));
                add(new Group("c", peoples));
                add(new Group("d", peoples));
                add(new Group("e", peoples));
                add(new Group("f", peoples));
                add(new Group("g", peoples));
                add(new Group("h", peoples));
                add(new Group("i", peoples));
                add(new Group("j", peoples));
                add(new Group("k", peoples));
                add(new Group("l", peoples));
                add(new Group("m", peoples));
                add(new Group("n", peoples));
                add(new Group("o", peoples));
                add(new Group("x", peoples));
                add(new Group("z", peoples));
            }
        };
        GroupAdapter adapter = new GroupAdapter(this, groupData);
        listview.setAdapter(adapter);
        listview.setFastScrollEnabled(true);
        for (int i = 0, count = listview.getCount(); i < count; i++) {
            listview.expandGroup(i);
        }
        listview.setOnGroupClickListener(this);
        listview.setOnHeaderUpdateListener(this);
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {
        return false;
    }

    @Override
    public View getPinnedHeader() {
        return LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, listview, false);
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        Group firstVisibleGroup = (Group) listview.getExpandableListAdapter().getGroup(firstVisibleGroupPos);
        TextView textView = (TextView) headerView;
        textView.setText(firstVisibleGroup.getName());
    }

    class GroupAdapter extends ModelGroupAdapter implements SectionIndexer {

        public GroupAdapter(Context context, List<Group> groupData) {
            super(context, groupData, android.R.layout.simple_list_item_1,
                    new String[]{"name"},
                    new int[]{android.R.id.text1},
                    android.R.layout.simple_list_item_1,
                    new String[]{"name"},
                    new int[]{android.R.id.text1});
        }

        @Override
        public Object[] getSections() {
            List<String> sections = new ArrayList<String>();
            for (Object object : GroupAdapter.this.getData()) {
                Group group = ((Group) object);
                sections.add(group.getName());
            }
            return sections.toArray(new String[sections.size()]);
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            return listview.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(sectionIndex));
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }

    class People extends BaseModel {
        String name;

        People(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class Group extends BaseParentModel<People> {
        String name;

        Group(String name, List<People> peoples) {
            this.name = name;
            setChildren(peoples);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
