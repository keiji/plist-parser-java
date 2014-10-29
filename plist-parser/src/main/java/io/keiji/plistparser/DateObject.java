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

class DateObject extends PListObject<Date> {

    DateObject(Date value) {
        super(value);
    }

    @Override
    public Date getDate() throws PListException {
        return getValue();
    }

    @Override
    Type getType() {
        return Type.Date;
    }

    @Override
    void toString(StringBuffer sb, int indent, int level) {
        insertSpaces(sb, indent, level);

        sb.append("<date>").append(getValue().toString()).append("</date>").append('\n');
    }

}
