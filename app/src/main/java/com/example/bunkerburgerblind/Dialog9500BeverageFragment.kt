package com.example.bunkerburgerblind

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bunkerburgerblind.databinding.FragmentDialog9500BeverageBinding
import kotlin.properties.Delegates

class Dialog9500BeverageFragment : DialogFragment() {
    private var _binding: FragmentDialog9500BeverageBinding? = null
    private val binding get() = _binding!!
    private lateinit var burger: ArrayList<MenuType>
    private lateinit var side: ArrayList<MenuType>
    private lateinit var beverage: ArrayList<MenuType>
    private lateinit var setList: ArrayList<String>
    var SBList = arrayListOf<shopping_basket_data>() //장바구니 아이템 배열

    lateinit var sendEventListener: SendEventListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        sendEventListener = context as SendEventListener
    }

    var selectedMenu = 0

    inner class TestShoppingBag {
        fun getSelectedIndex(idx: Int) {
            selectedMenu = idx
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialog9500BeverageBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        burger = arguments?.getSerializable("burger") as ArrayList<MenuType>
        side = arguments?.getSerializable("side") as ArrayList<MenuType>
        beverage = arguments?.getSerializable("beverage") as ArrayList<MenuType>
        setList = arguments?.getSerializable("setList") as ArrayList<String>
        SBList = arguments?.getSerializable("SBList") as ArrayList<shopping_basket_data>
        var totalCost = arguments?.getInt("totalCost")!!
        var prevTotalCost = arguments?.getInt("prevTotalCost")!!


        beverage = ArrayList(beverage.sortedBy { it.price })

        binding.prevBtn.setOnClickListener{
            setList.removeLast()
            totalCost = prevTotalCost
            val dialog = Dialog9500SideFragment()
            setDataAtFragment(dialog, burger, side, beverage, setList, totalCost, SBList)
            dismiss()
        }
        binding.nextBtn.setOnClickListener{
            setList.add(beverage[selectedMenu].name)
            totalCost += beverage[selectedMenu].price - 2000
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("setList", setList)
            intent.putExtra("totalCost", totalCost)
            intent.putExtra("SBListFromSemMenu", SBList)
            startActivity(intent)
            dismiss()
        }
        binding.scrapRecyclerView.adapter = RecyclerUserAdapter3(beverage, TestShoppingBag())

        return view
    }

    private fun setDataAtFragment(fragment:DialogFragment, burger:ArrayList<MenuType>, side:ArrayList<MenuType>, beverage:ArrayList<MenuType>, setList:ArrayList<String>, totalCost: Int, SBList:ArrayList<shopping_basket_data>) {
        val bundle = Bundle()
        bundle.putSerializable("burger", burger)
        bundle.putSerializable("side", side)
        bundle.putSerializable("beverage", beverage)
        bundle.putSerializable("setList", setList)
        bundle.putInt("totalCost", totalCost)
        bundle.putSerializable("SBList", SBList)

        fragment.arguments = bundle
        activity?.let { fragment.show(it.supportFragmentManager, "Custom") }
    }

}