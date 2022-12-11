package com.example.bunkerburgerblind

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bunkerburgerblind.databinding.FragmentDialog9500Binding

// TODO: Rename parameter arguments, choose names that match

class Dialog9500Fragment : DialogFragment() {
    private var _binding: FragmentDialog9500Binding? = null
    private val binding get() = _binding!!
    private lateinit var burger: ArrayList<MenuType>
    private lateinit var side: ArrayList<MenuType>
    private lateinit var beverage: ArrayList<MenuType>

    var shoppingBag = ArrayList<String>()
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
        _binding = FragmentDialog9500Binding.inflate(inflater, container, false)
        val view = binding.root

        burger = arguments?.getSerializable("burger") as ArrayList<MenuType>
        side = arguments?.getSerializable("side") as ArrayList<MenuType>
        beverage = arguments?.getSerializable("beverage") as ArrayList<MenuType>

        burger = burger.filter { it.price == 6500 } as ArrayList<MenuType>

        binding.prevBtn.setOnClickListener{
            dismiss()
        }

        binding.nextBtn.setOnClickListener{
            shoppingBag.add(burger[selectedMenu].name)
            val dialog = Dialog9500SideFragment()
            setDataAtFragment(dialog, burger, side, beverage, shoppingBag)
            dismiss()
        }

        binding.scrapRecyclerView.adapter = RecyclerUserAdapter(burger, TestShoppingBag())

        return view
    }

    private fun setDataAtFragment(fragment:DialogFragment, burger:ArrayList<MenuType>, side:ArrayList<MenuType>, beverage:ArrayList<MenuType>, shoppingBag: ArrayList<String>) {
        val bundle = Bundle()
        bundle.putSerializable("burger", burger)
        bundle.putSerializable("side", side)
        bundle.putSerializable("beverage", beverage)
        bundle.putSerializable("shoppingBag", shoppingBag)

        fragment.arguments = bundle
        activity?.let { fragment.show(it.supportFragmentManager, "Custom") }
    }
}