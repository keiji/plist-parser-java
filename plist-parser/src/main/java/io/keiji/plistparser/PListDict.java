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
import java.util.HashMap;
import java.util.Set;

public class PListDict extends PListObject {

    private final HashMap<String, PListObject> map = new HashMap<String, PListObject>();

    PListDict() {
        super(null);
    }

    void put(Key key, PListObject element) throws PListException {
        map.put(key.getString(), element);
    }

    void clear() {
        map.clear();
    }

    public boolean has(String key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public PListObject get(String key) throws PListException {
        if (!map.containsKey(key)) {
            throw new PListException(String.format("key %s is not found.", key));
        }
        return map.get(key);
    }

    public int getInt(String key) throws PListException {
        return get(key).getInt();
    }

    public Date getDate(String key) throws PListException {
        return get(key).getDate();
    }

    public String getString(String key) throws PListException {
        return get(key).getString();
    }

    public double getReal(String key) throws PListException {
        return get(key).getReal();
    }

    public boolean getBool(String key) throws PListException {
        return get(key).getBool();
    }

    public PListArray getPListArray(String key) throws PListException {
        if (!(get(key) instanceof PListArray)) {
            throw new PListException("key " + key + " is not PListArray object.");
        }
        return (PListArray) get(key);
    }

    public PListDict getPListDict(String key) throws PListException {
        if (!(get(key) instanceof PListDict)) {
            throw new PListException("key " + key + " is not PListDict object.");
        }
        return (PListDict) get(key);
    }

    @Override
    Type getType() {
        return Type.Dict;
    }

    @Override
    public void toString(StringBuffer sb, int indent, int level) {
        insertSpaces(sb, indent, level);
        sb.append("<dict>").append('\n');

        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            PListObject obj = map.get(key);

            insertSpaces(sb, indent, level + 1);
            sb.append("<key>")
                    .append(key)
                    .append("</key>")
                    .append('\n');

            obj.toString(sb, indent, level + 1);
        }

        insertSpaces(sb, indent, level);
        sb.append("</dict>").append('\n');
    }
}

class Key extends PListObject<String> {

    Key(String value) {
        super(value);
    }

    @Override
    public String getString() throws PListException {
        return getValue();
    }

    @Override
    Type getType() {
        return Type.Key;
    }

    @Override
    public void toString(StringBuffer sb, int indent, int level) {
        // do nothing
    }
}
