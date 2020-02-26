package com.example.expandable.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView

class ExpandableListViewAdapter(private var context: Context, expandableListView: ExpandableListView) : BaseExpandableListAdapter() {

    private var data: ArrayList<Data<String, String>> = arrayListOf(
        Data("0", arrayListOf("0", "1", "2", "3")),
        Data("1", arrayListOf("0", "1", "2", "3")),
        Data("2", arrayListOf("0", "1", "2", "3")),
        Data("3", arrayListOf("0", "1", "2", "3")),
        Data("4", arrayListOf("0", "1", "2", "3"))
    )

    init {
        expandableListView.setAdapter(this)
    }

    override fun getGroup(groupPosition: Int): Data<String, String> {
        return data[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }


    @SuppressLint("SetTextI18n")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ParentViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(this.context)
                .inflate(R.layout.item_group, parent, false)
            viewHolder = ParentViewHolder()
            viewHolder.group = view.findViewById(R.id.group_text)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ParentViewHolder
        }

        viewHolder.group.text = "group" + data[groupPosition].group

        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return data[groupPosition].child.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return data[groupPosition].child[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ChildViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(this.context)
                .inflate(R.layout.item_child, parent, false)
            viewHolder = ChildViewHolder()
            viewHolder.child = view.findViewById(R.id.child_text)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ChildViewHolder
        }

        viewHolder.child.text = "\tchild" + data[groupPosition].child[childPosition]

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return data.size
    }

    companion object {
        class ParentViewHolder {
            lateinit var group: TextView
        }

        class ChildViewHolder {
            lateinit var child: TextView
        }

        data class Data<K, V>(val group: K, val child: ArrayList<V>)
    }
}
