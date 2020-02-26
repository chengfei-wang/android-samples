package com.example.expandable.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = ExpandableListViewAdapter(this, expandable_list_view)
        expandable_list_view.setOnGroupClickListener { expandableListView: ExpandableListView, view1: View, i: Int, l: Long ->
            return@setOnGroupClickListener true
        }
        expandable_list_view.setGroupIndicator(null)
        for (i in 0 until adapter.groupCount) {
            expandable_list_view.expandGroup(i)
        }
    }
}