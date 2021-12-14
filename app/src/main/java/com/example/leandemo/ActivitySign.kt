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
//注册
class ActivitySign : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        sign_account.addTextChangedListener(weather)//这里登录里面一样
        sign_pwd.addTextChangedListener(weather)

        sign_btn.isEnabled=false
        progress2.visibility=View.INVISIBLE

        sign_btn.setOnClickListener {
            val name=sign_account.text?.trim().toString()
            val pwd=sign_pwd.text?.trim().toString()

            //下面的代码展示了一个典型的使用用户名和密码注册的流程：
            LCUser().apply {
                progress2.visibility=View.VISIBLE

                username=name
                password=pwd
                signUpInBackground().subscribe(object :Observer<LCUser>{

                    override fun onNext(t: LCUser) {//注册成功
                        Toast.makeText(this@ActivitySign,"注册成功",Toast.LENGTH_SHORT).show()

                        //注册成功后,就是登录
                        LCUser.logIn(name,pwd).subscribe(object :Observer<LCUser>{

                            override fun onNext(t: LCUser) {//登录成功
                                //跳转到主界面
                                startActivity(Intent(this@ActivitySign,MainActivity::class.java))
                                finish()//返回到上一个界面
                            }

                            override fun onError(e: Throwable){//登录失败，就提示失败原因
                                progress2.visibility=View.INVISIBLE
                                Toast.makeText(this@ActivitySign,"${e}",Toast.LENGTH_SHORT).show()
                            }
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                        })
                    }

                    override fun onError(e: Throwable) {//注册失败,也是提示原因
                        progress2.visibility=View.INVISIBLE
                        Toast.makeText(this@ActivitySign,"${e}",Toast.LENGTH_SHORT).show()
                    }

                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {}

                })
            }
        }
    }

    private val weather=object : TextWatcher {
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            val text_1=sign_account.text.toString().isNotEmpty()
            val text_2=sign_pwd.text.toString().isNotEmpty()
            sign_btn.isEnabled=text_1 and text_2//同登录一样

        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
    }
}