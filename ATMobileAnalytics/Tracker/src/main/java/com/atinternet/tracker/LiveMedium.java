/*
 * This SDK is licensed under the MIT license (MIT)
 * Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.atinternet.tracker;

/**
 * Wrapper class for media tracking
 */
public class LiveMedium extends RichMedia {

    LiveMedium(MediaPlayer player) {
        super(player);
        broadcastMode = BroadcastMode.Live;
    }

    /**
     * Set a new media label
     *
     * @param mediaLabel /
     * @return the LiveMedium instance
     */
    public LiveMedium setMediaLabel(String mediaLabel) {
        this.mediaLabel = mediaLabel;
        return this;
    }

    /**
     * Set a new first media theme
     *
     * @param mediaTheme1 /
     * @return the LiveMedium instance
     */
    public LiveMedium setMediaTheme1(String mediaTheme1) {
        this.mediaTheme1 = mediaTheme1;
        return this;
    }

    /**
     * Set a new second media theme
     *
     * @param mediaTheme2 /
     * @return the LiveMedium instance
     */
    public LiveMedium setMediaTheme2(String mediaTheme2) {
        this.mediaTheme2 = mediaTheme2;
        return this;
    }

    /**
     * Set a new third media theme
     *
     * @param mediaTheme3 /
     * @return the LiveMedium instance
     */
    public LiveMedium setMediaTheme3(String mediaTheme3) {
        this.mediaTheme3 = mediaTheme3;
        return this;
    }

    /**
     * Set a new media level 2
     *
     * @param mediaLevel2 /
     * @return LiveMedium instance
     */
    public LiveMedium setMediaLevel2(int mediaLevel2) {
        this.mediaLevel2 = mediaLevel2;
        return this;
    }

    /**
     * Change boolean "isEmbedded" value
     *
     * @param isEmbedded /
     * @return LiveMedium instance
     */
    public LiveMedium setEmbedded(boolean isEmbedded) {
        this.isEmbedded = isEmbedded;

        return this;
    }

    /**
     * Set a new media type
     *
     * @param mediaType /
     * @return the LiveMedium instance
     */
    public LiveMedium setMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    /**
     * Set a new webdomain
     *
     * @param webDomain /
     * @return LiveMedium instance
     */
    public LiveMedium setWebDomain(String webDomain) {
        this.webDomain = webDomain;
        return this;
    }

    /**
     * Set a new linked content
     *
     * @param linkedContent /
     * @return the LiveMedium instance
     */
    public LiveMedium setLinkedContent(String linkedContent) {
        this.linkedContent = linkedContent;
        return this;
    }
}
