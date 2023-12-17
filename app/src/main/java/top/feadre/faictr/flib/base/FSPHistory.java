package top.feadre.faictr.flib.base;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedList;

public abstract class FSPHistory<T> {
    private final String saveKey;//spkey
    private SharedPreferences obj_sp;
    protected LinkedList<T> datas;//数组
    private int sizeLimit;
    private String keySp;

    public FSPHistory(Context context, String keySp, String saveKey, int historyLimit) {
        this.obj_sp = context.getSharedPreferences(keySp, 0); //sp对象
        this.datas = new LinkedList<T>();
        this.sizeLimit = historyLimit;
        this.keySp = keySp;
        this.saveKey = saveKey;
        init();
    }

    /**
     * 多字符解析方法
     */
    public abstract String parse_data_obj2sp(T d); //    PARSE_STRING datas.get(i)

    /**
     * 多字符解析方法
     */
    public abstract T parse_data_sp2obj(String s);

    public void init() {
        // 从SP读取保存值
        int size = obj_sp.getInt(saveKey, 0); //获取设置的最大尺寸
        for (int i = 0; i < size; i++) {
            String _d = obj_sp.getString(saveKey + i, null);
            if (_d != null) {
                add(parse_data_sp2obj(_d));
            }
        }
    }

    public void add(T d) {
        int i = datas.indexOf(d);
        if (i != -1) {//如果已经有了
            datas.remove(i);
        } else {
            if (datas.size() >= sizeLimit) {
                datas.removeFirst();
            }
        }
        datas.addFirst(d);
        save();
    }

    public void save() {
        //重新保存
        SharedPreferences.Editor edit = obj_sp.edit();
        for (int i = 0; i < datas.size(); i++) {
            edit.putString(saveKey + i, parse_data_obj2sp(datas.get(i)));
        }
        edit.putInt(saveKey, datas.size());
        edit.apply();
    }


    public void clear_data() {
        SharedPreferences.Editor edit = obj_sp.edit();
        for (int i = 0; i < datas.size(); i++) {
            edit.putString(saveKey + i, null);
        }
        datas.clear();
        edit.putInt(saveKey, datas.size());
        edit.apply();
    }

    public String getKeySp() {
        return keySp;
    }

    public void setKeySp(String keySp) {
        this.keySp = keySp;
    }

    public String getSaveKey() {
        return saveKey;
    }

    public LinkedList<T> getDatas() {
        return datas;
    }

    public void setDatas(LinkedList<T> datas) {
        this.datas = datas;
    }

    public int getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }
}
