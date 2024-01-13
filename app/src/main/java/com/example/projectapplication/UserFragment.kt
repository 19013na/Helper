package com.example.projectapplication

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.example.projectapplication.databinding.FragmentUserBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentUserBinding.inflate(inflater, container, false)

        // 전화 걸기
        binding.btnCall.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:/1588-4388"))  // 설명 : (this, Activity::class.java)
            startActivity(intent)      // ACTION_CALL : 바로 전화거는 설정 - 이 설정을 해주려면 AndroidManifest에 추가해줘야함.
        }

        // 웹사이트 연결
        // 해당 사이트에 접속하기
        binding.btnWeb.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sisul.or.kr/open_content/calltaxi/"))
            startActivity(intent)
        }

        // 질문하기 누르면
        binding.btnQst.setOnClickListener{
            val intent = Intent(requireContext(), BoardActivity::class.java)
            startActivity(intent)
        }



        // 수정하기
        val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try{
                val calRatio = calculateInSampleSize(it.data!!.data!!, 150, 150)
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio
                var inputStream = getActivity()?.getContentResolver()?.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream = null
                bitmap?.let {
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let{ Log.d("mobileApp", "bitmap NULL")}
            } catch(e:Exception){ e.printStackTrace() } // 원하는 데이터가 없으면 exception 발생
        }

        binding.galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            //startActivity(intent) // 이거 되긴하지만 기존에 사용한 지도, 전화...은 그냥 화면에서 봤을 뿐임. but, 갤러리는 데이터를 가져오는 것임 - startActivity쓰면 안됨.
            requestGalleryLauncher.launch(intent)
        }

        return binding.root
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = getActivity()?.getContentResolver()?.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}