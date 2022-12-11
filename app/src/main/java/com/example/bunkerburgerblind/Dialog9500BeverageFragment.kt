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
    private lateinit var shoppingBag: ArrayList<String>

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
        shoppingBag = arguments?.getSerializable("shoppingBag") as ArrayList<String>
        var totalCost = arguments?.getInt("totalCost")!!


        beverage = ArrayList(beverage.sortedBy { it.price })

        binding.prevBtn.setOnClickListener{
            shoppingBag.removeLast()
            val dialog = Dialog9500SideFragment()
            setDataAtFragment(dialog, burger, side, beverage, shoppingBag)
            dismiss()
        }
        binding.nextBtn.setOnClickListener{
            shoppingBag.add(beverage[selectedMenu].name)
            totalCost += beverage[selectedMenu].price - 2000
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("shoppingBag", shoppingBag)
            intent.putExtra("totalCost", totalCost)
            startActivity(intent)
            dismiss()
        }
        binding.scrapRecyclerView.adapter = RecyclerUserAdapter3(beverage, TestShoppingBag())

        return view
    }

    private fun setDataAtFragment(fragment:DialogFragment, burger:ArrayList<MenuType>, side:ArrayList<MenuType>, beverage:ArrayList<MenuType>, shoppingBag:ArrayList<String>) {
        val bundle = Bundle()
        bundle.putSerializable("burger", burger)
        bundle.putSerializable("side", side)
        bundle.putSerializable("beverage", beverage)
        bundle.putSerializable("setList", shoppingBag)

        fragment.arguments = bundle
        activity?.let { fragment.show(it.supportFragmentManager, "Custom") }
    }

}