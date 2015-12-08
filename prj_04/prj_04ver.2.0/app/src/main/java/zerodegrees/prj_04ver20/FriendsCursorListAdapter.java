package zerodegrees.prj_04ver20;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FriendsCursorListAdapter extends SimpleCursorAdapter {
    Context mContext;
    Cursor cursor;

    public FriendsCursorListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.cursor = c;
        mContext=context;
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex("user_id"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        cursor.moveToPosition(position);
        String first_name = cursor.getString(cursor.getColumnIndex("first_name"));
        String last_name = cursor.getString(cursor.getColumnIndex("last_name"));
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.friends_list_item, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.text1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.text2);
        textView1.setText(first_name);
        textView2.setText(last_name);
        return rowView;
    }
}
