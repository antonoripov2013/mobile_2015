package zerodegrees.prj_04ver20;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsListAdapter extends BaseAdapter {
    JSONArray mFriends;
    Context mContext;

    public FriendsListAdapter(Context context, JSONArray friends) {
        mFriends = friends;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mFriends.length();
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return mFriends.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        try {
            return mFriends.getJSONObject(position).getLong("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        long user_id = 0;
        String first_name = "";
        String last_name = "";
        try {
            user_id = mFriends.getJSONObject(position).getLong("user_id");
            first_name = mFriends.getJSONObject(position).getString("first_name");
            last_name = mFriends.getJSONObject(position).getString("last_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_id", user_id);
        cv.put("first_name", first_name);
        cv.put("last_name", last_name);
        db.insert("friends", null, cv);
        dbHelper.close();
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
