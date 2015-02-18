package com.gatech.objectsanddesign.shoppingwithfriends;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class FriendList extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        super.onCreateDrawer();
        Firebase.setAndroidContext(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ListView mFriendsList;
        private ArrayAdapter<User> friendsAdapter;
        Firebase ref;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friend_list, container, false);
            mFriendsList = (ListView) rootView.findViewById(R.id.friend_list);
            friendsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getFriends());
            mFriendsList.setAdapter(friendsAdapter);
            return rootView;
        }

        private ArrayList<User> getFriends() {
            final ArrayList<User> friends = new ArrayList<>();
            ref = new Firebase("https://2340.firebaseio.com/users");
            AuthData auth = ref.getAuth();
            final Query query = ref.child(auth.getUid()).child("friends");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> friendsMap = (Map) dataSnapshot.getValue();
                    for(final String friendID : friendsMap.values()){
                        Query friendQuery = ref.child(friendID);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> friend = (Map) dataSnapshot.getValue();
                                friends.add(new ConcreteUser(
                                        (String) friend.get("firstName"),
                                        (String) friend.get("lastName"),
                                        friendID));
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return friends;
        }
    }
}