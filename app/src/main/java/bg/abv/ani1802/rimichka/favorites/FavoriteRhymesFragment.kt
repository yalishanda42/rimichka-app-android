package bg.abv.ani1802.rimichka.favorites

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import bg.abv.ani1802.rimichka.R
import bg.abv.ani1802.rimichka.common.RhymesRecyclerViewAdapter

class FavoriteRhymesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteRhymesFragment()
    }

    private lateinit var viewModel: FavoriteRhymesViewModel
    private lateinit var recyclerView: RecyclerView

    private var adapter: RhymesRecyclerViewAdapter? = null
        set(value) {
            field = value
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_rhymes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.favorite_rhymes_recycler_view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoriteRhymesViewModel::class.java)
        viewModel.favoriteRhymesViewModels.observe(this, Observer {
            context?.let { context ->
                adapter = RhymesRecyclerViewAdapter(context, it)
            }
        })
    }

}
