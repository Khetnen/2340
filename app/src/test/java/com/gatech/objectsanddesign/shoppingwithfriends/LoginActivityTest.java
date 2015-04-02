package com.gatech.objectsanddesign.shoppingwithfriends;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoginActivityTest {

    private LoginActivity activity;
    private AutoCompleteTextView mEmailView;
    private EditText mPassView;
    private Button mEmailSignInButton;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(LoginActivity.class)
                .create().get();
        Firebase ref = mock(Firebase.class);
        FirebaseInterfacer.interfacer.setRef(ref);
        assertEquals(ref, FirebaseInterfacer.interfacer.getRef());
        mEmailView = (AutoCompleteTextView) activity.findViewById(R.id.email);
        mPassView = (EditText) activity.findViewById(R.id.password);
        mEmailSignInButton = (Button) activity.findViewById(R.id.email_sign_in_button);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNotNull() throws Exception {
        assertNotNull(activity);
        assertNotNull(mEmailView);
        assertNotNull(mPassView);
        assertNotNull(mEmailSignInButton);
    }

    @Test
    public void testEmptyEmailPass() throws Exception {
        mEmailView.setText("");
        mPassView.setText("");
        mEmailSignInButton.performClick();
        assertEquals(activity.getString(R.string.error_field_required), mEmailView.getError());
        assertTrue(mEmailView.hasFocus());
    }

    @Test
    public void testEmptyPass() throws Exception {
        mEmailView.setText("test@test.com");
        mPassView.setText("");
        mEmailSignInButton.performClick();
        assertEquals(activity.getString(R.string.error_invalid_password), mPassView.getError());
        assertTrue(mPassView.hasFocus());
    }

    @Test
    public void testInvalidEmail() throws Exception {
        mEmailView.setText("foobar");
        mPassView.setText("");
        mEmailSignInButton.performClick();
        assertEquals(activity.getString(R.string.error_invalid_email), mEmailView.getError());
        assertTrue(mEmailView.hasFocus());
    }

    @Test
    public void testValidEmailPass() throws Exception {
        mEmailView.setText("test@test.com");
        mPassView.setText("testpassword");
        try{
            mEmailSignInButton.performClick();
        } catch (Exception ex) {
            //this is okay, we're not connected to the internet.
        }
        assertNull(mEmailView.getError());
        assertNull(mPassView.getError());
    }
}