package com.lsm.supermemories.ui.memories.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lsm.supermemories.R
import com.lsm.supermemories.data.Memory
import com.lsm.supermemories.databinding.ItemDateBinding
import com.lsm.supermemories.viewmodels.DateViewModel



class DateAdapter internal constructor(
        private val mMemoryListViewModel: DateViewModel
):  ListAdapter<Memory, DateAdapter.MemoryViewHolder>(MemoryDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        return MemoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        val memory = getItem(position)
        holder.bind(memory, mMemoryListViewModel)
    }


    class MemoryViewHolder(val binding: ItemDateBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(currentMemory: Memory, memoryViewModel: DateViewModel) {
            binding.memory = currentMemory
            binding.memoryViewModel = memoryViewModel
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): MemoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding:  ItemDateBinding = DataBindingUtil.inflate(
                        layoutInflater, R.layout.item_date,
                        parent, false
                )
                return MemoryViewHolder(binding)

            }
        }
    }
}
interface MemoryItemClickListener {
    fun choose_memory(memory: Memory)

}


private class MemoryDiffCallback : DiffUtil.ItemCallback<Memory>() {
    override fun areItemsTheSame(oldItem: Memory, newItem: Memory): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Memory, newItem: Memory): Boolean {
        return oldItem == newItem
    }
}