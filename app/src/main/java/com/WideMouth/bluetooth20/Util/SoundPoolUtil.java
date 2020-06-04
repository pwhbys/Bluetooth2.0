package com.WideMouth.bluetooth20.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import com.WideMouth.bluetooth20.R;

public class SoundPoolUtil {

    private static SoundPool soundPool;

    public static int message_received_sound_id = -1;

    public static int message_send_sound_id = -1;


    @SuppressLint("NewApi")

    public static void initialize(Context context) {

//        soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);

        //加载音频文件
        soundPool = new SoundPool.Builder().build();

        message_received_sound_id = soundPool.load(context, R.raw.message_received_sound, 1);
        message_send_sound_id = soundPool.load(context, R.raw.message_send_sound1, 1);

    }

    public static void play(int soundID) {
        if (soundID == message_received_sound_id) {
            soundPool.play(soundID, 1, 1, 0, 0, 1);
        } else if (soundID == message_send_sound_id) {
            soundPool.play(soundID, 0.5f, 0.5f, 0, 0, 1);
        }
    }

}
