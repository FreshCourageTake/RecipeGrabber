package tests;

import android.test.InstrumentationTestCase;

import com.android.andrewgarver.recipegrabber.IngredDatabase;

/**
 * Created by Andrew Garver on 11/4/2015.
 */
public class ExampleTest extends InstrumentationTestCase {
    public boolean testCallDataBase() throws Exception {
        boolean accessedDB = false;
        assert(accessedDB);
        return true;
    }

    IngredDatabase testDatabase = new IngredDatabase();

    public void testAddIndred() throws Exception {
        assert 0 == testDatabase.addIngred("Baby Carrots");
        assert 1 == testDatabase.addIngred("Salt");
        assert 2 == testDatabase.addIngred("Baking Soda");
    }

    public void testFetchIndred() throws Exception {
        assert testDatabase.fetchIngred(0).equals("");
        testAddIndred();
        assert testDatabase.fetchIngred(0).equals("Baby Carrots");
        assert testDatabase.fetchIngred(1).equals("Salt");
        assert testDatabase.fetchIngred(2).equals("Baking Soda");
    }
}


