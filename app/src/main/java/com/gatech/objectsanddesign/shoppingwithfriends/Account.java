package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;


public class Account extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_account);
        super.onCreateDrawer();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
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

        EditText mOldPassword;
        EditText mNewPassword;
        Button mChangePasswordButton;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_account, container, false);
            mOldPassword = (EditText) rootView.findViewById(R.id.old_password);
            mNewPassword = (EditText) rootView.findViewById(R.id.new_password);
            mChangePasswordButton = (Button) rootView.findViewById(R.id.change_password_button);

            mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOldPassword.setError(null);
                    mNewPassword.setError(null);
                    if (TextUtils.isEmpty(mOldPassword.getText().toString())) {
                        mOldPassword.setError("Please enter old password");
                    } else if (TextUtils.isEmpty(mNewPassword.getText().toString())) {
                        mOldPassword.setError("Please enter new password");
                    } else {
                        FirebaseInterfacer.interfacer.changePassword(
                                mOldPassword.getText().toString(),
                                mNewPassword.getText().toString()
                        );

                        Toast.makeText(getActivity(),
                                "Password changed. Please login with your new password",
                                Toast.LENGTH_SHORT).show();

                        getActivity().finish();
                        Intent i = new Intent(getActivity(), ApplicationScreen.class);
                        startActivity(i);
                    }
                }
            });
            return rootView;
        }
    }
}
