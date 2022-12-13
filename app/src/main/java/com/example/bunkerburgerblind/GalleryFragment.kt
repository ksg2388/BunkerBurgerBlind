package com.example.bunkerburgerblind
/*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bunkerburgerblind.databinding.EditMenuDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class GalleryFragment(uri: Uri): Fragment() {
    private lateinit var mBinding: EditMenuDetailBinding
    private var uri: Uri? = uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        mBinding = EditMenuDetailBinding.inflate(inflater, container, false)
        mBinding.editMenuImg.setImageURI(uri)
        mBinding.editMenuImgSubmit.setOnClickListener {
            uploadImageTOFirebase(uri!!)
        }

        return mBinding.root
    }

    // Firebase Storage에 이미지 업로드하는 함수
    fun uploadImageTOFirebase(uri: Uri) {
        var storage : FirebaseStorage? = FirebaseStorage.getInstance()

        // 파일 이름 생성
        var fileName = "IMAGE_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.png"
        // 기본 찾모 위치
        var imagesRef = storage!!.reference.child("images/.".child(fileName))

        imagesRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(activity, getString(R.string.upload_success), Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            println(it)
            Toast.makeText(activity, getString(R.string.upload_fail), Toast.LENGTH_SHORT).show()
        }
    }
}

 */