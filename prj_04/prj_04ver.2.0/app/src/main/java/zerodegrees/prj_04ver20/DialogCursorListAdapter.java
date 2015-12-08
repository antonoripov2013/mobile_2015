package zerodegrees.prj_04ver20;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DialogCursorListAdapter extends SimpleCursorAdapter {
    Context mContext;
    Cursor cursor;
    long user_id;

    public DialogCursorListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, long user_id) {
        super(context, layout, c, from, to, flags);
        mContext = context;
        this.cursor = c;
        this.user_id = user_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        cursor.moveToPosition(position);
        long date = cursor.getLong(cursor.getColumnIndex("uid"));
        String body = cursor.getString(cursor.getColumnIndex("body"));
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.dialog_list_item, parent, false);
        TextView dialog_textView = (TextView) rowView.findViewById(R.id.dialog_textView);
        dialog_textView.setText(body);
        TextView dialog_textView_date = (TextView) rowView.findViewById(R.id.dialog_textView_date);
        dialog_textView_date.setText("Date " + date);
        return rowView;
    }
}
