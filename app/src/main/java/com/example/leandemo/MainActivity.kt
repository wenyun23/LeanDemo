package com.example.leandemo

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.leancloud.LCUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:MyLivedate

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //创建ViewModel
        viewModel=ViewModelProvider(this,ViewModelProvider
            .AndroidViewModelFactory(application)).get(MyLivedate::class.java)
        //创建适配器

        val myAdapter=MyAdapter()
        recycle.apply {
            layoutManager=LinearLayoutManager(this@MainActivity)
            adapter=myAdapter
        }

        //添加观察
        viewModel.dataListLive.observe(this, {
            myAdapter.updataDataList(it)
            myAdapter.notifyDataSetChanged()//刷新数据
        })

        addWord.setOnClickListener {
            val v=LayoutInflater.from(this).inflate(R.layout.addnewword,null)

            //创建个对话框
            AlertDialog.Builder(this)
                .setTitle("添加单词")
                .setView(v)
                .setPositiveButton("确定",object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                      val xin_word=v.findViewById<EditText>(R.id.editaddword).text.toString()

                      viewModel.addWord(xin_word)//将单词传过去
                    }
                })

                .setNegativeButton("取消",object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.dismiss()//让对话框消失
                    }
                })

                .create()
                .show()
        }
    }

    //重写菜单方法，实现菜单栏的功能
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.lignOut){
            LCUser.logOut()//退出登录
            startActivity(Intent(this,ActivityLogin::class.java))//回到登录界面
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)//使菜单栏可见
        return super.onCreateOptionsMenu(menu)
    }
}