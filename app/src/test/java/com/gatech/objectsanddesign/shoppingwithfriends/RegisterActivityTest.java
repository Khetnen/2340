package com.gatech.objectsanddesign.shoppingwithfriends;

import android.support.v4.app.Fragment;
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
public class RegisterActivityTest {

    private RegisterActivity activity;
    private EditText mEmailView;
    private EditText mPassView;
    private EditText mFirstName;
    private EditText mLastName;
    private Button mRegisterButton;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(RegisterActivity.class).create().start().resume().get();
        activity.getSupportFragmentManager().executePendingTransactions();
        Fragment frag = activity.getSupportFragmentManager().findFragmentById(R.id.container);
        assertNotNull(frag);

        Firebase ref = mock(Firebase.class);
        FirebaseInterfacer.interfacer.setRef(ref);
        assertEquals(ref, FirebaseInterfacer.interfacer.getRef());


        mEmailView = (EditText) frag.getView().findViewById(R.id.email_input);
        mPassView = (EditText) frag.getView().findViewById(R.id.password_input);
        mFirstName = (EditText) frag.getView().findViewById(R.id.first_name_input);
        mLastName = (EditText) frag.getView().findViewById(R.id.last_name_input);
        mRegisterButton = (Button) frag.getView().findViewById(R.id.register_button);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNotNull() throws Exception {
        assertNotNull(activity);
        assertNotNull(mEmailView);
        assertNotNull(mPassView);
        assertNotNull(mFirstName);
        assertNotNull(mLastName);
        assertNotNull(mRegisterButton);
    }

    @Test
    public void testEmptyAll() throws Exception {
        mEmailView.setText("");
        mPassView.setText("");
        mFirstName.setText("");
        mLastName.setText("");
        mRegisterButton.performClick();
        assertEquals(activity.getString(R.string.error_field_required), mEmailView.getError());
        assertTrue(mEmailView.hasFocus());
    }

    @Test
    public void testEmptyPassword() throws Exception {
        mEmailView.setText("foo@bar.com");
        mPassView.setText("");
        mFirstName.setText("AAAA");
        mLastName.setText("BBBB");
        mRegisterButton.performClick();
        assertEquals(activity.getString(R.string.error_field_required), mPassView.getError());
        assertTrue(mPassView.hasFocus());
    }



    public void testEmptyLastName() throws Exception {
        mEmailView.setText("foo@bar.com");
        mPassView.setText("Password");
        mFirstName.setText("AAAA");
        mLastName.setText("");
        mRegisterButton.performClick();
        assertEquals(activity.getString(R.string.error_field_required), mPassView.getError());
        assertTrue(mPassView.hasFocus());
    }

    @Test
    public void testNewRegister() throws Exception {
        mEmailView.setText("foo@bar.com");
        mPassView.setText("pass");
        mFirstName.setText("test_first");
        mLastName.setText("test_last");
        mRegisterButton.performClick();
        assertNull(mEmailView.getError());
        assertNull(mPassView.getError());
    }



}