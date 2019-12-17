package bg.abv.ani1802.rimichka.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import bg.abv.ani1802.rimichka.R
import bg.abv.ani1802.rimichka.databinding.RhymeItemBinding

class RhymesRecyclerViewAdapter(
    context: Context,
    private val rhymesViewModelsList: List<SingleRhymeViewModel>
) : RecyclerView.Adapter<RhymesRecyclerViewAdapter.SearchRhymesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchRhymesViewHolder {
        val itemBinding = DataBindingUtil.inflate<RhymeItemBinding>(
            inflater,
            R.layout.rhyme_item,
            parent,
            false
        )
        return SearchRhymesViewHolder(
            itemBinding.root,
            itemBinding
        )
    }

    override fun getItemCount(): Int {
        return rhymesViewModelsList.size
    }

    override fun onBindViewHolder(holder: SearchRhymesViewHolder, position: Int) {
        val currRhymeViewModel = rhymesViewModelsList[position]
        holder.binding.viewModel = currRhymeViewModel
    }

    class SearchRhymesViewHolder(
        itemView: View,
        val binding: RhymeItemBinding
    ) : RecyclerView.ViewHolder(itemView) {
    }
}