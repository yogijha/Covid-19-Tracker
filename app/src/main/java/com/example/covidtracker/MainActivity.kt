package com.example.covidtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.android.synthetic.main.activity_main.recoveredTv as recoveredTv1
import kotlinx.android.synthetic.main.item_list.activeTv as activeTv1
import kotlinx.android.synthetic.main.item_list.confirmedTv as confirmedTv1
import kotlinx.android.synthetic.main.item_list.deceasedTv as deceasedTv1

class MainActivity : AppCompatActivity() {
    private lateinit var listAdapter: ListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            val response = withContext(Dispatchers.IO){ Client.api.execute() }
            if (response.isSuccessful){
                val data=Gson().fromJson(response.body?.string(),Response::class.java)
                launch(Dispatchers.Main){
                    bindCombinedData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(0,data.statewise.size))
                }
            }
        }
    }
    private fun bindStateWiseData(subList:List<StatewiseItem>){
        listAdapter= ListAdapter(subList)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_header,list,false))
        list.adapter=listAdapter
    }
    private fun bindCombinedData(data:StatewiseItem){
        confirmedTv.text=data.confirmed
        activeTv.text=data.active
        recoveredTv.text=data.recovered
        deceasedTv.text=data.deaths
    }
}
