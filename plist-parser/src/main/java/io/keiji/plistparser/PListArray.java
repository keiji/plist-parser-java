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

import java.util.ArrayList;
import java.util.Date;

public class PListArray extends PListObject<PListObject> {

    private final ArrayList<PListObject> list = new ArrayList<PListObject>();

    PListArray() {
        super(null);
    }

    void add(PListObject element) {
        list.add(element);
    }

    void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public PListObject get(int idx) throws PListException {
        return list.get(idx);
    }

    public int getInt(int idx) throws PListException {
        return get(idx).getInt();
    }

    public Date getDate(int idx) throws PListException {
        return get(idx).getDate();
    }

    public String getString(int idx) throws PListException {
        return get(idx).getString();
    }

    public double getReal(int idx) throws PListException {
        return get(idx).getReal();
    }

    public boolean getBool(int idx) throws PListException {
        return get(idx).getBool();
    }

    public PListArray getPListArray(int idx) throws PListException {
        if (!(get(idx) instanceof PListArray)) {
            throw new PListException("index " + idx + " is not PListArray object.");
        }
        return (PListArray) get(idx);
    }

    public PListDict getPListDict(int idx) throws PListException {
        if (!(get(idx) instanceof PListDict)) {
            throw new PListException("index " + idx + " is not PListDict object.");
        }
        return (PListDict) get(idx);
    }

    @Override
    Type getType() {
        return Type.Array;
    }

    @Override
    void toString(StringBuffer sb, int indent, int level) {
        insertSpaces(sb, indent, level);
        sb.append("<array>").append('\n');

        for (PListObject obj : list) {
            obj.toString(sb, indent, level + 1);
        }

        insertSpaces(sb, indent, level);
        sb.append("</array>").append('\n');
    }
}
