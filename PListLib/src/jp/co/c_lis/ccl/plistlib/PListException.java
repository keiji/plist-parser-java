
package jp.co.c_lis.ccl.plistlib;

/**
 * plist形式の例外.
 * 
 * @author Keiji Ariyama C-LIS CO., LTD.
 */
public class PListException extends RuntimeException {
    private static final long serialVersionUID = -8472719594339564960L;

    /**
     * コンストラクタ.
     * 
     * @param message
     */
    public PListException(String message) {
        super(message);
    }

}
