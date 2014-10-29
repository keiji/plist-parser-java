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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * plist parser.
 */
public class PListParser {

    private static final String TAG = PListParser.class.getSimpleName();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * Make singleton.
     */
    private PListParser() {
    }

    public static <T extends PListObject> T parse(InputStream is) throws PListException {
        return parse(new InputStreamReader(is));
    }

    public static <T extends PListObject> T parse(String plist) throws PListException {
        StringReader sr = new StringReader(plist);
        return parse(sr);
    }

    public static <T extends PListObject> T parse(Reader reader) throws PListException {

        PListObject plistObject = null;

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(reader);

            int eventType = 0;
            while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("plist")) {
                            continue;
                        }
                        plistObject = getValue(parser);
                        break;
                }
            }

        } catch (XmlPullParserException e) {
            throw new PListException(e.getMessage());
        } catch (IOException e) {
            throw new PListException(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }

        }

        return (T) plistObject;
    }

    private static void getChildValues(XmlPullParser parser, PListArray array)
            throws XmlPullParserException, IOException, PListException {
        array.clear();
        int eventType = 0;
        while (true) {
            eventType = parser.next();
            if (eventType == XmlPullParser.END_TAG
                    && parser.getName() != null
                    && parser.getName().equals("array")) {
                break;
            }

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    array.add(getValue(parser));
                    break;
            }
        }
    }

    private static void getChildValues(XmlPullParser parser, PListDict dict)
            throws XmlPullParserException, IOException, PListException {
        dict.clear();
        int eventType = 0;
        Key key = null;
        while (true) {
            eventType = parser.next();
            if (eventType == XmlPullParser.END_TAG
                    && parser.getName() != null
                    && parser.getName().equals("dict")) {
                break;
            }

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String tagName = parser.getName();
                    if (tagName.equals("key")) {
                        key = new Key(parser.nextText());
                    } else if (key != null) {
                        PListObject val = getValue(parser);
                        dict.put(key, val);
                        key = null;
                    }
                    break;
            }
        }
    }

    private static PListObject getValue(XmlPullParser parser) throws XmlPullParserException,
            IOException, PListException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            return null;
        }

        String tagName = parser.getName();
        if (tagName.equals("array")) {
            PListArray val = new PListArray();
            getChildValues(parser, val);
            return val;

        } else if (tagName.equals("dict")) {
            PListDict val = new PListDict();
            getChildValues(parser, val);
            return val;

        } else if (tagName.equals("key")) {
            return new Key(parser.nextText());

        } else if (tagName.equals("string")) {
            return new StringObject(parser.nextText());

        } else if (tagName.equals("integer")) {
            String text = parser.nextText();
            try {
                return new IntegerObject(Integer.parseInt(text));
            } catch (NumberFormatException e) {
                throw new PListException("NumberFormatException occurred.");
            }

        } else if (tagName.equals("real")) {
            String text = parser.nextText();
            try {
                return new RealObject(Double.parseDouble(text));
            } catch (NumberFormatException e) {
                throw new PListException("NumberFormatException occurred. " + text);
            }

        } else if (tagName.equals("true") || tagName.equals("false")) {
            return new BoolObject(tagName.equals("true"));

        } else if (tagName.equals("date")) {
            String text = parser.nextText();
            try {
                return new DateObject(DATE_FORMAT.parse(text));
            } catch (ParseException e) {
                throw new PListException("ParseException occurred. " + text);
            }
        }

        return null;
    }
}