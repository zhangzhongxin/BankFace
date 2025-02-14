package com.hjsy.bankface.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hjsy.bankface.MainActivity


class ShowAppReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 创建启动主活动的意图
        val mainIntent = Intent(context, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // 启动主活动
        context.startActivity(mainIntent)
    }
}