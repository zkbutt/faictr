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

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xuexiang.xtask.logger.TaskLogger;

import top.feadre.faictr.R;
import top.feadre.faictr.fragments.T001Fragment;

public class TActivity_fragment extends AppCompatActivity {
    private static final String TAG = "TActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_fragment);

        String packageName = getPackageName();
        TaskLogger.setTag(TAG);
        TaskLogger.d("packageName = " + packageName);

        // 获取FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开始Fragment事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 创建并添加Fragment到指定的View
        T001Fragment t001Fragment = new T001Fragment();
        fragmentTransaction.add(R.id.fl_container, t001Fragment);

        // 提交事务
        fragmentTransaction.commit();

    }
}
