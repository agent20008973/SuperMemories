package com.lsm.supermemories.ui.memories.list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lsm.supermemories.R
import com.lsm.supermemories.data.Memory
import com.lsm.supermemories.databinding.ItemTitleBinding
import com.lsm.supermemories.viewmodels.TitleViewModel

class TitleAdapter internal constructor(
        private val mTitleListViewModel: TitleViewModel
):  ListAdapter<Memory, TitleAdapter.TitleViewHolder>(TitleDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val memory = getItem(position)
        holder.bind(memory, mTitleListViewModel)
    }


    class TitleViewHolder(val binding: ItemTitleBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(currentMemory: Memory, memoryViewModel: TitleViewModel) {
            binding.memory = currentMemory
            binding.titleViewModel = memoryViewModel
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): TitleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ItemTitleBinding = DataBindingUtil.inflate(
                        layoutInflater, R.layout.item_title,
                        parent, false
                )
                return TitleViewHolder(binding)

            }
        }
    }
}
interface TitleItemClickListener {
    fun choose_title(memory: Memory)

}


private class TitleDiffCallback : DiffUtil.ItemCallback<Memory>() {
    override fun areItemsTheSame(oldItem: Memory, newItem: Memory): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Memory, newItem: Memory): Boolean {
        return oldItem == newItem
    }
}