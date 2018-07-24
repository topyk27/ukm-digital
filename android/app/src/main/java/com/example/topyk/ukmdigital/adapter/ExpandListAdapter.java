package com.example.topyk.ukmdigital.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.example.topyk.ukmdigital.R;
import com.example.topyk.ukmdigital.kelas.Parent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by topyk on 7/12/2017.
 */

public class ExpandListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private ArrayList<Parent> _Parents;
    private LayoutInflater _Inflater;
    private static ExpandableListAdapter adapter = new ExpandListAdapter();
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandListAdapter(){

    }

    public ExpandListAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List childList = this._listDataChild.get(this._listDataHeader.get(groupPosition));
        if (childList != null && !childList.isEmpty()){
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void prepareListData(List<String> listDataHeader, Map<String,
                List<String>> listDataChild) {


        // Adding child data
        listDataHeader.add("Dashboard");
        listDataHeader.add("Toko");
        listDataHeader.add("Transaksi");
        listDataHeader.add("Login");
        listDataHeader.add("Logout");
        listDataHeader.add("Daftar Kampung");
        listDataHeader.add("About");

        // Adding child data
        List<String> dashboard = new ArrayList<String>();
        dashboard.add("sembunyikan");

        List<String> daftarKampung = new ArrayList<String>();
        daftarKampung.add("Citra Niaga");
        daftarKampung.add("Kampung Tenun");
        daftarKampung.add("Kampung Wadai");



        List<String> daftarAbout = new ArrayList<String>();
        daftarAbout.add("UKM Digital");
        daftarAbout.add("Universitas Mulawarman");





        listDataChild.put(listDataHeader.get(0), dashboard);
        listDataChild.put(listDataHeader.get(5), daftarKampung); // Header, Child data
        listDataChild.put(listDataHeader.get(6), daftarAbout);

    }
}
