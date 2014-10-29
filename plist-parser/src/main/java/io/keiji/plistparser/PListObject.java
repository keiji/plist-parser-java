/*
 * Copyright (C) 20011 Keiji Ariyama C-LIS CO., LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.keiji.plistparser;

import java.util.Date;

public abstract class PListObject<T> {

    public enum Type {
        Key,
        String,
        Int,
        Real,
        Bool,
        Date,
        Dict,
        Array
    }

    private static final int DEFAULT_INDENT = 4;

    private T value;

    T getValue() {
        return value;
    }

    PListObject(T value) {
        this.value = value;
    }

    public String getString() throws PListException {
        throw new PListException("value is not string.");
    }

    public int getInt() throws PListException {
        throw new PListException("value is not int.");
    }

    public double getReal() throws PListException {
        throw new PListException("value is not real.");
    }

    public boolean getBool() throws PListException {
        throw new PListException("value is not bool.");
    }

    public Date getDate() throws PListException {
        throw new PListException("value is not date.");
    }

    public PListDict getPListDict() throws PListException {
        throw new PListException("value is not dict.");
    }

    public PListArray getPListArray() throws PListException {
        throw new PListException("value is not array.");
    }

    @Override
    public String toString() {

        return toString(DEFAULT_INDENT);
    }

    public String toString(int indent) {
        StringBuffer sb = new StringBuffer();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append('\n')
                .append("<plist version=\"1.0\">").append('\n');

        toString(sb, indent, 1);

        sb.append("</plist>").append('\n');

        return sb.toString();
    }

    static void insertSpaces(StringBuffer sb, int indent, int level) {
        indent *= level;

        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
    }

    abstract Type getType();

    abstract void toString(StringBuffer sb, int indent, int level);
}
