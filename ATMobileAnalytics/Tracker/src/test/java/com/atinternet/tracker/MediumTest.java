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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class MediumTest extends AbstractTestClass {

    private Medium medium;
    private MediaPlayer player;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        player = new MediaPlayer(tracker);
        medium = new Medium(player);
    }

    @Test
    public void initTest() {
        assertEquals(-1, medium.getLevel2());
        assertEquals(-1, medium.getMediaLevel2());
        assertEquals("", medium.getName());
        assertEquals("", medium.getMediaLabel());
        assertEquals("", medium.getMediaType());
        assertNull(medium.getWebDomain());
        assertEquals(RichMedia.BroadcastMode.Clip, medium.getBroadcastMode());
        assertFalse(medium.isBuffering());
        assertFalse(medium.isEmbedded());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(medium, new Medium(player));
    }

    @Test
    public void setParamsPlayTest() {
        tracker.setParam("a", RichMedia.Action.Play.stringValue());
        medium.setMediaType("anim")
                .setDuration(56)
                .setMediaLabel("name")
                .setMediaLevel2(9)
                .setMediaTheme1("chapter1")
                .setParams();

        assertEquals(8, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("anim", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("play", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("9", buffer.getVolatileParams().get("s2").getValues().get(0).execute());
    }

    @Test
    public void setParamsRefreshTest() {
        tracker.setParam("a", RichMedia.Action.Refresh.stringValue());
        medium.setMediaType("anim")
                .setDuration(56)
                .setMediaLabel("name")
                .setMediaLevel2(9)
                .setMediaTheme1("chapter1")
                .setParams();

        assertEquals(8, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("anim", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("refresh", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("9", buffer.getVolatileParams().get("s2").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m1").getValues().size());
        assertEquals("56", buffer.getVolatileParams().get("m1").getValues().get(0).execute());
    }

    @Test
    public void setParamsPauseTest() {
        tracker.setParam("a", RichMedia.Action.Pause.stringValue());
        medium.setMediaType("anim")
                .setDuration(56)
                .setMediaLabel("name")
                .setMediaLevel2(9)
                .setMediaTheme1("chapter1")
                .setParams();
        assertEquals(8, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("anim", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("pause", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("9", buffer.getVolatileParams().get("s2").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m1").getValues().size());
        assertEquals("56", buffer.getVolatileParams().get("m1").getValues().get(0).execute());
    }

    @Test
    public void setParamsStopTest() {
        tracker.setParam("a", RichMedia.Action.Stop.stringValue());
        medium.setMediaType("anim")
                .setDuration(56)
                .setMediaLabel("name")
                .setMediaLevel2(9)
                .setMediaTheme1("chapter1")
                .setParams();
        assertEquals(8, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("anim", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("stop", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("9", buffer.getVolatileParams().get("s2").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m1").getValues().size());
        assertEquals("56", buffer.getVolatileParams().get("m1").getValues().get(0).execute());
    }
}
