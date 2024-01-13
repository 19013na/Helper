package com.example.projectapplication


import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.projectapplication.databinding.ActivityAddBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode === android.app.Activity.RESULT_OK){   // 이미지가 제대로 왔으면
                // 이미지를 ImageView에 보이기
                Glide
                    .with(applicationContext)
                    .load(it.data?.data)
                    .apply(RequestOptions().override(250, 200)) // 사진 규격 설정
                    .centerCrop()  // 센터에 자리한다.
                    .into(binding.addImageView) // 이미지 넣기
                // 이미지의 주소 저장
                val cursor = contentResolver.query(it.data?.data as Uri, arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null)
                cursor?.moveToFirst().let{
                    filePath = cursor?.getString(0) as String
                    // 그냥 filePath 확인하는 것임. Log.d("mobileApp", "${filePath}")
                }
            }
        }
        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "images/");
            requestLauncher.launch(intent)
        }
        // btnSave눌렀을 때 처리하는 코드가 있어야함.
        binding.btnSave.setOnClickListener {
            if(binding.addEditView.text.isNotEmpty() && binding.addImageView.drawable !== null) { // addEditView에 뭐라도 적었어야 저장도 가능
                // firestore에 저장해야함.
                saveStore() // 함수 호출... 아래에 있음.
            } else{
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            finish()    // addActivity가 종료되면 BoardFragment.kt로 돌아감. 그다음은 onstart 실행.
        }
    }

    fun dateToString(date:Date): String{
        val format = SimpleDateFormat("yyyy-mm-dd hh:mm")
        return format.format(date)
    }
    fun saveStore(){
        val data = mapOf(
            "email" to MyApplication.email,
            "content" to binding.addEditView.text.toString(),
            "date" to dateToString(Date())
        )

        // 드디어 firestore에 저장
        MyApplication.db.collection("news")
            .add(data)
            .addOnSuccessListener {
                Log.d("mobileApp", "data firestore save Ok")
                //uploadImage(it.id)
            }
            .addOnFailureListener {
                Log.d("mobileApp", "data firestore save error")
            }
    }
    fun uploadImage(docId:String){  // storage 접근 방법
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${docId}.jpg")
        val file = Uri.fromFile(File(filePath))
        imageRef.putFile(file)
            .addOnSuccessListener {
                Log.d("mobileApp", "imageRef.putFile(file) - addOnSuccessListener")
                finish()
            }
            .addOnFailureListener {
                Log.d("mobileApp", "imageRef.putFile(file) - addOnFailureListener")
            }
    }
}