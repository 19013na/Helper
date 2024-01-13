package com.example.projectapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.projectapplication.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeVisibility(intent.getStringExtra("data").toString())  // 처음은 로그아웃 상태가 보임

        binding.goSignInBtn.setOnClickListener {
            // 회원가입
            changeVisibility("signin")
        }

        binding.signBtn.setOnClickListener {
            //이메일,비밀번호 회원가입........................
            val email: String = binding.authEmailEditView.text.toString()
            val password: String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)  // email과 password를 전달하겠다.
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                            sendTask ->
                                if(sendTask.isSuccessful){
                            Toast.makeText(baseContext, "회원가입 성공, 이메일 확인", Toast.LENGTH_LONG).show()
                            changeVisibility("Logout")
                        }
                        }
                    } else{
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                    binding.authEmailEditView.text.clear()  // email과 password검색창을 깨끗하게
                    binding.authPasswordEditView.text.clear()
                }
        }

        binding.loginBtn.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            val email: String = binding.authEmailEditView.text.toString()
            val password: String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                         //MyApplication에 함수 추가해줌.
                        if (MyApplication.checkAuth()) { // 함수 checkAuth가 true면
                            MyApplication.email = email
                            //changeVisibility("login")
                            finish()    // login대신 원래의 프로그램으로 돌아와야한다. --> 바로 mainactivity 화면으로
                        } else {
                            Toast.makeText(baseContext, "이메일 인증 실패", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")  // logout 상태 유지
                    }
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                }
        }

        binding.logoutBtn.setOnClickListener {
            //로그아웃...........
            MyApplication.auth.signOut()
            MyApplication.email = null
            //changeVisibility("logout")
            finish()    // 원래의 프로그램으로 돌아와야한다.
        }

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)   // 결과 전달 받음
            // 이때 발생하는 오류는 ApiException 오류 : Google Play 서비스 호출이 실패하였을 때 테스크에서 반환할 예외
            try{
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)   // 구글에 인증되어졌는가.
                MyApplication.auth.signInWithCredential(credential)   // credential 인증을 이용할 것임.
                    .addOnCompleteListener(this){   // 인증이 완료되었을 때 처리
                            task ->
                        if(task.isSuccessful){  // 인증이 성공적인 경우
                            MyApplication.email = account.email
                            //changeVisibility("login")
                            Log.d("mobileApp", "GoogleSignIn - Successful")
                            finish() // 원래의 프로그램으로 돌아와야한다.
                        }
                        else{
                            changeVisibility("logout")  // logout상태 유지
                            Log.d("mobileApp", "GoogleSignIn - NOT Successful")
                        }
                    }

            }catch (e: ApiException){
                changeVisibility("logout")  // logout상태 유지
                Log.d("mobileApp", "GoogleSignIn - ${e.message}")   // 어떤 예외가 발생했는지 알려줌.
            }
        }
        binding.googleLoginBtn.setOnClickListener {
            //구글 로그인....................
            val gso : GoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent : Intent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)    // 후속처리
        }
    }

    fun changeVisibility(mode: String) {
        if (mode.equals("signin")) {  // 회원가입해야할 때    // ===보다 .equals()가 더 안정적임.
            binding.run {
                logoutBtn.visibility = View.GONE    // 회원가입할 때 필요한 기능들만 보이도록 함.
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        } else if (mode.equals("login")) {
            binding.run {
                authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
                logoutBtn.visibility = View.VISIBLE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.GONE
            }

        } else if (mode.equals("logout")) {
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        }
    }
}