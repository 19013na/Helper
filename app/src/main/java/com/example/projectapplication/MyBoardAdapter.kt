package com.example.projectapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.projectapplication.databinding.ItemBoardBinding

// item_board.xml과 ItemBoardModel.kt연결하기 위한 adapter

// iter_board.xml을 사용하기 위해 binding
class MyBoardViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

// ItemBoardModel를 mutablelist로 사용하겠다?
class MyBoardAdapter(val context: Context, val itemList: MutableList<ItemBoardModel>): RecyclerView.Adapter<MyBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBoardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyBoardViewHolder(ItemBoardBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyBoardViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            itemEmailView.text=data.email   // text부분에 data.email부분을 넣겠다.
            itemDateView.text=data.date
            itemContentView.text=data.content
        }

        //스토리지 이미지 다운로드........................
        // 이미지 불러와야함.
//        val imageRef = MyApplication.storage.reference.child("images/{${data.docId}}.jpg")
//        imageRef.downloadUrl.addOnCompleteListener{task ->
//            if(task.isSuccessful){
//                // 다운로드 이미지를 ImageView에 보여준다.
//                GlideApp.with(context)  // 뭐가 문제지? 1시간 10분정도
//                    .load(task.result)
//                    .into(holder.binding.itemImageView) // 여기부터 위로 3줄 : 이미지 가져오기...?
//            }
//        }

    }
}
