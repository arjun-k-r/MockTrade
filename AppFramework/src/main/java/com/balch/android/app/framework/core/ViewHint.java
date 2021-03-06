/*
 * Author: Balch
 * Created: 9/4/14 12:26 AM
 *
 * This file is part of MockTrade.
 *
 * MockTrade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MockTrade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MockTrade.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2014
 */

package com.balch.android.app.framework.core;

public class ViewHint {

    public enum Hint {
        DISPLAY_LINES,
        MAX_CHARS,
        NON_NEGATIVE,
        HIDE_CENTS,
        NOT_EMPTY,
        PERCENT,
        INIT_EMPTY
    }

    protected final Hint hint;
    protected final String value;

    public ViewHint(Hint hint, String value) {
        this.hint = hint;
        this.value = value;
    }

    public Hint getHint() {
        return hint;
    }

    public String getValue() {
        return value;
    }

    public int getIntValue() {
        return Integer.parseInt(value);
    }

    public boolean getBoolValue() {
        return Boolean.parseBoolean(value);
    }

    @Override
    public String toString() {
        return ViewHint.toString(this.hint, this.value);
    }

    static public String toString(Hint hint, String value) {
        return hint.toString()+"="+value;
    }

    static public ViewHint parse(String hintString) {
        String [] parts = hintString.split("=");
        return new ViewHint(Hint.valueOf(parts[0]), parts[1]);
    }

    static public ViewHint[] parse(String[] hintStrings) {
        ViewHint[] hints = new ViewHint[hintStrings.length];
        for (int x = 0; x < hintStrings.length; x++) {
            hints[x] = ViewHint.parse(hintStrings[x]);
        }
        return hints;
    }
}
