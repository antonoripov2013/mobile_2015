package zerodegrees.prj_04ver20;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DialogListAdapter extends BaseAdapter {
    JSONArray mDialog;
    Context mContext;
    DBHelper dbHelper;
    long user_id;

    public DialogListAdapter(Context context, JSONArray dialog, long userId) {
        mDialog = dialog;
        mContext = context;
        user_id = userId;
    }

    @Override
    public int getCount() {
        return mDialog.length();
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return mDialog.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        try {
            return mDialog.getJSONObject(position).getLong("uid");
        } catch (JSONException e) {
            Log.d("LOG", "POSITION " + position);
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        long uid = 0;
        String body = "";
        try {
            body = mDialog.getJSONObject(position).getString("body");
            uid = mDialog.getJSONObject(position).getLong("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dbHelper = new DBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_id", user_id);
        cv.put("body", body);
        cv.put("uid", uid);
        db.insert("dialogs", null, cv);
        dbHelper.close();
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.dialog_list_item, parent, false);
        TextView dialog_textView = (TextView) rowView.findViewById(R.id.dialog_textView);
        dialog_textView.setText(body);
        TextView dialog_textView_date = (TextView) rowView.findViewById(R.id.dialog_textView_date);
        dialog_textView_date.setText("uid " + uid);
        return rowView;
    }
}
