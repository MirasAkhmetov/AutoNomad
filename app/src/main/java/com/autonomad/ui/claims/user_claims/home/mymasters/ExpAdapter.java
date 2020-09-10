package com.autonomad.ui.claims.user_claims.home.mymasters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpAdapter extends BaseExpandableListAdapter {

    public class Group {
        String name;
        private List<Child> childList;

        void setChildList(List<Child> list) {
            childList = list;
        }
        List<Child> getChildList() {
            return childList;
        }
    }

    public class Child {
        String name;
        String comment;
    }

    private final class ViewHolder {
        TextView textLabel;
    }

    private final List<Group> itemList;
    private final LayoutInflater inflater;

    private ExpAdapter(Context context, List<Group> itemList) {
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    @Override
    public Child getChild(int groupPosition, int childPosition) {

        return itemList.get(groupPosition).getChildList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemList.get(groupPosition).getChildList().size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             final ViewGroup parent) {
        View resultView = convertView;
        ViewHolder holder;

        if (resultView == null) {
            resultView = inflater.inflate(android.R.layout.simple_list_item_1, null); //TODO change layout id
            holder = new ViewHolder();
            holder.textLabel = resultView.findViewById(android.R.id.text1); //TODO change view id
            resultView.setTag(holder);
        } else {
            holder = (ViewHolder) resultView.getTag();
        }

        final Child item = getChild(groupPosition, childPosition);

        holder.textLabel.setText(item.name);

        return resultView;
    }

    @Override
    public Group getGroup(int groupPosition) {
        return itemList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return itemList.size();
    }

    @Override
    public long getGroupId(final int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View theConvertView, ViewGroup parent) {
        View resultView = theConvertView;
        ViewHolder holder;

        if (resultView == null) {
            resultView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            holder = new ViewHolder();
            holder.textLabel = resultView.findViewById(android.R.id.text1);
            resultView.setTag(holder);
        } else {
            holder = (ViewHolder) resultView.getTag();
        }

        final Group item = getGroup(groupPosition);

        holder.textLabel.setText(item.name);

        return resultView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
