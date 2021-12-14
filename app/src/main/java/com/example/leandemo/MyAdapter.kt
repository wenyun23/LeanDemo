package com.example.leandemo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject

//适配器
class MyAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataList= listOf<LCObject>()//定义一个集合用来存单词

    fun updataDataList(newlist:List<LCObject>){//定义一个函数用来将单词传过来
        dataList=newlist
    }
    override fun getItemCount()=dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v=LayoutInflater.from(parent.context).inflate(R.layout.cell,parent,false)

        return object :RecyclerView.ViewHolder(v){}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.findViewById<TextView>(R.id.text_word)
            .text=dataList[position].get("word").toString()//根据key值将单词显示到TextView上

        holder.itemView.findViewById<TextView>(R.id.text_time)
            .text=dataList[position].get("createdAt").toString()//这个Key(createdAt)是自带的，可以直接查询
    }
}