
package jp.co.c_lis.ccl.plistlib;

/**
 * plistの要素を表すインターフェース.
 * 
 * @author Keiji Ariyama C-LIS CO., LTD.
 */
public interface IElement {

    /**
     * 要素の種類
     */
    public enum Type {
            Integer,
            String,
            Date,
            Dict,
            Key,
            Array,
    }

    /**
     * 要素の種類を取得.
     * 
     * @return
     */
    public Type getType();

    /**
     * 要素を取得.
     * 
     * @return
     */
    public Object getValue();

}
