package top.feadre.faictr.activitys;


import androidx.annotation.NonNull;

import java.util.LinkedList;

import top.feadre.faictr.cfg.FCFGBusiness;
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
        super(fMainActivity);
        this.fMainActivity = fMainActivity;
        // 需要复写多字符解析方法
        fspHistory = new FSPHistory<EntityItem4SimpleRecyclerAdapter>(
                fMainActivity,
                FCFGBusiness.SPSet.KEY_MAIN,
                FCFGBusiness.SPSet.LINKED_IP_SIZE,
                10) {
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
    }

    public void loadDatas() {
        //数据同步
        fspHistory.init();
        LinkedList<EntityItem4SimpleRecyclerAdapter> datas = fspHistory.getDatas();
        this.setDatas(datas);
    }

    /**
     * 添加一条数据
     */
    @Override
    public void addData(EntityItem4SimpleRecyclerAdapter e) {
        super.addData(e);
        fspHistory.save();
    }

    /**
     * 清空数组
     */
    @Override
    protected void onClearDatas() {
        super.onClearDatas();
        fspHistory.save();
    }

    /**
     * 删除一个数据
     */
    @Override
    protected void onRemoveData(int position) {
        super.onRemoveData(position);
        fspHistory.save();
    }
}
