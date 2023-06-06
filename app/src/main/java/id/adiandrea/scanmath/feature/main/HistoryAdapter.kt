package id.adiandrea.scanmath.feature.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.adiandrea.scanmath.data.local.history.History
import id.adiandrea.scanmath.databinding.ItemHistoryBinding

class HistoryAdapter(val data: MutableList<History>)
    : RecyclerView.Adapter<HistoryAdapter.HistoryHolder>(){

    class HistoryHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder =
        HistoryHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        with(holder) {
            val item = data[bindingAdapterPosition]
            binding.expression.text = "Input: ${item.getDisplayedExpression()}"
            binding.result.text = "Result: ${item.result}"
        }
    }

    override fun getItemCount(): Int = data.size

}