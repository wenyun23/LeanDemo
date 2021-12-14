package com.example.leandemo

import android.app.Application
import cn.leancloud.LeanCloud
import cn.leancloud.LCObject

//如果是一个 Android 项目，则向 Application 类的 onCreate 方法添加：
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // 提供 this、App ID、绑定的自定义 API 域名作为参数
        // 提供 this、App ID、App Key、Server Host 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.LeanCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        LeanCloud.initialize(
            this,
            "rwq382tIJ5A2EfrFhhiLFPIL-gzGzoHsz",
            "n8eDmKkvFoWIrktlN62eovSN",
            "https://rwq382ti.lc-cn-n1-shared.com"
        )

        //验证是否连接到服务器
//        val testObject = LCObject("TestObject")
//        testObject.put("words", "Hello world!")
//        testObject.saveInBackground().blockingSubscribe()
    }
}