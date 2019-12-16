package bg.abv.ani1802.rimichka.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bg.abv.ani1802.rimichka.R
import bg.abv.ani1802.rimichka.network.Rhyme

class SearchRhymesAdapter(context: Context, private val rhymesList: List<Rhyme>) :
    RecyclerView.Adapter<SearchRhymesAdapter.SearchRhymesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchRhymesViewHolder {
        val itemView = inflater.inflate(R.layout.rhyme_item, parent, false)
        return SearchRhymesViewHolder(itemView, this)
    }

    override fun getItemCount(): Int {
        return rhymesList.size
    }

    override fun onBindViewHolder(holder: SearchRhymesViewHolder, position: Int) {
        val currRhyme = rhymesList[position]
        holder.rhymeTextView.text = currRhyme.word
    }

    class SearchRhymesViewHolder(itemView: View, val adapter: SearchRhymesAdapter) :
        RecyclerView.ViewHolder(itemView) {

        val rhymeTextView: TextView = itemView.findViewById(R.id.rhyme_item_text)
        val rhymeButton: ImageButton = itemView.findViewById(R.id.rhyme_item_button)
    }
}