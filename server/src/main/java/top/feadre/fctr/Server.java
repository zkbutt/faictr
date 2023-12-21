package top.feadre.fctr;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public final class Server {
    private static String ip = null;

    private Server() {
        // not instantiable
    }

    private static void scrcpy(Options options) throws IOException {
        final Device device = new Device(options);
        Ln.d("scrcpy --- ip = " + ip);
        try (DroidConnection connection = DroidConnection.open(ip)) {
            ScreenEncoder screenEncoder = new ScreenEncoder(options.getBitRate());

            // asynchronous
            startEventController(device, connection);

            try {
                // synchronous 这个报错  java.lang.NullPointerException: Attempt to invoke virtual method 'java.io.OutputStream top.feadre.fctr.DroidConnection.getOutputStream()' on a null object reference
                screenEncoder.streamScreen(device, connection.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                // this is expected on close
                Ln.d("scrcpy --- Screen streaming stopped"); //这里日志文件有输出
            }
        }
    }

    private static void startEventController(final Device device, final DroidConnection connection) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new EventController(device, connection).control();
                } catch (IOException e) {
                    // this is expected on close
                    Ln.d("startEventController --- Event controller stopped"); //这里日志文件有输出
                }
            }
        }).start();
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private static Options createOptions(String... args) {
        Options options = new Options();

        if (args.length < 1) {
            return options;
        }
        ip = String.valueOf(args[0]); // /192.168.22.122


        if (args.length < 2) {
            return options;
        }
        int maxSize = Integer.parseInt(args[1]) & ~7; // 传输长宽的最大尺寸 确保是8的倍数 multiple of 8
        options.setMaxSize(maxSize);

        if (args.length < 3) {
            return options;
        }
        int bitRate = Integer.parseInt(args[2]); // 4096000 速率
        options.setBitRate(bitRate);

        if (args.length < 4) {
            return options;
        }
        // use "adb forward" instead of "adb tunnel"? (so the server must listen)
        boolean tunnelForward = Boolean.parseBoolean(args[3]);
        options.setTunnelForward(tunnelForward);
        return options;
    }

    public static void main(String... args) throws Exception {
        /*
         *  CLASSPATH=/data/local/tmp/scrcpy-server.jar app_process >>aaa / top.feadre.fctr.Server /192.168.22.122 608 4096000;
         * [/192.168.22.122,608,4096000]
         * */
        System.out.println();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(dateFormat.format(date));

        Ln.d("main --- Server main: args" + Arrays.toString(args));

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Ln.e("uncaughtException --- Exception on thread " + t, e); ////这里日志文件有输出
            }
        });

//        try {
//            //本机命令 由控制端去处理
//            Process cmd = Runtime.getRuntime().exec("rm /data/local/tmp/scrcpy-server.jar");
//            cmd.waitFor();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }

        Options options = createOptions(args);
        scrcpy(options);
    }
}

