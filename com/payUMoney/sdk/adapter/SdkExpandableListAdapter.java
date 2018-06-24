package com.payUMoney.sdk.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.payUMoney.sdk.C0360R;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.fragment.SdkDebit;
import com.payUMoney.sdk.fragment.SdkNetBankingFragment;
import com.payUMoney.sdk.fragment.SdkStoredCardFragment;
import java.util.HashMap;
import java.util.List;

public class SdkExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private HashMap<String, Fragment> _listDataChild = null;
    private List<String> _listDataHeader;

    public SdkExpandableListAdapter(Context context, List<String> listDataHeader) {
        this._context = context;
        this._listDataHeader = listDataHeader;
    }

    public Fragment getChild(int groupPosition, int childPosititon) {
        return null;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService("layout_inflater");
        if (((String) this._listDataHeader.get(groupPosition)).equals(SdkConstants.PAYMENT_MODE_STORE_CARDS)) {
            return new SdkStoredCardFragment(this._context).onCreateView(infalInflater, parent);
        }
        if (((String) this._listDataHeader.get(groupPosition)).equals(SdkConstants.PAYMENT_MODE_CC)) {
            return new SdkDebit(this._context).onCreateView(infalInflater, parent, SdkConstants.PAYMENT_MODE_CC);
        }
        if (((String) this._listDataHeader.get(groupPosition)).equals(SdkConstants.PAYMENT_MODE_DC)) {
            return new SdkDebit(this._context).onCreateView(infalInflater, parent, SdkConstants.PAYMENT_MODE_DC);
        }
        if (((String) this._listDataHeader.get(groupPosition)).equals(SdkConstants.PAYMENT_MODE_NB)) {
            return new SdkNetBankingFragment(this._context).onCreateView(infalInflater, parent);
        }
        return null;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    public Object getGroup(int groupPosition) {
        if (this._listDataHeader == null || this._listDataHeader.size() <= groupPosition) {
            return null;
        }
        return this._listDataHeader.get(groupPosition);
    }

    public int getGroupCount() {
        if (this._listDataHeader != null) {
            return this._listDataHeader.size();
        }
        return 0;
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Object object = getGroup(groupPosition);
        String headerTitle = "";
        if (object != null) {
            String titleGroup = object.toString();
            if (titleGroup != null) {
                int i = -1;
                switch (titleGroup.hashCode()) {
                    case -318970969:
                        if (titleGroup.equals(SdkConstants.PAYMENT_MODE_STORE_CARDS)) {
                            i = 0;
                            break;
                        }
                        break;
                    case 2144:
                        if (titleGroup.equals(SdkConstants.PAYMENT_MODE_CC)) {
                            i = 1;
                            break;
                        }
                        break;
                    case 2175:
                        if (titleGroup.equals(SdkConstants.PAYMENT_MODE_DC)) {
                            i = 2;
                            break;
                        }
                        break;
                    case 2484:
                        if (titleGroup.equals(SdkConstants.PAYMENT_MODE_NB)) {
                            i = 3;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                        headerTitle = "Saved Cards";
                        break;
                    case 1:
                        headerTitle = "Credit Card";
                        break;
                    case 2:
                        headerTitle = "Debit Card";
                        break;
                    case 3:
                        headerTitle = "Net Banking";
                        break;
                }
            }
        }
        if (convertView == null) {
            convertView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(C0360R.layout.sdk_list_group, null);
        }
        if (!(headerTitle == null || headerTitle.isEmpty())) {
            TextView lblListHeader = (TextView) convertView.findViewById(C0360R.id.lblListHeader);
            lblListHeader.setTypeface(null, 1);
            lblListHeader.setText(headerTitle);
        }
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }
}
