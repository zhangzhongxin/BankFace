package com.hjsy.bankface

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hjsy.bankface.service.MyBackgroundService
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import android.Manifest

class MainActivity : ComponentActivity() {

    private lateinit var systemInfoTextView: TextView
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 获取输出系统信息的 TextView 组件
        systemInfoTextView = findViewById(R.id.systemInfoTextView)
        // 判断权限
        if (checkPermissions()) {
            // 已经获取权限，获取并显示系统信息
            getAndDisplaySystemInfo()
        } else {
            // 请求获取权限
            requestPermissions()
        }

        // 启动后台服务
        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        startService(serviceIntent)
    }


    private fun checkPermissions(): Boolean {
        // 检查是否已经获取了读取和写入外部存储的权限
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    // 请求获取权限
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    // 处理权限请求结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                getAndDisplaySystemInfo()
            } else {
                Toast.makeText(this, "权限被拒绝，无法保存文件", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 获取并显示系统信息
    private fun getAndDisplaySystemInfo() {
        val systemInfo = JSONObject()
        systemInfo.put("brand", Build.BRAND)
        systemInfo.put("model", Build.MODEL)
        systemInfo.put("device", Build.DEVICE)
        systemInfo.put("manufacturer", Build.MANUFACTURER)
        systemInfo.put("product", Build.PRODUCT)
        systemInfo.put("version_release", Build.VERSION.RELEASE)
        systemInfo.put("version_sdk", Build.VERSION.SDK_INT)

        val infoString = systemInfo.toString(4)
        systemInfoTextView.text = infoString

        saveSystemInfoToFile(infoString)
    }

    // 将系统信息保存到文件
    private fun saveSystemInfoToFile(info: String) {
        val folder = File(Environment.getExternalStorageDirectory(), "SystemInfo")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val file = File(folder, "system_info.json")
        try {
            val writer = FileWriter(file)
            writer.write(info)
            writer.flush()
            writer.close()
            Toast.makeText(this, "系统信息已保存到 ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "保存文件时出错", Toast.LENGTH_SHORT).show()
        }
    }
}