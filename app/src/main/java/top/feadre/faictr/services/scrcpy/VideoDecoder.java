package top.feadre.faictr.services.scrcpy;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class VideoDecoder {
    // 视频解码到 Surface
    private static final String TAG = "VideoDecoder";
    private MediaCodec mediaCodec; //多媒体编解码器
    private Worker mWorker;
    //自身状态没有设置方法
    private AtomicBoolean is_mediacodec_config = new AtomicBoolean(false);

    public void decodeSample(byte[] data, int offset, int size,
                             long presentationTimeUs, int flags) {
        if (mWorker != null) {
            mWorker.decodeSample(data, offset, size, presentationTimeUs, flags);
        }
    }

    public void configure_worker4surface(Surface surface, int width, int height,
                                         ByteBuffer csd0, ByteBuffer csd1) {
        if (mWorker != null) {
            mWorker.configure(surface, width, height, csd0, csd1);
        }
    }

    public void start_worker_thread() {
        //服务开始时 恢复时
        if (mWorker == null) {
            mWorker = new Worker();
            mWorker.setIsWorkerRunning(true);
            mWorker.start();
        }
    }

    public void stop_worker_thread() {
        if (mWorker != null) {
            mWorker.setIsWorkerRunning(false);
            mWorker = null;
            is_mediacodec_config.set(false);
        }
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec = null;
        }
    }

    private class Worker extends Thread {
        /**
         * 把接收和解码视频流
         */
        private AtomicBoolean isWorkerRunning = new AtomicBoolean(false);

        private void setIsWorkerRunning(boolean isRunning) {
            isWorkerRunning.set(isRunning);
        }

        private void configure(Surface surface, int width, int height,
                               ByteBuffer csd0, ByteBuffer csd1) {
            //默认没有解码配置  false
            if (VideoDecoder.this.is_mediacodec_config.get()) {
                //如果已经配置过 需先停止 mediaCodec
                VideoDecoder.this.is_mediacodec_config.set(false);
                mediaCodec.stop();
            }

            MediaFormat format = MediaFormat.createVideoFormat("video/avc", width, height);
            format.setByteBuffer("csd-0", csd0);
            format.setByteBuffer("csd-1", csd1);
            try {
                mediaCodec = MediaCodec.createDecoderByType("video/avc");
            } catch (IOException e) {
                Log.e(TAG, "configure: MediaCodec 失败");
                throw new RuntimeException("MediaCodec.createDecoderByType 失败---------------", e);
            }
            // 这个有可能要 java.lang.IllegalArgumentException: The surface has been released 报错
            mediaCodec.configure(format, surface, null, 0);
            mediaCodec.start();
            VideoDecoder.this.is_mediacodec_config.set(true);
            Log.d(TAG, "configure: 成功");
        }


        @SuppressWarnings("deprecation")
        public void decodeSample(byte[] data, int offset, int size,
                                 long presentationTimeUs, int flags) {
            try {
                if (VideoDecoder.this.is_mediacodec_config.get() && isWorkerRunning.get()) {
                    int index = mediaCodec.dequeueInputBuffer(-1);
                    if (index >= 0) {
                        ByteBuffer buffer;

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            buffer = mediaCodec.getInputBuffers()[index];
                            buffer.clear();
                        } else {
                            buffer = mediaCodec.getInputBuffer(index);
                        }
                        if (buffer != null) {
                            buffer.put(data, offset, size);
                            mediaCodec.queueInputBuffer(index, 0, size, 0, flags);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "decodeSample:出错  这里久了不动就要出错 " + e.getMessage());
            }

        }

        @Override
        public void run() {
            try {
                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                while (isWorkerRunning.get()) {
                    //如果已配置
                    if (VideoDecoder.this.is_mediacodec_config.get()) {
                        int index = mediaCodec.dequeueOutputBuffer(info, 0);
                        if (index >= 0) {
                            // setting true is telling system to render frame onto Surface
                            mediaCodec.releaseOutputBuffer(index, true);
                            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                                    == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                                break;
                            }
                        }
                    } else {
                        // 等待媒体配置
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}