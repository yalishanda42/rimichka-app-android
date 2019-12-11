package bg.abv.ani1802.rimichka.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import bg.abv.ani1802.rimichka.R

class SearchRhymesFragment : Fragment() {

    companion object {
        fun newInstance() = SearchRhymesFragment()
    }

    private lateinit var viewModel: SearchRhymesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_rhymes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchRhymesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
