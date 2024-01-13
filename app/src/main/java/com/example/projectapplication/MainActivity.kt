package com.example.projectapplication

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectapplication.MyApplication.Companion.auth
import com.example.projectapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var authMenuItem: MenuItem? = null
    private lateinit var fragmentTransaction: FragmentTransaction
    private var fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentHome = HomeFragment()
    private val fragmentSubway = SubwayFragment()
    private val fragmentUser = UserFragment()

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menu_frame_view,fragmentHome).commitAllowingStateLoss()


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigationview)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            val transaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.menu_home -> transaction.replace(R.id.menu_frame_view, fragmentHome)
                    .commitAllowingStateLoss()
                R.id.menu_subway -> transaction.replace(R.id.menu_frame_view, fragmentSubway)
                    .commitAllowingStateLoss()
                R.id.menu_user -> transaction.replace(R.id.menu_frame_view, fragmentUser)
                    .commitAllowingStateLoss()
            }
            true
        }
    }

    //add...............................
    override fun onResume() {
        super.onResume()

        val bgColor:String? = sharedPreferences.getString("bg_color", "")
        //binding.viewpager2.setBackgroundColor(Color.parseColor(bgColor))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)    // 옵션 메뉴 추가

        // 로그인 오류발생
        authMenuItem = menu!!.findItem(R.id.menu_auth)
        if(MyApplication.checkAuth()){  // 로그인 여부 확인
            authMenuItem!!.title = "${MyApplication.email}님"
        } else{ // 로그인 인증이 안된 경우 인증 글자 그대로
            authMenuItem!!.title = "인증"
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart(){ // 자동으로 호출되는 함수임. 그럼 언제 불려지냐?
        // Intent에서 finish()  돌아올 때 실행
        // onCreate -> onStart -> onCreateOptionMenu
        super.onStart()
        if(authMenuItem != null) {
            if (MyApplication.checkAuth()) {
                authMenuItem!!.title = "${MyApplication.email}님"
            } else {
                authMenuItem!!.title = "인증"
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {   // 메뉴를 눌렀을 때 반응
        if(item.itemId === R.id.menu_main_setting) {
            val intent = Intent(this, SettingActivity::class.java)   // 약 1시간 30분?
            startActivity(intent)
            return true
        } else if(item.itemId === R.id.menu_auth){
            val intent = Intent(this,AuthActivity::class.java)
            if(authMenuItem!!.title!!.equals("인증")){    // title도 null이 아니기 때문에 !!를 붙여줘야함.
                intent.putExtra("data", "logout")
            }
            else{ // 이메일 또는 구글로 로그인 되어있는 상황
                intent.putExtra("data", "login")    // 로그인 상태라는 것을 전달
            }
            startActivity(intent)    // AuthActivity activity 생성
        }
        return super.onOptionsItemSelected(item)
    }
}