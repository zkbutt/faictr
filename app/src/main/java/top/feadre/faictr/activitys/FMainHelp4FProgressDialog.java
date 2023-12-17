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


import com.xuexiang.xui.utils.ResUtils;

import top.feadre.faictr.R;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.base.FProgressDialog;

public class FMainHelp4FProgressDialog extends FProgressDialog implements FProgressDialog.OnFProgressDialogListener {
    private static final String TAG = "FMainH4FProgressDialog";
    private final FMainActivity fMainActivity;

    public FMainHelp4FProgressDialog(FMainActivity fMainActivity) {
        super(fMainActivity);
        this.setOnFProgressDialogListener(this);
        this.fMainActivity = fMainActivity;
    }

    public void close() {
        dialog.dismiss();
    }


    @Override
    public void on_dialog_success(String id, DialogInterface di) {
        FTools.log_d(TAG, "任务成功 id = " + id);
    }

    @Override
    public void on_dialog_cancel(String id, DialogInterface di) {
        //区别在于 进度值  this.progress_val
        FTools.log_d(TAG, "任务取消 this.progress_val = " + this.progress_val
                + " id = " + id);
        if (id.equals(ResUtils.getString(R.string.FMainHelp4FProgressDialog_bt_ip_search))) {
            fMainActivity.fMainHelp4NetUtils.stop_scan_ip();
        }
    }


}
