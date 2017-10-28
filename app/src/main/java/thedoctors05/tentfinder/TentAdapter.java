package thedoctors05.tentfinder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gregory on 2017-10-26.
 */

public class TentAdapter extends ArrayAdapter<Tent> {

    Context context;
    int layoutResourceId;
    ArrayList<Tent> data = null;

    public TentAdapter(@NonNull Context context, @LayoutRes int layoutResourceId, @NonNull ArrayList<Tent> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowBeanHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RowBeanHolder();
            holder.tvTitleRow = (TextView) row.findViewById(R.id.tvTitleRow);
            holder.tvLongitudeRow = (TextView) row.findViewById(R.id.tvLongitudeRow);
            holder.tvLatitudeRow = (TextView) row.findViewById(R.id.tvLatitudeRow);

            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
        Tent object = data.get(position);
        holder.tvTitleRow.setText(object.Title);
        holder.tvLongitudeRow.setText(object.Longitude);
        holder.tvLatitudeRow.setText(object.Latitude);

        return  row;
    }

    static class RowBeanHolder {
        TextView tvTitleRow;
        TextView tvLongitudeRow;
        TextView tvLatitudeRow;
    }
}
