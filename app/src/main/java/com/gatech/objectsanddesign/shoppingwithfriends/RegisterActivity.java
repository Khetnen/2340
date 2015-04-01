package com.gatech.objectsanddesign.shoppingwithfriends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for the registration screen
 */

public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
        private Firebase ref;
        private EditText mEmailView;
        private EditText mPasswordView;
        private EditText mFirstName;
        private EditText mLastName;
        private Button mRegister;

        public PlaceholderFragment() {
        }


        public void attemptRegister() {
            // Reset errors.
            mEmailView.setError(null);
            mPasswordView.setError(null);
            mFirstName.setError(null);
            mPasswordView.setError(null);

            // Store values at the time of the login attempt.
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();
            String first = mFirstName.getText().toString();
            String last = mLastName.getText().toString();

            boolean cancel = false;
            View focusView = null;


            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.error_field_required));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid email address.
            if (TextUtils.isEmpty(email)) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                attemptRegisterAuth(email, password, first, last);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register, container, false);
            mEmailView = (EditText) rootView.findViewById(R.id.email_input);
            mPasswordView = (EditText) rootView.findViewById(R.id.password_input);
            mRegister = (Button) rootView.findViewById(R.id.register_button);

            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_GO || id == EditorInfo.IME_NULL) {
                        attemptRegister();
                        return true;
                    }
                    return false;
                }
            });

            mFirstName = (EditText) rootView.findViewById(R.id.first_name_input);
            mLastName = (EditText) rootView.findViewById(R.id.last_name_input);

            Firebase.setAndroidContext(getActivity());
            ref = FirebaseInterfacer.interfacer.getRef();
            mRegister.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    attemptRegister();
                }
            });
            return rootView;
        }

        public void attemptRegisterAuth(final String email, final String password, final String first, final String last) {

            ref.createUser(email, password, new Firebase.ResultHandler() {

                @Override
                public void onSuccess() {
                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler(){

                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Map<String, String> map = new HashMap<>();
                            map.put("provider", authData.getProvider());
                            if(authData.getProviderData().containsKey("id")){
                                map.put("provider_id", authData.getProviderData().get("id").toString());
                            }
                            if(authData.getProviderData().containsKey("displayName")) {
                                map.put("displayName", authData.getProviderData().get("displayName").toString());
                            }
                            map.put("firstName",first);
                            map.put("email", email);
                            map.put("lastName", last);
                            ref.child("users").child(authData.getUid()).setValue(map);
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                        }
                    });
                    getActivity().finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    if (isAdded()) {
                        if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                            mEmailView.setError(getString(R.string.error_account_exists));
                        } else {
                            mEmailView.setError(firebaseError.getMessage());
                        }
                    }
                }
            });
        }
    }
}
