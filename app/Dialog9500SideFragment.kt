package com.example.bunkerburgerblind

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bunkerburgerblind.databinding.FragmentDialog9500SideBinding

class Dialog9500SideFragment : DialogFragment() {
    private var _binding: FragmentDialog9500SideBinding? = null
    private val binding get() = _binding!!
    private lateinit var burger: ArrayList<MenuType>
    private lateinit var side: ArrayList<MenuType>
    private lateinit var beverage: ArrayList<MenuType>
    private lateinit var setList: ArrayList<String>

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
        _binding = FragmentDialog9500SideBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        burger = arguments?.getSerializable("burger") as ArrayList<MenuType>
        side = arguments?.getSerializable("side") as ArrayList<MenuType>
        beverage = arguments?.getSerializable("beverage") as ArrayList<MenuType>
        setList = arguments?.getSerializable("setList") as ArrayList<String>
        var totalCost = arguments?.getInt("totalCost")!!
        val prevTotalCost = totalCost

        side = ArrayList(side.sortedBy { it.price })

        Log.d("장바구니", setList[0])

        binding.prevBtn.setOnClickListener{
            val dialog = Dialog9500Fragment()
            setDataAtFragment(dialog, burger, side, burger, setList, totalCost, prevTotalCost)
            dismiss()
        }
        binding.nextBtn.setOnClickListener{
            setList.add(side[selectedMenu].name)
            totalCost += side[selectedMenu].price - 2500
            val dialog = Dialog9500BeverageFragment()
            setDataAtFragment(dialog, burger, side, beverage, setList, totalCost, prevTotalCost)
            dismiss()
        }
        binding.scrapRecyclerView.adapter = RecyclerUserAdapter2(side ,TestShoppingBag())

        Log.d("테스뚜1", binding.scrapRecyclerView.adapter.toString())

        return view
    }

    private fun setDataAtFragment(fragment:DialogFragment, burger:ArrayList<MenuType>, side:ArrayList<MenuType>, beverage:ArrayList<MenuType>, setList:ArrayList<String>, totalCost: Int, prevTotalCost: Int) {
        val bundle = Bundle()
        bundle.putSerializable("burger", burger)
        bundle.putSerializable("side", side)
        bundle.putSerializable("beverage", beverage)
        bundle.putSerializable("setList", setList)
        bundle.putInt("totalCost", totalCost)
        bundle.putInt("prevTotalCost", prevTotalCost)

        fragment.arguments = bundle
        activity?.let { fragment.show(it.supportFragmentManager, "Custom") }
    }


}