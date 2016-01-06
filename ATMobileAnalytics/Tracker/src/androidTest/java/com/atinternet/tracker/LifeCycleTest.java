/*
This SDK is licensed under the MIT license (MIT)
Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.atinternet.tracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class LifeCycleTest extends AbstractTestClass {

    private SharedPreferences preferences;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        preferences = Robolectric.application.getSharedPreferences(TrackerKeys.PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        today = sdf.format(new Date());
    }

    @Test
    public void retrieveSDKV1LifecycleTest() {
        preferences.edit().putBoolean("FirstLaunch", true)
                .putString("FirstLaunchDate", null)
                .putString("LastLaunchDate", null)
                .putInt("LaunchCount", 0)
                .apply();

        SharedPreferences backwardPrefs = Robolectric.application.getSharedPreferences("ATPrefs", Context.MODE_PRIVATE);
        backwardPrefs.edit()
                .putString("ATFirstLaunch", "fld")
                .putInt("ATLaunchCount", 6)
                .putString("ATLastLaunch", "yesterday")
                .apply();

        assertTrue(preferences.getBoolean("FirstLaunch", true));
        assertTrue(preferences.getString("FirstLaunchDate", "").isEmpty());
        assertTrue(preferences.getString("LastLaunchDate", "").isEmpty());
        assertTrue(preferences.getInt("LaunchCount", 0) == 0);

        LifeCycle.firstLaunchInit(preferences, backwardPrefs);

        assertFalse(preferences.getBoolean("FirstLaunch", true));
        assertEquals("fld", preferences.getString("FirstLaunchDate", ""));
        assertEquals("yesterday", preferences.getString("LastLaunchDate", ""));
        assertEquals(6, preferences.getInt("LaunchCount", 0));
    }

    @Test
    public void firstLaunchInitTest() {
        LifeCycle.firstLaunchInit(preferences, null);
        assertTrue(preferences.getBoolean(LifeCycle.FIRST_LAUNCH_KEY, false));
        assertFalse(preferences.getBoolean(LifeCycle.FIRST_LAUNCH_AFTER_UPDATE_KEY, false));
        assertEquals(today, preferences.getString(LifeCycle.FIRST_LAUNCH_DATE_KEY, null));

        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_KEY, 0));
        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_SINCE_UPDATE_KEY, 0));
        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_ON_DAY_KEY, 0));
        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_ON_WEEK_KEY, 0));
        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_ON_MONTH_KEY, 0));
        assertEquals(0, preferences.getInt(LifeCycle.DAYS_SINCE_FIRST_LAUNCH_KEY, 1));
        assertEquals(0, preferences.getInt(LifeCycle.DAYS_SINCE_LAST_USE_KEY, 1));
        assertEquals("1", preferences.getString(LifeCycle.VERSION_CODE_KEY, null));
    }

    @Test
    public void newLaunchInitTest() throws JSONException {

        LifeCycle.firstLaunchInit(preferences, null);
        JSONObject obj = new JSONObject(LifeCycle.getMetrics(preferences).execute());
        JSONObject life = obj.getJSONObject("lifecycle");
        String sesssionId = life.getString("sessionId");

        LifeCycle.newLaunchInit(preferences);
        obj = new JSONObject(LifeCycle.getMetrics(preferences).execute());
        life = obj.getJSONObject("lifecycle");

        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_KEY, 0));
        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_SINCE_UPDATE_KEY, 0));
        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_ON_DAY_KEY, 0));
        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_ON_WEEK_KEY, 0));
        assertEquals(1, preferences.getInt(LifeCycle.LAUNCH_COUNT_ON_MONTH_KEY, 0));
        assertNotSame(sesssionId, life.getString("sessionId"));
    }

    public void lifecycleContainedInHitTest() throws JSONException {
        LifeCycle.firstLaunchInit(preferences, null);
        JSONObject obj = new JSONObject(LifeCycle.getMetrics(preferences).execute());
        JSONObject life = obj.getJSONObject("lifecycle");

        assertEquals(1, life.getInt("fl"));
        assertEquals(0, life.getInt("flau"));
        assertEquals(1, life.getInt("lc"));
        assertEquals(1, life.getInt("lmc"));
        assertEquals(1, life.getInt("lwc"));
        assertEquals(1, life.getInt("ldc"));
        assertEquals(Integer.parseInt(today), life.getInt("fld"));
        assertEquals(0, life.getInt("dslu"));
        assertEquals(0, life.getInt("dsfl"));

        assertTrue(life.isNull("dsu"));
        assertTrue(life.isNull("lcsu"));
        assertTrue(life.isNull("uld"));
    }

    @Test
    public void afterUpdateTest() throws JSONException {
        LifeCycle.firstLaunchInit(preferences, null);
        preferences.edit().putString(LifeCycle.VERSION_CODE_KEY, "test").apply();
        LifeCycle.isInitialized = false;
        LifeCycle.updateFirstLaunch(preferences);
        LifeCycle.newLaunchInit(preferences);

        JSONObject obj = new JSONObject(LifeCycle.getMetrics(preferences).execute());
        JSONObject life = obj.getJSONObject("lifecycle");

        assertEquals(0, life.getInt("fl"));
        assertEquals(1, life.getInt("flau"));
        assertEquals(2, life.getInt("lc"));
        assertEquals(2, life.getInt("lmc"));
        assertEquals(2, life.getInt("lwc"));
        assertEquals(2, life.getInt("ldc"));
        assertEquals(Integer.parseInt(today), life.getInt("fld"));
        assertEquals(0, life.getInt("dslu"));
        assertEquals(0, life.getInt("dsfl"));
        assertEquals(0, life.getInt("dsu"));
        assertEquals(1, life.getInt("lcsu"));
        assertEquals(Integer.parseInt(today), life.getInt("uld"));
    }
}