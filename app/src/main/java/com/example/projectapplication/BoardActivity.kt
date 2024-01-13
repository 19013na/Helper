package com.example.projectapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectapplication.databinding.ActivityBoardBinding
import com.example.projectapplication.databinding.ActivityMainBinding
import com.google.firebase.firestore.Query

class BoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityBoardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val binding = ActivityBoardBinding.inflate(inflater, container, false)

            //myCheckPermission(this as AppCompatActivity)

            binding.mainFab.setOnClickListener {
                Log.d("mobileApp", "binding");
                if (MyApplication.checkAuth()) {  // 로그인한 유저만 실행 가능.
                    val intent = Intent(this, AddActivity::class.java)
                    // val intent = Intent(requireContext(), AddActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "인증을 진행해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            return binding.root
        }

        fun makeRecyclerView(){
            MyApplication.db.collection("news")
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemBoardModel>()
                    for(document in result){
                        val item = document.toObject(ItemBoardModel::class.java)
                        item.docId=document.id
                        itemList.add(item)
                    }
                    binding.boardRecyclerView.layoutManager = LinearLayoutManager(this)
                    binding.boardRecyclerView.adapter = MyBoardAdapter(this, itemList)
                }
                .addOnFailureListener{exception ->
                    Log.d("mobileapp", "error", exception)
                    Toast.makeText(this, "서버로부터 데이터 획득에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
        }




        fun myCheckPermission(activity: AppCompatActivity) {

            val requestPermissionLauncher = activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                if (it) {
                    Toast.makeText(activity, "권한 승인", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "권한 거부", Toast.LENGTH_SHORT).show()
                }
            }

            if (ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.READ_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    activity.startActivity(intent)
                }
            }
        }
    }
}