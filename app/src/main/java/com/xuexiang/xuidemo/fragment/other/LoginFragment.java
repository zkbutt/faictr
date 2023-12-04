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

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.DrawableUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.activity.MainActivity;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.utils.PrivacyUtils;
import com.xuexiang.xuidemo.utils.SettingSPUtils;
import com.xuexiang.xuidemo.utils.TokenUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xuidemo.utils.sdkinit.UMengInit;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.RandomUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xuexiang.xuidemo.fragment.other.ServiceProtocolFragment.KEY_PROTOCOL_TITLE;


/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(anim = CoreAnim.none)
public class LoginFragment extends BaseFragment {

    @BindView(R.id.et_phone_number)
    MaterialEditText etPhoneNumber;
    @BindView(R.id.et_verify_code)
    MaterialEditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    RoundButton btnGetVerifyCode;

    @BindView(R.id.cb_protocol)
    CheckBox cbProtocol;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;

    private View mJumpView;

    private CountDownButtonHelper mCountDownHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        titleBar.setLeftImageDrawable(DrawableUtils.setTint(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close), ThemeUtils.getMainThemeColor(getContext())));
        titleBar.setActionTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
        mJumpView = titleBar.addAction(new TitleBar.TextAction(R.string.xui_jump) {
            @Override
            public void performAction(View view) {
                onLoginSuccess();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        mCountDownHelper = new CountDownButtonHelper(btnGetVerifyCode, 60);
        //隐私政策弹窗
        SettingSPUtils spUtils = SettingSPUtils.getInstance();
        boolean isAgreePrivacy = spUtils.isAgreePrivacy();
        if (!isAgreePrivacy) {
            PrivacyUtils.showPrivacyDialog(getContext(), (dialog, which) -> {
                dialog.dismiss();
                spUtils.setIsAgreePrivacy(true);
                UMengInit.init();
                // 应用市场不让默认勾选
//                ViewUtils.setChecked(cbProtocol, true);
            });
        }
        cbProtocol.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spUtils.setIsAgreePrivacy(isChecked);
            refreshButton(isChecked);
        });
        ViewUtils.setChecked(cbProtocol, isAgreePrivacy);
        refreshButton(isAgreePrivacy);
    }

    @SingleClick
    @OnClick({R.id.btn_get_verify_code, R.id.btn_login, R.id.tv_other_login, R.id.tv_forget_password, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_verify_code:
                if (etPhoneNumber.validate()) {
                    getVerifyCode(etPhoneNumber.getEditValue());
                }
                break;
            case R.id.btn_login:
                if (etPhoneNumber.validate()) {
                    if (etVerifyCode.validate()) {
                        loginByVerifyCode(etPhoneNumber.getEditValue(), etVerifyCode.getEditValue());
                    }
                }
                break;
            case R.id.tv_other_login:
                XToastUtils.info("其他登录方式");
                break;
            case R.id.tv_forget_password:
                XToastUtils.info("忘记密码");
                break;
            case R.id.tv_user_protocol:
                openPage(ServiceProtocolFragment.class, KEY_PROTOCOL_TITLE, ResUtils.getString(getContext(), R.string.title_user_protocol));
                break;
            case R.id.tv_privacy_protocol:
                openPage(ServiceProtocolFragment.class, KEY_PROTOCOL_TITLE, ResUtils.getString(getContext(), R.string.title_privacy_protocol));
                break;
            default:
                break;
        }
    }

    private void refreshButton(boolean isChecked) {
        ViewUtils.setEnabled(btnLogin, isChecked);
        ViewUtils.setEnabled(mJumpView, isChecked);
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode(String phoneNumber) {
        // TODO: 2019-11-18 这里只是界面演示而已
        XToastUtils.warning("只是演示，验证码请随便输");
        mCountDownHelper.start();
    }

    /**
     * 根据验证码登录
     *
     * @param phoneNumber 手机号
     * @param verifyCode  验证码
     */
    private void loginByVerifyCode(String phoneNumber, String verifyCode) {
        // TODO: 2019-11-18 这里只是界面演示而已
        onLoginSuccess();
    }

    /**
     * 登录成功的处理
     */
    private void onLoginSuccess() {
        String token = RandomUtils.getRandomNumbersAndLetters(16);
        if (TokenUtils.handleLoginSuccess(token)) {
            popToBack();
            ActivityUtils.startActivity(MainActivity.class);
        }
    }


    @Override
    public void onDestroyView() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        super.onDestroyView();
    }
}
