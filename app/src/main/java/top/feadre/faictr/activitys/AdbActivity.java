package top.feadre.faictr.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xui.utils.XToastUtils;

import java.util.LinkedList;

import top.feadre.faictr.R;
import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.flib.base.Thread2Main;
import top.feadre.faictr.flib.net.FNetTools;
import top.feadre.faictr.services.adb.ADBCommands;
import top.feadre.faictr.services.adb.ADBShellConn;
import top.feadre.faictr.services.adb.ADBShellOrderEntity;
import top.feadre.faictr.services.adb.ADBShellService;


public class AdbActivity extends Activity implements
        TextView.OnEditorActionListener,
        Thread2Main.OnThread2MainCallback<String, String, String>,
        ADBShellService.OnADBShellResListener {
    private static final String TAG = "AdbActivity";
    private static final int MAX_COMMAND_COUNT = 30;
    public TextView tv_res;
    private EditText et_command;
    private ScrollView sv;
    private static final int MENU_TV_CTRL_C = 1;
    private static final int MENU_TV_AUTOSCROLL = 2;
    private static final int MENU_TV_EXIT = 3;
    private static final int MENU_TV_COPY_FILE = 4;
    private static final int MENU_TV_T001 = 5;
    private static final int MENU_TV_T002 = 6;
    private boolean is_sv_auto_scroll = true;
    private String control_ip_str;
    private int port;
    private ADBShellConn conn_adb_shell;
    private AdbActivityFSPHistory adbActivityFSPHistory;
    private AdbHelp4FProgressDialog adbHelp4FProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        control_ip_str = intent.getStringExtra(FCFGBusiness.KeyIntent.MAIN_CONTROL_IP);
        port = intent.getIntExtra(FCFGBusiness.KeyIntent.MAIN_PORT, 5555);

        setContentView(R.layout.activity_adb);

        tv_res = (TextView) findViewById(R.id.tv_res);
//        tv_res.setBackgroundColor(Color.WHITE); //debug
        et_command = (EditText) findViewById(R.id.et_command);
        sv = (ScrollView) findViewById(R.id.sv);


        et_command.setImeActionLabel("Run", EditorInfo.IME_ACTION_DONE);
        et_command.setOnEditorActionListener(this);
//        et_command.setOnLongClickListener(this);

        registerForContextMenu(et_command);
        registerForContextMenu(tv_res);

        startADBCoon();
    }

    private void startADBCoon() {
        adbHelp4FProgressDialog = new AdbHelp4FProgressDialog(this);
        adbActivityFSPHistory = new AdbActivityFSPHistory(this,
                FCFGBusiness.SPSet.KEY_ADB,
                FCFGBusiness.SPSet.COMMAND_HISTORY_SIZE,
                MAX_COMMAND_COUNT
        );
        setTitle("ADB Shell - " + control_ip_str + ":" + port);
        conn_adb_shell = new ADBShellConn(control_ip_str, port,
                this, this, this);

        adbHelp4FProgressDialog.showDialog(getResources().getString(R.string.adb_linking));
        adbHelp4FProgressDialog.updateProgress(2, control_ip_str + "开始连接");

        new FNetTools(new Thread2Main.OnThread2MainCallback<String, String, String>() {
            @Override
            public void on_fun_running(int status_run, String obj) {
            }

            @Override
            public void on_fun_res_success(int status_run, String obj) {
                Intent intent = new Intent(AdbActivity.this, ADBShellService.class);
                bindService(intent, conn_adb_shell, Context.BIND_AUTO_CREATE);
            }

            @Override
            public void on_fun_res_fail(int status_run, String obj) {
                finish();
                XToastUtils.error(control_ip_str + " 网络无法连通！", Toast.LENGTH_LONG);
            }
        }).fping(control_ip_str, 4);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //当选择弹出 菜单时 --- onLongClick(返回true,不需要长按侦听)   registerForContextMenu(et_command); ->  onCreateContextMenu ->  onContextItemSelected
        Log.d(TAG, "onContextItemSelected: item.getTitle() = " + item.getTitle());
        String fileNameJar = "FJarT002.jar";
        String file_name_base64 = "menu_tv_copy_file";
        if (item.getItemId() == 0) {
            et_command.setText(item.getTitle());
        } else {
            switch (item.getItemId()) {
                case MENU_TV_CTRL_C:
                    conn_adb_shell.getServiceAdbShell().executeCtrlC();
                    break;

                case MENU_TV_AUTOSCROLL:
                    item.setChecked(!item.isChecked());
                    is_sv_auto_scroll = item.isChecked();
                    break;

                case MENU_TV_EXIT:
                    finish();
                    break;
                case MENU_TV_COPY_FILE:

                    ADBCommands.copy_assets_jar4shell(this, fileNameJar, file_name_base64, conn_adb_shell.getServiceAdbShell(), -999);
                    String order = String.format(ADBCommands.Order.FILE_COPY_base642file, file_name_base64, fileNameJar);
                    conn_adb_shell.getServiceAdbShell().executeCommand(order + '\n', -999);
                    break;
                case MENU_TV_T001:
                    //这是一个卡顿方法
                    String temp_str = " CLASSPATH=/data/local/tmp/" + fileNameJar + " >aaa app_process / top.feadre.fctr.Server ";
                    temp_str += "/" + "192.168.1.1" + " " + 666 + " " + 1232112 + ";\n";
                    Log.d(TAG, "onResText: RUN_JAR命令 = " + temp_str);
                    conn_adb_shell.getServiceAdbShell().executeCommand(temp_str, -999, true);
                    break;
                case MENU_TV_T002:
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        //当ET提交时 ----
        Log.d(TAG, "onEditorAction: " + "ET动作侦听，回车  i = " + i);
//        if (et_command.getText().length() == 0 || connection == null)
        if (et_command.getText().length() == 0)
            return false;

        if (i == EditorInfo.IME_ACTION_DONE) {//是提交回车时
            String text = et_command.getText().toString();
//            buff_sb.append(text).append('\n');
            adbActivityFSPHistory.add(text);

            if (conn_adb_shell.getServiceAdbShell() != null) {
                conn_adb_shell.getServiceAdbShell().executeCommand(text + '\n');
            }

            et_command.setText("");

            return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // 确定是哪个 view 在创建菜单 --- onLongClick(返回true) -> registerForContextMenu(et_command); ->  onCreateContextMenu -> ->  onContextItemSelected
        Log.d(TAG, "onCreateContextMenu: ");
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v == et_command) {
            LinkedList<String> datas = adbActivityFSPHistory.getDatas();
            for (int i = datas.size() - 1; i >= 0; i--) {
                menu.add(Menu.NONE, 0, Menu.NONE, datas.get(i));
            }
        } else {
            menu.add(Menu.NONE, MENU_TV_CTRL_C, Menu.NONE, "Send Ctrl+C");

            MenuItem autoscroll = menu.add(Menu.NONE, MENU_TV_AUTOSCROLL, Menu.NONE, "Auto-scroll terminal");
            autoscroll.setCheckable(true);
            autoscroll.setChecked(is_sv_auto_scroll);

            menu.add(Menu.NONE, MENU_TV_EXIT, Menu.NONE, "Exit Terminal");
            menu.add(Menu.NONE, MENU_TV_COPY_FILE, Menu.NONE, "COPY_JAR");
            menu.add(Menu.NONE, MENU_TV_T001, Menu.NONE, "T001");
            menu.add(Menu.NONE, MENU_TV_T002, Menu.NONE, "T002");
        }
    }

    @Override
    public void on_fun_running(int status_run, String obj) {
        String[] _ss = obj.split(FCFGBusiness.PARSE_STRING);
        adbHelp4FProgressDialog.updateProgress(Integer.parseInt(_ss[1]), _ss[0]);
    }

    @Override
    public void on_fun_res_success(int status_run, String obj) {
        XToastUtils.success(obj);
    }

    @Override
    public void on_fun_res_fail(int status_run, String obj) {
        XToastUtils.error(obj);
        adbHelp4FProgressDialog.close();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn_adb_shell.getServiceBound()) {
            unbindService(conn_adb_shell);
            conn_adb_shell.close();
        }
    }


    @Override
    public void onInitSuccess(ADBShellOrderEntity objOrderEntity) {
        FToolsAndroid.ftoast(this, "shell 初始化成功 ...");
    }


    @Override
    public void onResTextLine(String resText, int orderId, int isFinish) {
        Log.d(TAG, "onResTextLine: resText = " + resText);
    }

    @Override
    public void onResText(ADBShellOrderEntity objOrderEntity) {
        byte[] orderByte = objOrderEntity.getOrderByte();
        String s = null;
        if (orderByte != null) {
            s = String.valueOf(ADBCommands.bytes2texts(orderByte));
        }
        Log.d(TAG, "onResText: "
                + "  objOrderEntity.getResText() 不拉平 = " + objOrderEntity.getResText()
                + "  objOrderEntity.getOrderByte() = " + s
                + "  objOrderEntity.getOrderId() = " + objOrderEntity.getOrderId()
        );

        String text1 = tv_res.getText() + objOrderEntity.getResText();
        tv_res.setText(text1);

        if (is_sv_auto_scroll) { //要延迟一点
            tv_res.post(new Runnable() {
                @Override
                public void run() {
                    sv.smoothScrollTo(0, tv_res.getBottom());
                }
            });
        }
    }
}