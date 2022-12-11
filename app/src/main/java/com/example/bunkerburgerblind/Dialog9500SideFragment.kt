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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Dialog9500SideFragment : DialogFragment() {
    private var _binding: FragmentDialog9500SideBinding? = null
    private val binding get() = _binding!!
    private lateinit var burger: ArrayList<MenuType>
    private lateinit var side: ArrayList<MenuType>
    private lateinit var beverage: ArrayList<MenuType>
    private lateinit var shoppingBag: ArrayList<String>

    var totalCost = 9500
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
        shoppingBag = arguments?.getSerializable("shoppingBag") as ArrayList<String>

        side = ArrayList(side.sortedBy { it.price })

        Log.d("장바구니", shoppingBag[0])

        binding.prevBtn.setOnClickListener{
            val dialog = Dialog9500Fragment()
            setDataAtFragment(dialog, burger, side, burger, shoppingBag, totalCost)
            dismiss()
        }
        binding.nextBtn.setOnClickListener{
            shoppingBag.add(side[selectedMenu].name)
            totalCost += side[selectedMenu].price - 2500
            val dialog = Dialog9500BeverageFragment()
            setDataAtFragment(dialog, burger, side, beverage, shoppingBag, totalCost)
            dismiss()
        }
        binding.scrapRecyclerView.adapter = RecyclerUserAdapter2(side ,TestShoppingBag())

        Log.d("테스뚜1", binding.scrapRecyclerView.adapter.toString())

        return view
    }

    private fun setDataAtFragment(fragment:DialogFragment, burger:ArrayList<MenuType>, side:ArrayList<MenuType>, beverage:ArrayList<MenuType>, shoppingBag:ArrayList<String>, totalCost: Int) {
        val bundle = Bundle()
        bundle.putSerializable("burger", burger)
        bundle.putSerializable("side", side)
        bundle.putSerializable("beverage", beverage)
        bundle.putSerializable("shoppingBag", shoppingBag)
        bundle.putInt("totalCost", totalCost)

        fragment.arguments = bundle
        activity?.let { fragment.show(it.supportFragmentManager, "Custom") }
    }


}