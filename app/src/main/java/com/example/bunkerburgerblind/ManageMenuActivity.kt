package com.example.bunkerburgerblind

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

data class ManagemenuView(val menu: MenuType, val type: String)

lateinit var requestLaunch: ActivityResultLauncher<Intent>

class ManageMenuActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")

    lateinit var goBack: Button
    lateinit var addMenu: Button
    lateinit var radioGroup: RadioGroup

    var tempMenuType = MenuType()
    lateinit var tempManagemenuView: ManagemenuView

    var burgerList = ArrayList<MenuType>()
    var sideList = ArrayList<MenuType>()
    var beverageList = ArrayList<MenuType>()

    var burgers = ArrayList<ManagemenuView>()
    var sides = ArrayList<ManagemenuView>()
    var beverages = ArrayList<ManagemenuView>()
    var all = ArrayList<ManagemenuView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_menu_main)

        lateinit var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.manage_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ???????????????????????? ????????? ????????????
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // ????????? ?????? ??????
                burgerList.clear()
                sideList.clear()
                beverageList.clear()
                burgers.clear()
                sides.clear()
                beverages.clear()
                all.clear()

                // ?????? ????????? ????????????
                val burgerdata = snapshot.child("menu").child("burger")
                for (item in burgerdata.children) {
                    val burger = item.getValue(MenuType::class.java)
                    if (burger != null) {
                        burgerList.add(burger)
                    }
                }

                // ????????? ????????? ????????????
                val sidedata = snapshot.child("menu").child("side")
                for (item in sidedata.children) {
                    val side = item.getValue(MenuType::class.java)
                    if (side != null) {
                        sideList.add(side)
                    }
                }

                // ?????? ????????? ????????????
                val beveragedata = snapshot.child("menu").child("beverage")
                for (item in beveragedata.children) {
                    val beverage = item.getValue(MenuType::class.java)
                    if (beverage != null) {
                        beverageList.add(beverage)
                    }
                }



                for (item in burgerList) {
                    burgers.add(ManagemenuView(item, "burger"))
                }
                for (item in sideList) {
                    sides.add(ManagemenuView(item, "side"))
                }
                for (item in beverageList) {
                    beverages.add(ManagemenuView(item, "beverage"))
                }
                all.addAll(burgers)
                all.addAll(sides)
                all.addAll(beverages)

                recyclerView.adapter = ManageAdapter(all)
                (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("??????", "??????")
            }
        })

        requestLaunch = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                val resultDir = it.data?.getIntExtra("dir", 0)
                val resultOp = it.data?.getStringExtra("option")

                val resultName = it.data?.getStringExtra("name").toString()
                val resultEx = it.data?.getStringExtra("examination").toString()
                val resultPrice = it.data?.getIntExtra("price", 0).toString().toInt()
                val resultId = it.data?.getIntExtra("id", 0).toString().toInt()
                val resultUsage = it.data?.getIntExtra("usage", 0).toString().toInt()
                val resultStock = it.data?.getIntExtra("stock", 0).toString().toInt()
                val resultImg = it.data?.getStringExtra("img").toString()

                if (resultOp.equals("add")) {
                    // ????????? ???????????? ??????
                    if (resultDir == 0) {
                        // ?????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        burgers.clear()
                        for (item in burgerList) {
                            burgers.add(ManagemenuView(item, "burger"))
                        }
                        all.addAll(burgers)

                        tempMenuType = (MenuType(
                            resultUsage,
                            resultName,
                            resultEx,
                            resultId,
                            resultPrice,
                            resultStock,
                            resultImg
                        ))
                        burgers.add(ManagemenuView(tempMenuType, "burger"))
                        tempManagemenuView = (ManagemenuView(tempMenuType, "burger"))
                        all.add(tempManagemenuView)
                    } else if (resultDir == 1) {
                        // ????????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        sides.clear()
                        for (item in sideList) {
                            sides.add(ManagemenuView(item, "side"))
                        }
                        all.addAll(sides)

                        tempMenuType = (MenuType(
                            resultUsage,
                            resultName,
                            resultEx,
                            resultId,
                            resultPrice,
                            resultStock,
                            resultImg
                        ))
                        sides.add(ManagemenuView(tempMenuType, "side"))
                        tempManagemenuView = (ManagemenuView(tempMenuType, "side"))
                        all.add(tempManagemenuView)
                    } else if (resultDir == 2) {
                        // ?????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        beverages.clear()
                        for (item in beverageList) {
                            beverages.add(ManagemenuView(item, "beverage"))
                        }
                        all.addAll(beverages)

                        tempMenuType = (MenuType(
                            resultUsage,
                            resultName,
                            resultEx,
                            resultId,
                            resultPrice,
                            resultStock,
                            resultImg
                        ))
                        burgers.add(ManagemenuView(tempMenuType, "beverage"))
                        tempManagemenuView = (ManagemenuView(tempMenuType, "beverage"))
                        all.add(tempManagemenuView)
                    }
                    recyclerView.adapter = ManageAdapter(all)
                    (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
                } else if (resultOp.equals("modify")) {
                    // ????????? ???????????? ??????
                    if (resultDir == 0) {
                        // ?????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        burgers.clear()
                        for (item in burgerList) {
                            burgers.add(ManagemenuView(item, "burger"))
                        }
                        all.addAll(burgers)

                        tempMenuType = (MenuType(
                            resultUsage,
                            resultName,
                            resultEx,
                            resultId,
                            resultPrice,
                            resultStock,
                            resultImg
                        ))
                        burgers.add(ManagemenuView(tempMenuType, "burger"))
                        tempManagemenuView = (ManagemenuView(tempMenuType, "burger"))
                        all.add(tempManagemenuView)

                        recyclerView.adapter = ManageAdapter(burgers)
                        (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
                    } else if (resultDir == 1) {
                        // ????????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        sides.clear()
                        for (item in sideList) {
                            sides.add(ManagemenuView(item, "side"))
                        }
                        all.addAll(sides)

                        tempMenuType = (MenuType(
                            resultUsage,
                            resultName,
                            resultEx,
                            resultId,
                            resultPrice,
                            resultStock,
                            resultImg
                        ))
                        sides.add(ManagemenuView(tempMenuType, "side"))
                        tempManagemenuView = (ManagemenuView(tempMenuType, "side"))
                        all.add(tempManagemenuView)

                        sides.clear()
                        recyclerView.adapter = ManageAdapter(sides)
                        (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
                    } else if (resultDir == 2) {
                        // ?????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        beverages.clear()
                        for (item in beverageList) {
                            beverages.add(ManagemenuView(item, "beverage"))
                        }
                        all.addAll(beverages)

                        tempMenuType = (MenuType(
                            resultUsage,
                            resultName,
                            resultEx,
                            resultId,
                            resultPrice,
                            resultStock,
                            resultImg
                        ))
                        burgers.add(ManagemenuView(tempMenuType, "beverage"))
                        tempManagemenuView = (ManagemenuView(tempMenuType, "beverage"))
                        all.add(tempManagemenuView)

                        sides.clear()
                        recyclerView.adapter = ManageAdapter(beverages)
                        (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
                    }
                } else if (resultOp.equals("delete")) {
                    // ????????? ???????????? ??????
                    var i = 0 // index

                    recyclerView.removeAllViewsInLayout()

                    if (resultDir == 0) {
                        // ?????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        burgers.clear()
                        for (item in burgerList) {
                            burgers.add(ManagemenuView(item, "burger"))
                        }
                        all.addAll(burgers)

                        // ??????????????? ???????????? ???????????? ??????
                        for (item in burgers) {
                            if ((burgers[i].menu.name).equals(resultName)) { // ??????????????? ????????? ?????? ??????
                                burgers.removeAt(i)
                                all.removeAt(i)
                                break
                            }
                        }
                        burgers.clear()
                        recyclerView.adapter = ManageAdapter(burgers)
                        (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
                    } else if (resultDir == 1) {
                        // ????????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        sides.clear()
                        for (item in sideList) {
                            sides.add(ManagemenuView(item, "side"))
                        }
                        all.addAll(sides)

                        // ??????????????? ???????????? ???????????? ??????
                        for (item in sides) {
                            if ((sides[i].menu.name).equals(resultName)) { // ??????????????? ????????? ?????? ??????
                                sides.removeAt(i)
                                all.removeAt(i)
                                break
                            }
                        }
                        recyclerView.adapter = ManageAdapter(sides)
                        (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
                    } else if (resultDir == 2) {
                        // ?????? ????????? ??????

                        // ????????? ?????? ??????
                        all.clear()
                        beverages.clear()
                        for (item in beverageList) {
                            beverages.add(ManagemenuView(item, "beverage"))
                        }
                        all.addAll(beverages)

                        // ??????????????? ???????????? ???????????? ??????
                        for (item in beverages) {
                            if ((beverages[i].menu.name).equals(resultName)) { // ??????????????? ????????? ?????? ??????
                                beverages.removeAt(i)
                                all.removeAt(i)
                                break
                            }
                        }
                        recyclerView.adapter = ManageAdapter(beverages)
                        (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
                    }
                }
            } else Log.d("my", "errr")
        }

        radioGroup = findViewById(R.id.manage_radioGroup)
        radioGroup.check(R.id.manageSalesShowAll) // ????????? (?????? ??????) ??????
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.manageSalesShowAll) {
                Log.e("my", "?????? ??????")
                recyclerView.adapter = ManageAdapter(all)
            } else if (checkedId == R.id.manageSalesShowSingle) {
                Log.e("my", "????????? ??????")
                recyclerView.adapter = ManageAdapter(burgers)
            } else if (checkedId == R.id.manageSalesShowSide) {
                Log.e("my", "???????????? ??????")
                recyclerView.adapter = ManageAdapter(sides)
            } else if (checkedId == R.id.manageSalesShowbeverage) {
                Log.e("my", "????????? ??????")
                recyclerView.adapter = ManageAdapter(beverages)
            }
            (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
        }

        // ????????????
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }

        // ????????????
        addMenu = findViewById(R.id.addMenu)
        addMenu.setOnClickListener {
            val intent = Intent(this, EditMenuActivity::class.java)
            startActivity(intent)
        }
    }

    class ManageViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        // layout : manage_sales_item.xml ??? ?????? ????????????

        // manage_menu_item.xml??? ??? View?????? ?????? ????????? ??????
        val imageView: ImageView = layout.findViewById(R.id.manage_item_image)
        val nameView: TextView = layout.findViewById(R.id.manage_item_name)
        val editBtn: Button = layout.findViewById(R.id.manage_edit_btn)
        val deleteBtn: Button = layout.findViewById(R.id.manage_delete_btn)
    }

    class ManageAdapter(val dataList: ArrayList<ManagemenuView>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            // LayoutInflater ?????? ????????????
            val viewInflater = LayoutInflater.from(parent.context)

            // item.xml ??????????????? inflate() ????????? ViewHolder ???????????? ??????
            val manageItemLayout =
                viewInflater.inflate(R.layout.manage_menu_item, parent, false)
            return ManageViewHolder(manageItemLayout)
        }

        //RecyclerView??? ViewHolder??? ???????????? ????????? ??? ?????? (onCreateViewHolder??? ????????? ?????? ??????)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val oneViewItem = dataList[position]
            val viewHolder = holder as ManageViewHolder

            // url??? ???????????? ????????? ??????
            Glide.with(holder.itemView.context)
                .load(oneViewItem.menu.img)
                .into(viewHolder.imageView)


            viewHolder.nameView.text = oneViewItem.menu.name

            viewHolder.editBtn.setOnClickListener {
                val intent = Intent(holder.itemView.context, EditMenuActivity::class.java)
                intent.putExtra("name", oneViewItem.menu.name)
                intent.putExtra("examination", oneViewItem.menu.examination)
                intent.putExtra("price", oneViewItem.menu.price.toString())
                intent.putExtra("imgSrc", oneViewItem.menu.img)
                intent.putExtra("stock", oneViewItem.menu.stock.toString())
                intent.putExtra("usage", oneViewItem.menu.usage.toString())
                intent.putExtra("id", oneViewItem.menu.id.toString())
                intent.putExtra("type", oneViewItem.type)
                intent.putExtra("dirty", 0)
                requestLaunch.launch(intent)
            }

            viewHolder.deleteBtn.setOnClickListener {
                Log.d("my", "???????????? ??????")
                val intent = Intent(holder.itemView.context, DeleteMenuActivity::class.java)
                intent.putExtra("name", oneViewItem.menu.name)
                intent.putExtra("imgSrc", oneViewItem.menu.img)
                intent.putExtra("type", oneViewItem.type)
                intent.putExtra("dirty", 0)
                requestLaunch.launch(intent)
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }
}