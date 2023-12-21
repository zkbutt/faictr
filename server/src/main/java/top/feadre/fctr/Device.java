package top.feadre.fctr;

import android.graphics.Point;
import android.os.Build;
import android.os.RemoteException;
import android.view.IRotationWatcher;
import android.view.InputEvent;

import top.feadre.fctr.BuildConfig;

import top.feadre.fctr.wrappers.ServiceManager;

public final class Device {

    private final ServiceManager serviceManager = new ServiceManager();
    private ScreenInfo screenInfo;
    private RotationListener rotationListener;

    public Device(Options options) {
        screenInfo = computeScreenInfo(options.getMaxSize());

        registerRotationWatcher(new IRotationWatcher.Stub() {
            @Override
            public void onRotationChanged(int rotation) throws RemoteException {
                synchronized (Device.this) {
                    screenInfo = screenInfo.withRotation(rotation);

                    // notify
                    if (rotationListener != null) {
                        rotationListener.onRotationChanged(rotation);
                    }
                }
            }
        });
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public synchronized ScreenInfo getScreenInfo() {
        return screenInfo;
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private ScreenInfo computeScreenInfo(int maxSize) {
        // 计算视频大小和视频内内容的填充 Compute the video size and the padding of the content inside this video.
        // 原则: Principle:
        // -将屏幕的大边长缩放到maxSize(如果需要); - scale down the great side of the screen to maxSize (if necessary);
        // -缩小另一边，以保持长宽比; - scale down the other side so that the aspect ratio is preserved;
        // --将该值四舍五入到最接近8的倍数(H.264只接受8的倍数) round this value to the nearest multiple of 8 (H.264 only accepts multiples of 8)
        DisplayInfo displayInfo = serviceManager.getDisplayManager().getDisplayInfo();
        boolean rotated = (displayInfo.getRotation() & 1) != 0;
        Size deviceSize = displayInfo.getSize();
        int w = deviceSize.getWidth() & ~7; // 确保是8的倍数 in case it's not a multiple of 8
        int h = deviceSize.getHeight() & ~7;
        if (maxSize > 0) {
            if (BuildConfig.DEBUG && maxSize % 8 != 0) {
                throw new AssertionError("Max size must be a multiple of 8");
            }
            boolean portrait = h > w;
            int major = portrait ? h : w;
            int minor = portrait ? w : h;
            if (major > maxSize) {
                int minorExact = minor * maxSize / major;
                // +4 to round the value to the nearest multiple of 8
                minor = (minorExact + 4) & ~7;
                major = maxSize;
            }
            w = portrait ? minor : major;
            h = portrait ? major : minor;
        }
        Size videoSize = new Size(w, h);
        Ln.d("computeScreenInfo size"
                + " deviceSize = " + deviceSize
                + " videoSize = " + videoSize
        );
        return new ScreenInfo(deviceSize, videoSize, rotated);
    }

    public Point getPhysicalPoint(Position position) {
        @SuppressWarnings("checkstyle:HiddenField") // it hides the field on purpose, to read it with a lock
        ScreenInfo screenInfo = getScreenInfo(); // read with synchronization
        Size videoSize = screenInfo.getVideoSize();
        Size clientVideoSize = position.getScreenSize();
        if (!videoSize.equals(clientVideoSize)) {
            // The client sends a click relative to a video with wrong dimensions,
            // the device may have been rotated since the event was generated, so ignore the event
            return null;
        }
        Size deviceSize = screenInfo.getDeviceSize();
        Point point = position.getPoint();
        int scaledX = point.x * deviceSize.getWidth() / videoSize.getWidth();
        int scaledY = point.y * deviceSize.getHeight() / videoSize.getHeight();
        return new Point(scaledX, scaledY);
    }

    public boolean injectInputEvent(InputEvent inputEvent, int mode) {
        return serviceManager.getInputManager().injectInputEvent(inputEvent, mode);
    }

    public boolean isScreenOn() {
        return serviceManager.getPowerManager().isScreenOn();
    }

    public void registerRotationWatcher(IRotationWatcher rotationWatcher) {
        serviceManager.getWindowManager().registerRotationWatcher(rotationWatcher);
    }

    public synchronized void setRotationListener(RotationListener rotationListener) {
        this.rotationListener = rotationListener;
    }

    public Point NewgetPhysicalPoint(Point point) {
        @SuppressWarnings("checkstyle:HiddenField") // it hides the field on purpose, to read it with a lock
        ScreenInfo screenInfo = getScreenInfo(); // read with synchronization
        Size videoSize = screenInfo.getVideoSize();
//        Size clientVideoSize = position.getScreenSize();

        Size deviceSize = screenInfo.getDeviceSize();
//        Point point = position.getPoint();
        int scaledX = point.x * deviceSize.getWidth() / videoSize.getWidth();
        int scaledY = point.y * deviceSize.getHeight() / videoSize.getHeight();
        return new Point(scaledX, scaledY);
    }


    public interface RotationListener {
        void onRotationChanged(int rotation);
    }

}
