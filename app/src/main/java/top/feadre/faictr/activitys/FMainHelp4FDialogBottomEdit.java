package top.feadre.faictr.activitys;


import androidx.annotation.NonNull;

import java.util.LinkedList;

import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.base.FSPHistory;
import top.feadre.faictr.flib.fviews.FDialogBottomEdit;
import top.feadre.faictr.flib.fviews.dialog_edit.EntityItem4SimpleRecyclerAdapter;

/**
 * @author Administrator
 * @version 1.0.0
 * @projectName faictr
 * @description: 用于连接 FDialogBottomEdit 和 fspHistory
 * @date :2023/12/16 21:43
 */
public class FMainHelp4FDialogBottomEdit extends FDialogBottomEdit {
    private static final String TAG = "FMainH4FProgressDialog";
    private final FMainActivity fMainActivity;
    private final FSPHistory<EntityItem4SimpleRecyclerAdapter> fspHistory;

    public FMainHelp4FDialogBottomEdit(@NonNull FMainActivity fMainActivity) {
        // 创建完成后 有一个 mAdapter datas
        super(fMainActivity);
        this.fMainActivity = fMainActivity;
        // 需要复写多字符解析方法
        fspHistory = new FSPHistory<EntityItem4SimpleRecyclerAdapter>(
                fMainActivity,
                FCFGBusiness.SPSet.KEY_MAIN,
                FCFGBusiness.SPSet.LINKED_IP_SIZE,
                50) {
            @Override
            public String parse_data_obj2sp(EntityItem4SimpleRecyclerAdapter d) {
                return d.getTitle() + FCFGBusiness.PARSE_STRING + d.getContent();
            }

            @Override
            public EntityItem4SimpleRecyclerAdapter parse_data_sp2obj(String s) {
                String[] split = s.split(FCFGBusiness.PARSE_STRING);
                return new EntityItem4SimpleRecyclerAdapter(split[0], split[1]);
            }
        };
        loadDatas();//初始化数据
    }

    private void loadDatas() {
        //数据加载
        fspHistory.init();
        LinkedList<EntityItem4SimpleRecyclerAdapter> datas = fspHistory.getDatas();
        FTools.log_d(TAG, " loadDatas fspHistory.getDatas = " + fspHistory.getDatas().size());
        //更新view
//        this.setDatas(datas); //调这个ADD 又会反向添加到 fspHistory 中 这个要着重复
        for (EntityItem4SimpleRecyclerAdapter d : datas) {
            super.addData(d); //这里用this要重复
        }

        FTools.log_d(TAG, " loadDatas "
                + " fspHistory.getDatas = " + fspHistory.getDatas().size()
                + " mAdapter.getData() = " + this.getDatas().size()
        );
        this.update_tv_dbe_mid();
    }

    /**
     * 添加一条数据
     */
    @Override
    public boolean addData(EntityItem4SimpleRecyclerAdapter e) {
        boolean b = super.addData(e);
        FTools.log_d(TAG, "addData "
                + " fspHistory.getDatas().size() = " + fspHistory.getDatas().size()
                + " mAdapter.getData() = " + this.getDatas().size()
        );
        if (b) {
            fspHistory.add(e);
        }
        FTools.log_d(TAG, "addData "
                + " fspHistory.getDatas().size() = " + fspHistory.getDatas().size()
                + " mAdapter.getData() = " + this.getDatas().size()
        );
        return b;
    }

    /**
     * 清空数组
     */
    @Override
    protected void onClearDatas() {
        super.onClearDatas();
        fspHistory.clear_data();
    }

    /**
     * 删除一个数据
     */
    @Override
    protected void onRemoveData(int position) {
        super.onRemoveData(position);
        fspHistory.del(position);
    }
}
