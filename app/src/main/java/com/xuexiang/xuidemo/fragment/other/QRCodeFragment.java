/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.other;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xqrcode.util.QRCodeAnalyzeUtils;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.base.webview.AgentWebActivity;
import com.xuexiang.xuidemo.fragment.components.imageview.DrawablePreviewFragment;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.app.SocialShareUtils;
import com.xuexiang.xutil.display.ImageUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.net.NetworkUtils;

import java.io.File;

import butterknife.BindView;

import static com.xuexiang.xuidemo.base.webview.AgentWebFragment.KEY_URL;
import static com.xuexiang.xuidemo.fragment.components.imageview.DrawablePreviewFragment.DRAWABLE_ID;

/**
 * @author xuexiang
 * @since 2019/1/7 上午9:14
 */
@Page(name = "扫码关注")
public class QRCodeFragment extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.iv_qq_group)
    ImageView ivQqGroup;
    @BindView(R.id.iv_win_xin)
    ImageView ivWinXin;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qrcode;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        ivQqGroup.setTag("qq_group");
        ivQqGroup.setOnClickListener(this);
        ivQqGroup.setOnLongClickListener(this);
        ivWinXin.setTag("wei_xin_subscription_number");
        ivWinXin.setOnClickListener(this);
        ivWinXin.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.iv_qq_group:
                bundle.putInt(DRAWABLE_ID, R.drawable.img_xui_qq);
                openPage(DrawablePreviewFragment.class, bundle);
                break;
            case R.id.iv_win_xin:
                bundle.putInt(DRAWABLE_ID, R.drawable.img_winxin_subscription_number);
                openPage(DrawablePreviewFragment.class, bundle);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v instanceof ImageView) {
            showBottomSheetList(v, ImageUtils.drawable2Bitmap(((ImageView) v).getDrawable()));
        }
        return true;
    }

    @NonNull
    private String getImgFilePath(View v) {
        return FileUtils.getDiskFilesDir() + File.separator + v.getTag() + ".png";
    }

    private void showBottomSheetList(View v, final Bitmap bitmap) {
        final String imgPath = getImgFilePath(v);
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("发送给朋友")
                .addItem("保存图片")
                .addItem("识别图中的二维码")
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    boolean result = checkFile(imgPath, bitmap);
                    switch (position) {
                        case 0:
                            if (result) {
                                SocialShareUtils.sharePicture(getActivity(), PathUtils.getUriForFile(FileUtils.getFileByPath(imgPath)));
                            } else {
                                XToastUtils.toast("图片发送失败!");
                            }
                            break;
                        case 1:
                            if (result) {
                                XToastUtils.toast("图片保存成功:" + imgPath);
                            } else {
                                XToastUtils.toast("图片保存失败!");
                            }
                            break;
                        case 2:
                            if (result) {
                                XQRCode.analyzeQRCode(imgPath, new QRCodeAnalyzeUtils.AnalyzeCallback() {
                                    @Override
                                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                        if (NetworkUtils.isUrlValid(result)) {
                                            goWeb(result);
                                        }
                                    }

                                    @Override
                                    public void onAnalyzeFailed() {
                                        XToastUtils.toast("解析二维码失败！");
                                    }
                                });
                            } else {
                                XToastUtils.toast("二维码识别失败!");
                            }
                            break;
                        default:
                            break;
                    }
                })
                .build()
                .show();
    }

    private boolean checkFile(String imgPath, Bitmap bitmap) {
        boolean result = FileUtils.isFileExists(imgPath);
        if (!result) {
            result = ImageUtils.save(bitmap, imgPath, Bitmap.CompressFormat.PNG);
        }
        return result;
    }

    /**
     * 请求浏览器
     *
     * @param url
     */
    public void goWeb(final String url) {
        Intent intent = new Intent(getContext(), AgentWebActivity.class);
        intent.putExtra(KEY_URL, url);
        startActivity(intent);
    }
}
