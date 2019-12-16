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

class RhymesRecyclerViewAdapter(context: Context, private val rhymesViewModelsList: List<SingleRhymeViewModel>) :
    RecyclerView.Adapter<RhymesRecyclerViewAdapter.SearchRhymesViewHolder>() {

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
            itemBinding,
            this
        )
    }

    override fun getItemCount(): Int {
        return rhymesViewModelsList.size
    }

    override fun onBindViewHolder(holder: SearchRhymesViewHolder, position: Int) {
        val currRhymeViewModel = rhymesViewModelsList[position]
        holder.binding.viewModel = currRhymeViewModel
//        holder.rhymeTextView.text = currRhymeViewModel.displayedText
//        val src = if (currRhymeViewModel.isMarked) {
//            R.drawable.ic_heart_full
//        } else {
//            R.drawable.ic_heart_empty
//        }
//        holder.rhymeButton.setImageResource(src)
    }

    class SearchRhymesViewHolder(
        itemView: View,
        val binding: RhymeItemBinding,
        val adapter: RhymesRecyclerViewAdapter
    ) : RecyclerView.ViewHolder(itemView) {

        val rhymeTextView: TextView = itemView.findViewById(R.id.rhyme_item_text)
        val rhymeButton: ImageButton = itemView.findViewById(R.id.rhyme_item_button)
    }
}