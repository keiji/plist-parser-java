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

package jp.co.c_lis.ccl.plistlib.test;

import java.io.InputStream;
import java.util.Date;

import jp.co.c_lis.ccl.plistlib.IElement;
import jp.co.c_lis.ccl.plistlib.PListObject;
import jp.co.c_lis.ccl.plistlib.PListObject.PListArray;
import jp.co.c_lis.ccl.plistlib.PListObject.PListDict;
import android.test.AndroidTestCase;
import android.util.Log;

public class PLibTest extends AndroidTestCase {
    private static final String LOG_TAG = "PLibTest";
    private static final boolean DBUG_FLG = true;

    private static final String FILE_NAME = "test.plist";

    public void testParsePLib() throws Exception {
        InputStream is = getContext().getAssets().open(FILE_NAME);

        PListObject plist = new PListObject(is);

        assertEquals(IElement.Type.Dict, plist.getType());

        PListDict dict = (PListDict) plist.getValue();

        assertEquals(4, dict.size());

        for (String key : dict.keySet()) {
            if (key.equals("value1")) {
                String str = dict.getString(key);
                assertEquals("Keiji_Ariyama", str);
            } else if (key.equals("code")) {
                String str = dict.getString(key);
                assertEquals("54235582305924389532", str);
            } else if (key.equals("date")) {
                Date date = dict.getDate(key);
               
                // 2011-11-12T03:14:00Z
                java.util.Date tmp = new java.util.Date();
                tmp.setYear(2011 - 1900);  // Dateの仕様
                tmp.setMonth(11 - 1);  // Dateの仕様
                tmp.setDate(12);
                tmp.setHours(3);
                tmp.setMinutes(14);
                tmp.setSeconds(0);
                assertEquals(tmp.toString(), date.toString());

            } else if (key.equals("users")) {
                PListArray array = (PListArray) dict.get(key);
                assertEquals(2, array.size());

                int len = array.size();
                for (int i = 0; i < len; i++) {
                    PListDict user = (PListDict) array.get(i);
                    assertUser(user);
                }
            }
        }
    }

    private static final String[] nameArray = new String[] {
            null, "test1", "test2"
    };

    private void assertUser(PListDict user) {

        int id = 0;
        String name = null;
        for (String key : user.keySet()) {
            if (key.equals("id")) {
                id = user.getInteger(key);
            } else if (key.equals("name")) {
                name = user.getString(key);
            }
        }

        Log.d(LOG_TAG, String.format("%d : %s", id, name));
        assertEquals(nameArray[id], name);
    }
}
