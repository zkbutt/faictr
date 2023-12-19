/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package top.feadre.faictr.services.adb;

import java.util.ArrayList;

public class ADBShellOrderEntity {
    private byte[] orderByte;
    private int orderId;
    private String resText;
    private String orderText;
    private ArrayList<String> resList;
    private boolean isReadRes;

    public ADBShellOrderEntity(byte[] orderByte, int orderId, boolean isReadRes) {
        this.orderByte = orderByte;
        this.orderId = orderId;
        this.isReadRes = isReadRes;
        if (orderByte != null) {
            this.orderText = String.valueOf(ADBCommands.bytes2texts(orderByte));
        }
    }

    public boolean isReadRes() {
        return isReadRes;
    }

    public void setReadRes(boolean readRes) {
        isReadRes = readRes;
    }

    public byte[] getOrderByte() {
        return orderByte;
    }

    public void setOrderByte(byte[] orderByte) {
        this.orderByte = orderByte;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public ArrayList<String> getResList() {
        return resList;
    }

    public void setResList(ArrayList<String> resList) {
        this.resList = resList;
    }
}
