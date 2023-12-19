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

package top.feadre.faictr.flib.fviews.dialog_edit;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


import top.feadre.faictr.R;

public class FSimpleRecyclerAdapter extends BaseRecyclerAdapter<EntityItem4SimpleRecyclerAdapter> {

    public FSimpleRecyclerAdapter() {
        super();
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, EntityItem4SimpleRecyclerAdapter item) {
        holder.text(android.R.id.text1, item.getTitle());
        holder.text(android.R.id.text2, item.getContent());
        holder.textColorId(android.R.id.text2, R.color.xui_config_color_light_blue_gray);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return android.R.layout.simple_list_item_2;
    }
}
