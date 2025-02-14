package com.hjsy.bankface.util

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.hjsy.bankface.MainActivity

/**
 * 创建一个广播接收器类，用于接收系统开机完成的广播，并在接收到广播后启动你的应用的主活动。
 */
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 检查接收到的广播是否是系统开机完成的广播
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Received BOOT_COMPLETED broadcast")
            Toast.makeText(context, "Received BOOT_COMPLETED broadcast", Toast.LENGTH_LONG).show()
            // 创建启动主活动的意图
            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            // 启动主活动
            context.startActivity(mainIntent)
        }
    }
}