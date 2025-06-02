package com.example.demo;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.UUID;

public class DeviceMgr {

    private static String clientId = null;

    /**
     * 获取clientId, 野火IM用clientId唯一表示用户设备
     */
    public static String getClientId(Context context) {
        if (clientId != null) {
            return clientId;
        }

        String imei = null;
        try (
                RandomAccessFile fw = new RandomAccessFile(context.getFilesDir().getAbsoluteFile() + "/.wfcClientId", "rw");
                FileChannel chan = fw.getChannel();
        ) {
            FileLock lock = chan.lock();
            imei = fw.readLine();
            if (TextUtils.isEmpty(imei)) {
                //  迁移旧的clientId
                imei = PreferenceManager.getDefaultSharedPreferences(context).getString("mars_core_uid", "");
                if (TextUtils.isEmpty(imei)) {
                    if (TextUtils.isEmpty(imei)) {
                        imei = UUID.randomUUID().toString();
                    }
                    imei += System.currentTimeMillis();
                }
                fw.writeBytes(imei);
            }
            lock.release();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("getClientError", "" + ex.getMessage());
        }
        clientId = imei;
        Log.d("DeviceMgr", "clientId " + clientId);
        return imei;
    }
}
