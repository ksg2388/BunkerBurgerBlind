package com.example.bunkerburgerblind

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bunkerburgerblind.databinding.MainfragmentBinding

class UserFragment(val itemList: ArrayList<item_data>, val SBList: ArrayList<shopping_basket_data>): Fragment() {
    // 단품 메뉴 아이템 배열
    lateinit var menuActivity: SimpleMenuActivity
    val listAdapter = ListAdapter(itemList)     // 단품 메뉴 rv 어댑터
    private lateinit var binding : MainfragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuActivity = context as SimpleMenuActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?
    {
        binding = MainfragmentBinding.inflate(layoutInflater)
        val view = binding.root

        binding.RvMenu.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.RvMenu.adapter = listAdapter

        listAdapter.notifyDataSetChanged()

        listAdapter.setItemClickListener(object: ListAdapter.OnItemClickListener{ //단일메뉴(리사이클러뷰) 클릭 시 다이얼로그
            var flag : Boolean = false

            override fun onClick(v: View, position: Int) {
                // 메뉴 클릭
                val dialog = SingleMenuClickedDig(menuActivity)
                dialog.SMDig(itemList[position], SBList)
                dialog.setOnClickedListener(object: SingleMenuClickedDig.ButtonClickListener{
                    override fun PutSBonClick(text: Int){ //장바구니 추가
                        for (item in SBList){
                            if(item.name == itemList[position].name){
                                item.cnt = item.cnt + text
                                flag = true
                            }
                        }
                        if(flag == false) SBList.add(shopping_basket_data(itemList[position].name, itemList[position].price.toString(), itemList[position].img, text))
                        flag = false //장바구니에 아이템 추가(장바구니 데이터 구조 바꿀 필요 있을 듯 함)

                        menuActivity.SetPrice()
                    }
                })
            }
        })

        return view

    }

}