package com.example.leandemo

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.leancloud.LCException
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import cn.leancloud.LCUser
import cn.leancloud.livequery.LCLiveQuery
import cn.leancloud.livequery.LCLiveQueryEventHandler
import cn.leancloud.livequery.LCLiveQuerySubscribeCallback
import io.reactivex.disposables.Disposable

//定义一个viewModel类，好对数据进行观察
class MyLivedate(application: Application) : AndroidViewModel(application) {

    private val _dataListLive=MutableLiveData<List<LCObject>>()
    val dataListLive:LiveData<List<LCObject>> =_dataListLive

    init {
        //下面是基础查询
        val query=LCQuery<LCObject>("Word")//这里是在服务器上创建一个叫"Word"文件
        query.whereEqualTo("user",LCUser.getCurrentUser())

        query.findInBackground().subscribe(object : io.reactivex.Observer<List<LCObject>>{

            override fun onNext(t: List<LCObject>) {//查询成功
                _dataListLive.value =t//就将查到的东西赋值给_dataListLive
            }

            override fun onError(e: Throwable) {//查询失败
                Toast.makeText(application,"${e}", Toast.LENGTH_SHORT).show()
            }
            override fun onSubscribe(d: Disposable) {}
            override fun onComplete() {}
        })

        //liveQuery可以实时更新数据,当数据改变时，可以实时更新同步
        val liveQuery=LCLiveQuery.initWithQuery(query)
        liveQuery.subscribeInBackground(object : LCLiveQuerySubscribeCallback() {
            override fun done(e: LCException?) {
                //订阅成功
                if (e != null) return
            }
        })

        //订阅成功后，就可以接收到和 LCObject 相关的更新了
        liveQuery.setEventHandler(object : LCLiveQueryEventHandler() {

            override fun onObjectCreated(lcObject: LCObject?) {//当有对象被创建时
                super.onObjectCreated(lcObject)
                val t=_dataListLive.value?.toMutableList()
                lcObject?.let { t?.add(it) }
                _dataListLive.value=t
            }

            override fun onObjectDeleted(objectId: String?) {//当有对象被删除时
                super.onObjectDeleted(objectId)

                val t=_dataListLive.value?.toMutableList()
                val ob=t?.find {
                    it.get("objectId")==objectId
                }

                t?.remove(ob)
                _dataListLive.value=t
            }

            override fun onObjectUpdated(objectId: LCObject?, updateKeyList: MutableList<String>?) {//当有对象被修改时
                super.onObjectUpdated(objectId, updateKeyList)

                val t=_dataListLive.value
                val ob=t?.find {
                    it.get("objectId")==objectId?.get("objectId")
                }

                updateKeyList?.forEach {
                    ob?.put(it,objectId?.get(it))
                }

                _dataListLive.value=_dataListLive.value//将新修改的值赋给原值,就实现了刷新
            }
        })
    }

    fun addWord(newWord :String){//添加单词

        LCObject("Word").apply {
            //序列化
            put("word",newWord)
            put("user",LCUser.getCurrentUser())//将单词和用户id保存,且可以根据key值获取

            //将文件保存到云端
            saveInBackground().subscribe(object :io.reactivex.Observer<LCObject>{

                override fun onNext(t: LCObject) {//保存成功,也提示一下
                    Toast.makeText(getApplication(),"添加成功", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {//保存失败,提示一下
                    Toast.makeText(getApplication(),"${e}", Toast.LENGTH_SHORT).show()
                }
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
            })
        }

    }
}