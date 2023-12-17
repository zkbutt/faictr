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

package top.feadre.faictr.flib.base;

import android.app.Activity;
import android.content.DialogInterface;

import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import top.feadre.faictr.R;
import top.feadre.faictr.flib.FTools;

public class FProgressDialog {
    private static final String TAG = "FProgressDialogHelp";
    private final Activity activity;
    protected MaterialDialog.Builder md_builder;
    private OnFProgressDialogListener onFProgressDialogListener;
    private String id;
    protected MaterialDialog dialog;
    protected int progress_val;

    public FProgressDialog(Activity activity) {
        this.activity = activity;
    }

    public void setOnFProgressDialogListener(OnFProgressDialogListener onFProgressDialogListener) {
        this.onFProgressDialogListener = onFProgressDialogListener;
    }

    public void showDialog(String id, String title, int progress_max, boolean enabled_cancel) {
        FTools.log_d(TAG, "showDialog progress_max = " + progress_max);
        this.id = id;
        md_builder = new MaterialDialog.Builder(activity)
                .title(title)
//                .content(R.string.content_downloading)
                .contentGravity(GravityEnum.CENTER)
                .progress(false, progress_max, true);
//                .cancelListener(di -> {
////                    XToastUtils.info("点取消");
//                    if (onFProgressDialogListener != null) {
//                        onFProgressDialogListener.on_dialog_cancel(id, di);
//                    }
//                })
//                .showListener(dialog -> onFProgressDialogListener.on_running(id, (MaterialDialog) dialog))
//                .negativeText(R.string.lab_cancel);
        if (enabled_cancel) {
            md_builder.cancelListener(di -> {
//                    XToastUtils.info("点取消");
                if (onFProgressDialogListener != null) {
                    onFProgressDialogListener.on_dialog_cancel(id, di);
                }
            }).negativeText(R.string.lab_cancel);
        }
        dialog = md_builder.show();
//        dialog.setMaxProgress(progress_max);
    }

    public void showDialog(String id, String title, int progress_max) {
        showDialog(id, title, progress_max, true);
    }

    public void showDialog(String title) {
        showDialog(title, title, 100);
    }

    public void showDialog(String title, int progress_max) {
        showDialog(title, title, progress_max);
    }

    public void showDialog(String id, String title) {
        showDialog(id, title, 100);
    }

    public void updateProgress(int val, String text) {
        FTools.log_d(TAG, "updateProgress "
                + " val = " + val
                + " dialog.getMaxProgress() = " + dialog.getMaxProgress()
        );
        this.progress_val = val;
        this.dialog.setProgress(val);
        this.dialog.setContent(text);
        if (val >= dialog.getMaxProgress()) {
            dialog.dismiss();
            if (onFProgressDialogListener != null) {
                onFProgressDialogListener.on_dialog_success(id, dialog);
            }
        }
    }


    public interface OnFProgressDialogListener {

        void on_dialog_success(String id, DialogInterface di);

        void on_dialog_cancel(String id, DialogInterface di);

    }


//    private void updateProgress(MaterialDialog dialogInterface) {
//        onFProgressDialogListener.on_running(dialogInterface);
//        
//        final MaterialDialog dialog = dialogInterface;
//        
//        
//        
//        
//        while (dialog.getCurrentProgress() != dialog.getMaxProgress()
//                && !Thread.currentThread().isInterrupted()) {
//            if (dialog.isCancelled()) {
//                break;
//            }
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                break;
//            }
//            dialog.incrementProgress(1);
//        }


//        startThread(() -> {
//            while (dialog.getCurrentProgress() != dialog.getMaxProgress()
//                    && !Thread.currentThread().isInterrupted()) {
//                if (dialog.isCancelled()) {
//                    break;
//                }
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    break;
//                }
//                dialog.incrementProgress(1);
//            }
//            this.activity.runOnUiThread(() -> {
//                thread = null;
//                dialog.setContent(R.string.tip_download_finished);
//            });
//        });
}

//    private void startThread(Runnable run) {
//        if (thread != null) {
//            thread.interrupt();
//        }
//        thread = new Thread(run);
//        thread.start();
//    }

//}
