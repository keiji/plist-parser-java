
package jp.co.c_lis.ccl.plistlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

/**
 * plist形式のオブジェクト.
 * 
 * @author Keiji Ariyama C-LIS CO., LTD.
 */
public class PListObject implements IElement, Serializable {
    private static final long serialVersionUID = 8745270165673642453L;

    private static final String LOG_TAG = "PListObject";
    private static final boolean DEBUG_FLG = false;

    private Type type = Type.String;
    private Object value = null;

    /**
     * コンストラクタ.
     */
    private PListObject() {
    }

    /**
     * コンストラクタ.
     * 
     * @param is
     */
    public PListObject(InputStream is) {
        parsePList(new InputStreamReader(is), this);
    }

    /**
     * コンストラクタ.
     * 
     * @param plist
     */
    public PListObject(String plist) {
        StringReader sr = new StringReader(plist);
        parsePList(sr, this);
    }

    /**
     * 文字列を設定.
     * 
     * @param val
     */
    public void set(String val) {
        type = Type.String;
        value = val;
    }

    /**
     * 文字列を取得.
     * 
     * @return
     * @throws PListException
     */
    public String getString() throws PListException {
        if (type != Type.String) {
            throw new PListException("value is not string.");
        }
        return (String) value;
    }

    /**
     * 数値を設定.
     * 
     * @param val
     */
    public void set(int val) {
        type = Type.Integer;
        value = val;
    }

    /**
     * 数値を取得.
     * 
     * @return
     * @throws PListException
     */
    public int getInt() throws PListException {
        if (type != Type.Integer) {
            throw new PListException("value is not integer.");
        }
        return ((Integer) value).intValue();
    }

    /**
     * 日付を設定.
     * 
     * @param val
     */
    public void set(Date val) {
        type = Type.Date;
        value = val;
    }

    /**
     * 日付を取得.
     * 
     * @return
     * @throws PListException
     */
    public Date getDate() throws PListException {
        if (type != Type.Date) {
            throw new PListException("value is not date.");
        }
        return (Date) value;
    }

    /**
     * 辞書型を設定.
     * 
     * @param val
     */
    public void set(PListDict val) {
        type = Type.Dict;
        value = val;
    }

    /**
     * 辞書型を取得.
     * 
     * @return
     * @throws PListException
     */
    public PListDict getDict() throws PListException {
        if (type != Type.Dict) {
            throw new PListException("value is not dict.");
        }
        return (PListDict) value;
    }

    /**
     * 配列を設定.
     * 
     * @param val
     */
    public void set(PListArray val) {
        type = Type.Array;
        value = val;
    }

    /**
     * 配列を取得.
     * 
     * @return
     * @throws PListException
     */
    public PListArray getArray() throws PListException {
        if (type != Type.Dict) {
            throw new PListException("value is not array.");
        }
        return (PListArray) value;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.c_lis.ccl.plistlib.PList.IElement#getType()
     */
    public Type getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * @see jp.co.c_lis.ccl.plistlib.PList.IElement#getValue()
     */
    public Object getValue() {
        return value;
    }

    /**
     * plistを読込.
     * 
     * @param is
     * @param out
     */
    private static void parsePList(Reader reader, PListObject out) {

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(reader);

            int eventType = 0;
            while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("plist")) {
                            break;
                        }
                        IElement element = getValue(parser);
                        out.value = element;
                        out.type = element.getType();
                        break;
                }
            }

        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG, "XmlPullParserException", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException", e);
        }
    }

    /**
     * 子要素を読込.
     * 
     * @param parser
     * @param array
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static void getChildValues(XmlPullParser parser, PListArray array)
            throws XmlPullParserException, IOException {
        array.clear();
        int eventType = 0;
        while ((eventType = parser.next()) != XmlPullParser.END_TAG) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    array.add(getValue(parser));
                    break;
            }
        }
    }

    /**
     * 子要素を読込.
     * 
     * @param parser
     * @param dict
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static void getChildValues(XmlPullParser parser, PListDict dict)
            throws XmlPullParserException, IOException {
        dict.clear();
        int eventType = 0;
        Key key = null;
        while ((eventType = parser.next()) != XmlPullParser.END_TAG) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String tagName = parser.getName();
                    if (tagName.equals("key")) {
                        key = new Key();
                        key.val = parser.nextText();
                    } else if (key != null) {
                        IElement val = getValue(parser);
                        dict.put(key, val);
                        key = null;
                    }
                    break;
            }
        }
    }

    /**
     * IElementオブジェクトを取得.
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static IElement getValue(XmlPullParser parser) throws XmlPullParserException,
            IOException {
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
            Key val = new Key();
            val.val = parser.nextText();
            return val;
        } else if (tagName.equals("string")) {
            PListObject obj = new PListObject();
            obj.set(parser.nextText());
            return obj;
        } else if (tagName.equals("integer")) {
            PListObject obj = new PListObject();
            try {
                obj.set(Integer.parseInt(parser.nextText()));
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, "NumberFormatException", e);
            }
            return obj;
        } else if (tagName.equals("date")) {
            PListObject obj = new PListObject();
            try {
                obj.set(new SimpleDateFormat(DATE_FORMAT).parse(parser.nextText()));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "ParseException", e);
            }
            return obj;
        }

        return null;
    }

    // 日付のフォーマット
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * 辞書形式のキーを表すクラス.
     */
    public static class Key implements IElement, Serializable {
        private static final long serialVersionUID = 7368190138452476664L;

        private String val = null;

        /*
         * (non-Javadoc)
         * @see jp.co.c_lis.ccl.plistlib.PList.IElement#getType()
         */
        public Type getType() {
            return Type.Key;
        }

        /*
         * (non-Javadoc)
         * @see jp.co.c_lis.ccl.plistlib.IElement#getValue()
         */
        public Object getValue() {
            throw new PListException("getValue() is not supported.");
        }
    }

    /**
     * 辞書形式のクラス
     */
    public static class PListDict implements IElement, Serializable {
        private static final long serialVersionUID = 582593975339902309L;

        private final HashMap<String, IElement> map = new HashMap<String, IElement>();

        /*
         * (non-Javadoc)
         * @see jp.co.c_lis.ccl.plistlib.IElement#getType()
         */
        public Type getType() {
            return Type.Dict;
        }

        /**
         * 要素の追加.
         * 
         * @param key
         * @param element
         */
        public void put(Key key, IElement element) {
            map.put(key.val, element);
        }

        /**
         * 全ての要素を削除.
         */
        public void clear() {
            map.clear();
        }
        
        /**
         * 指定したキーが格納されているか.
         * 
         * @param key
         * @return
         */
        public boolean has(String key) {
            return map.containsKey(key);
        }

        /**
         * 格納している要素のサイズ.
         * 
         * @return
         */
        public int size() {
            return map.size();
        }

        /**
         * 辞書が格納しているキーセット.
         * 
         * @return
         */
        public Set<String> keySet() {
            return map.keySet();
        }

        /**
         * 指定したキーに対応する要素を取得.
         * 
         * @param key
         * @return
         * @throws PListException
         */
        public IElement get(String key) throws PListException {
            if (!map.containsKey(key)) {
                throw new PListException(String.format("key %s is not found.", key));
            }
            return map.get(key);
        }

        /**
         * 指定したキーに対応する要素を数値で取得.
         * 
         * @param key
         * @return
         * @throws PListException
         */
        public int getInteger(String key) throws PListException {
            IElement element = get(key);
            if (element.getType() != Type.Integer) {
                throw new PListException(String.format("key %s's value is not integer.", key));
            }
            return (Integer) element.getValue();
        }

        /**
         * 指定したキーに対応する要素を日付で取得.
         * 
         * @param key
         * @return
         * @throws PListException
         */
        public Date getDate(String key) throws PListException {
            IElement element = get(key);
            if (element.getType() != Type.Date) {
                throw new PListException(String.format("key %s's value is not date.", key));
            }
            return (Date) element.getValue();
        }

        /**
         * 指定したキーに対応する要素を文字列で取得.
         * 
         * @param key
         * @return
         * @throws PListException
         */
        public String getString(String key) throws PListException {
            IElement element = get(key);
            if (element.getType() != Type.String) {
                throw new PListException(String.format("key %s's value is not string.", key));
            }
            return (String) element.getValue();
        }

        /**
         * 指定したキーに対応する要素を配列で取得.
         * 
         * @param key
         * @return
         * @throws PListException
         */
        public PListArray getPListArray(String key) throws PListException {
            IElement element = get(key);
            if (element.getType() != Type.Array) {
                throw new PListException(String.format("key %s's value is not array.", key));
            }
            return (PListArray) element;
        }

        /**
         * 指定したキーに対応する要素を辞書型で取得.
         * 
         * @param key
         * @return
         * @throws PListException
         */
        public PListDict getPListDict(String key) throws PListException {
            IElement element = get(key);
            if (element.getType() != Type.Dict) {
                throw new PListException(String.format("key %s's value is not dict.", key));
            }
            return (PListDict) element;
        }

        /*
         * (non-Javadoc)
         * @see jp.co.c_lis.ccl.plistlib.IElement#getValue()
         */
        public Object getValue() {
            throw new PListException("getValue() is not supported.");
        }
    }

    /**
     * 配列のクラス.
     */
    public static class PListArray implements IElement, Serializable {
        private static final long serialVersionUID = -6751093537870910649L;

        private final ArrayList<IElement> list = new ArrayList<IElement>();

        /*
         * (non-Javadoc)
         * @see jp.co.c_lis.ccl.plistlib.IElement#getType()
         */
        public Type getType() {
            return Type.Array;
        }

        /**
         * 配列に要素を追加.
         * 
         * @param element
         */
        public void add(IElement element) {
            list.add(element);
        }

        /**
         * 全ての要素を削除.
         */
        public void clear() {
            list.clear();
        }
        
        /**
         * 配列のサイズ.
         * 
         * @return
         */
        public int size() {
            return list.size();
        }

        /**
         * 指定したインデックスに対応する要素を取得.
         * 
         * @param idx
         * @return
         * @throws PListException
         */
        public IElement get(int idx) throws PListException {
            return list.get(idx);
        }

        /**
         * 指定したインデックスに対応する要素を数値で取得.
         * 
         * @param idx
         * @return
         * @throws PListException
         */
        public int getInteger(int idx) throws PListException {
            IElement element = get(idx);
            if (element.getType() != Type.Integer) {
                throw new PListException(String.format("index %d is not integer.", idx));
            }
            return (Integer) element.getValue();
        }

        /**
         * 指定したインデックスに対応する要素を日付で取得.
         * 
         * @param idx
         * @return
         * @throws PListException
         */
        public Date getDate(int idx) throws PListException {
            IElement element = get(idx);
            if (element.getType() != Type.Date) {
                throw new PListException(String.format("index %d is not date.", idx));
            }
            return (Date) element.getValue();
        }

        /**
         * 指定したインデックスに対応する要素を文字列で取得.
         * 
         * @param idx
         * @return
         * @throws PListException
         */
        public String getString(int idx) throws PListException {
            IElement element = get(idx);
            if (element.getType() != Type.String) {
                throw new PListException(String.format("index %d is not string.", idx));
            }
            return (String) element.getValue();
        }

        /**
         * 指定したインデックスに対応する要素を配列で取得.
         * 
         * @param idx
         * @return
         * @throws PListException
         */
        public PListArray getPListArray(int idx) throws PListException {
            IElement element = get(idx);
            if (element.getType() != Type.Array) {
                throw new PListException(String.format("index %d is not array.", idx));
            }
            return (PListArray) element;
        }

        /**
         * 指定したインデックスに対応する要素を辞書型で取得.
         * 
         * @param idx
         * @return
         * @throws PListException
         */
        public PListDict getPListDict(int idx) throws PListException {
            IElement element = get(idx);
            if (element.getType() != Type.Dict) {
                throw new PListException(String.format("index %d is not dict.", idx));
            }
            return (PListDict) element;
        }

        /*
         * (non-Javadoc)
         * @see jp.co.c_lis.ccl.plistlib.IElement#getValue()
         */
        public Object getValue() {
            throw new PListException("getValue() is not supported.");
        }
    }

}
