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
            Bool,
            Integer,
            Real,
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
