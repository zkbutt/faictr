package top.feadre.faictr.flib.base;

import android.os.Build;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import top.feadre.faictr.flib.FTools;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/27 19:32
 * @description:
 */
public class DelayTask<T, R> {
    private final Function<T, R> fun;
    private final int overtime;//秒数
    private int _overtime;//临时值
    private Thread thread;
    private OnTaskListener<R> onTaskListener;
    private AtomicBoolean isRun = new AtomicBoolean(true);

    public DelayTask(Function<T, R> fun,  int overtime) {
        this.fun = fun;
        this.overtime = overtime;
    }

    private Thread initThread(T dinput) {
        Thread thread = new Thread(() -> {
            _overtime = overtime;
            _overtime = _overtime * 10;
            while (_overtime != 0 && isRun.get()) {
                try {
                    Thread.sleep(100);//连接10秒钟
                    FTools.log_d("DelayTask", "耗时连接检测中: ........" + _overtime);
                } catch (InterruptedException e) {
                    e.printStackTrace(); //sleep 打断后报错
                    return;
                } finally {
                    _overtime--;
                }
            }

            if (isRun.get()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    R res = fun.apply(dinput);
                    if (onTaskListener != null) {
                        onTaskListener.onTaskRunOver(res);
                    }
                    FTools.log_d("DelayTask", "onTaskRunOver: ........" + _overtime);
                }
            } else {
                if (onTaskListener != null) {
                    onTaskListener.onTaskInterrupt();
                }
                FTools.log_d("DelayTask", "onTaskInterrupt: ........" + _overtime);
            }
        });
        return thread;
    }

    public void start(T dinput) {
        isRun.set(true);
        if (thread == null) {
            thread = initThread(dinput);
        }
        thread.start();
    }

    public void stop() {
        isRun.set(false);
        thread = null;
    }

    public void setOnTaskListener(OnTaskListener<R> onTaskListener) {
        this.onTaskListener = onTaskListener;
    }

    public interface OnTaskListener<T> {
        void onTaskRunOver(T res);

        void onTaskInterrupt();

    }

}
