package com.example.leandemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import cn.leancloud.LCUser
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign.*

//登录，主界面
class ActivityLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_account.addTextChangedListener(weather)//为两个textview添加监听事件
        login_pwd.addTextChangedListener(weather)

        btn_login.isEnabled=false//使登录按钮不可用
        progress1.visibility=View.INVISIBLE

        btn_sign.setOnClickListener {//点击注册跳转到注册界面
            startActivity(Intent(this,ActivitySign::class.java))
        }

        btn_login.setOnClickListener {
            progress1.visibility=View.VISIBLE

            //获取账户与密码
            val name=login_account.text?.trim().toString()
            val pwd=login_pwd.text?.trim().toString()

            //下面的代码是用用户名和密码登录一个账户：
            LCUser.logIn(name,pwd).subscribe(object : Observer<LCUser> {

                override fun onNext(t: LCUser) {//登录成功
                    //跳转到主界面
                    startActivity(Intent(this@ActivityLogin,MainActivity::class.java))
                    finish()//关闭当前窗口，这样做即使，按back键也退不回来
                }

                override fun onError(e: Throwable) {//登录失败
                    progress1.visibility=View.INVISIBLE
                    //就将错误提示出来
                    Toast.makeText(this@ActivityLogin,"${e}1", Toast.LENGTH_SHORT).show()
                }
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
            })
        }
    }

    private val weather=object :TextWatcher{

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){

            val text_1=login_account.text.toString().isNotEmpty()
            val text_2=login_pwd.text.toString().isNotEmpty()
            btn_login.isEnabled=text_1 and text_2//如果两个输入框都有值,就使登录按钮可用
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

    }
}