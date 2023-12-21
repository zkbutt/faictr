package top.feadre.faictr.activitys;

import android.content.Context;

import top.feadre.faictr.flib.base.FSPHistory;
import top.feadre.faictr.services.adb.ADBCommands;


public class AdbActivityFSPHistory extends FSPHistory<String> {
    public AdbActivityFSPHistory(Context context, String keySp, String saveKey, int historyLimit) {
        super(context, keySp, saveKey, historyLimit);
    }

    @Override
    public String parse_data_obj2sp(String d) {
        return d;
    }

    @Override
    public String parse_data_sp2obj(String s) {
        return s;
    }

    @Override
    public void init() {
        this.datas.add("ls -l");
        this.datas.add("cat /data/local/tmp/" + FMainHelp4ADBShellService.FILE_LOG);
        this.datas.add("rm /data/local/tmp/" + FMainHelp4ADBShellService.FILE_LOG);
        this.datas.add(String.format(ADBCommands.Order.SYS_CD, "/data/local/tmp"));
        super.init();
    }
}
