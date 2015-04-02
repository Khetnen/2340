package com.gatech.objectsanddesign.shoppingwithfriends;

import android.support.v4.app.Fragment;
import android.widget.AutoCompleteTextView;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.gatech.objectsanddesign.shoppingwithfriends.NewSale;

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
public class NewSaleTest {

    private NewSale activity;
    private EditText mName;
    private EditText mPrice;
    private Button mAddSale;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(NewSale.class).create().start().resume().get();
        activity.getSupportFragmentManager().executePendingTransactions();
        Fragment frag = activity.getSupportFragmentManager().findFragmentById(R.id.container);
        assertNotNull(frag);

        Firebase ref = mock(Firebase.class);
        FirebaseInterfacer.interfacer.setRef(ref);
        assertEquals(ref, FirebaseInterfacer.interfacer.getRef());

        mAddSale = (Button) frag.getView().findViewById(R.id.add_sale);
        mName = (EditText) frag.getView().findViewById(R.id.add_sale_name);
        mPrice = (EditText) frag.getView().findViewById(R.id.add_sale_price);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNotNull() throws Exception {
        assertNotNull(activity);
        assertNotNull(mName);
        assertNotNull(mPrice);
        assertNotNull(mAddSale);
    }

    @Test
    public void testEmptyName() throws Exception {
        mName.setText("");
        mPrice.setText("900");
        mAddSale.performClick();
        assertEquals("This field is required", mName.getError());
        assertTrue(mName.hasFocus());
    }

    @Test
    public void testEmptyPrice() throws Exception {
        mName.setText("foobar");
        mPrice.setText("");
        mAddSale.performClick();
        assertEquals("This field is required", mPrice.getError());
        assertTrue(mPrice.hasFocus());
    }

    @Test
    public void testEmptyAll() throws Exception {
        mName.setText("");
        mPrice.setText("");
        mAddSale.performClick();
        assertEquals("This field is required", mName.getError());
        assertTrue(mName.hasFocus());
        assertEquals("This field is required", mPrice.getError());
        assertTrue(mPrice.hasFocus());
    }

    @Test
    public void testEmptyInvalid() throws Exception {
        mName.setText("");
        mPrice.setText("xyz");
        mAddSale.performClick();
        assertEquals("This field is required", mName.getError());
        assertTrue(mName.hasFocus());
        assertEquals("Number not a valid price.", mPrice.getError());
        assertTrue(mPrice.hasFocus());
    }

    @Test
    public void testInvalidPrice() throws Exception {
        mName.setText("foobar");
        mPrice.setText("foobar");
        mAddSale.performClick();
        assertEquals("Number not a valid price.", mPrice.getError());
        assertTrue(mPrice.hasFocus());
    }

    @Test
    public void testValidSale() throws Exception {
        Firebase ref = mock(Firebase.class);
        FirebaseInterfacer.interfacer.setRef(ref);
        assertEquals(ref, FirebaseInterfacer.interfacer.getRef());
        mName.setText("foobar");
        mPrice.setText("42");
        mAddSale.performClick();
        assertNull(mName.getError());
        assertNull(mPrice.getError());
    }
}