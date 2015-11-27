package org.porks.arctouch.floripaomnibus.actdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.porks.arctouch.floripaomnibus.R;
import org.porks.arctouch.floripaomnibus.models.DepartureHour;

/**
 * Implements Adapter for the ExpandableListView using the bus' Departures
 */
class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final DepartureHour[] arrayDepartureHours;

    public ExpandableListAdapter(DepartureHour[] arrayDepartureHours) {
        this.arrayDepartureHours = arrayDepartureHours;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.arrayDepartureHours[groupPosition].getListDeparture().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_departure_child, parent);
        }

        final String children = (String) this.getChild(groupPosition, childPosition);
        TextView textView = (TextView) convertView.findViewById(R.id.ListViewDepartureChild_Text1);
        textView.setText(children);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.arrayDepartureHours[groupPosition].getListDeparture().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.arrayDepartureHours[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return this.arrayDepartureHours.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_departure_group, parent);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.ListViewDepartureGroup_Text1);
        textView.setText("At " + this.arrayDepartureHours[groupPosition].getHourName() + " Hours (" + this.arrayDepartureHours[groupPosition].getListDeparture().size() + " Departures)");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
