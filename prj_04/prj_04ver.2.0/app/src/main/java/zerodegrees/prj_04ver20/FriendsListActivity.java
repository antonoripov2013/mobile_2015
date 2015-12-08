package zerodegrees.prj_04ver20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class FriendsListActivity extends Activity {
    public static final int ACCESS_TOKEN_REQEST = 1;

    ListView listView;
    FriendsListAdapter listViewAdapter;
    FriendsCursorListAdapter friendsCursorListAdapter;
    VK vk;
    DialogListAdapter dialogListAdapter;
    DialogCursorListAdapter dialogCursorListAdapter;
    long user_id;
    boolean isTouch = false;
    boolean isHasInternet = false;
    DBHelper dbHelper;
    AsyncTask<Void, Void, JSONArray> updateFriendsList = new AsyncTask<Void, Void, JSONArray>() {
        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                JSONArray friends = vk.callFunction("friends.get", "fields=nickname")
                        .getJSONArray("response");
                return friends;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray friends) {
            super.onPostExecute(friends);
            listViewAdapter = new FriendsListAdapter(FriendsListActivity.this, friends);
            listView.setAdapter(listViewAdapter);
        }
    };

    AsyncTask<Void, Void, JSONArray> updateDialogList = new AsyncTask<Void, Void, JSONArray>() {
        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                JSONArray friends = vk.callFunction("messages.getHistory", "user_id=" + user_id)
                        .getJSONArray("response");
                return friends;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray dialog) {
            super.onPostExecute(dialog);
            dialogListAdapter = new DialogListAdapter(FriendsListActivity.this, dialog, user_id);
            listView.setAdapter(dialogListAdapter);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbHelper = new DBHelper(FriendsListActivity.this);
        listView = (ListView) findViewById(R.id.listView);
        isHasInternet = isOnline();
        Log.d("LOG", "hasConnection " + isHasInternet);
        if (isHasInternet) {
            startActivityForResult(
                    new Intent(this, AuthActivity.class),
                    ACCESS_TOKEN_REQEST);
        } else {
            SQLiteDatabase dbt = dbHelper.getReadableDatabase();
            Cursor cursor = dbt.query("friends", null, null, null, null, null, null);
            friendsCursorListAdapter = new FriendsCursorListAdapter(
                    FriendsListActivity.this,
                    R.layout.friends_list_item,
                    cursor,
                    new String[]{"first_name", "last_name"},
                    new int[]{R.id.text1, R.id.text2},
                    0);
            listView.setAdapter(friendsCursorListAdapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FriendsListActivity.this, "" + id, Toast.LENGTH_SHORT).show();
                if (!isTouch) {
                    user_id = id;
                    isTouch = true;
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.query("dialogs", null, "user_id = ?", new String[]{user_id + ""}, null, null, null);
                    dialogCursorListAdapter = new DialogCursorListAdapter(
                            FriendsListActivity.this,
                            R.layout.dialog_list_item,
                            cursor,
                            new String[]{"body", "uid"},
                            new int[]{R.id.dialog_textView, R.id.dialog_textView_date},
                            0,
                            user_id);
                    listView.setAdapter(dialogCursorListAdapter);
                    if (isHasInternet) {
                        updateDialogList.execute();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ACCESS_TOKEN_REQEST:
                if (resultCode == AuthActivity.ACCESS_TOKEN_TAKEN) {
                    vk = new VK(data.getStringExtra("access_token"));
                    updateFriendsList.execute();
                }
                break;
        }
    }

    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
}
