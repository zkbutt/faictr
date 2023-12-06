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

package top.feadre.faictr.activitys;

import android.content.DialogInterface;

import com.xuexiang.xui.utils.XToastUtils;

import top.feadre.faictr.flib.base.FProgressDialog;

public class FMainHelp4FProgressDialog extends FProgressDialog implements FProgressDialog.OnFProgressDialogListener {
    private final FMainActivity fMainActivity;

    public FMainHelp4FProgressDialog(FMainActivity fMainActivity) {
        super(fMainActivity);
        this.setOnFProgressDialogListener(this);
        this.fMainActivity = fMainActivity;
    }


    @Override
    public void on_dialog_success(String id, DialogInterface di) {
        XToastUtils.success("成功:" + id);
    }

    @Override
    public void on_dialog_cancel(String id, DialogInterface di) {
        XToastUtils.warning("手动取消:" + id);
    }
}
