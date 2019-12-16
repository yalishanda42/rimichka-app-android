package bg.abv.ani1802.rimichka.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import bg.abv.ani1802.rimichka.R
import bg.abv.ani1802.rimichka.network.Rhyme

class SearchRhymesFragment : Fragment() {

    companion object {
        fun newInstance() = SearchRhymesFragment()
    }

    private lateinit var viewModel: SearchRhymesViewModel
    private lateinit var searchBar: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView

    var adapter: SearchRhymesAdapter? = null
        set(value) {
            field = value
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

    private var lastSearchedWord: String? = null
    private var searchButtonIsHidden: Boolean = true
        set(value) {
            field = value
            searchButton.visibility = if (value) View.GONE else View.VISIBLE
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_rhymes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBar = view.findViewById(R.id.search_edit_text)
        searchButton = view.findViewById(R.id.search_button)
        recyclerView = view.findViewById(R.id.fetched_rhymes_recycler_view)

        searchButton.setOnClickListener { _ -> searchWordForRhymes() }

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(
                newSearchedWord: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                newSearchedWord?.let { newWord ->
                    lastSearchedWord?.let { oldWord ->
                        if (oldWord == newWord) {
                            searchButtonIsHidden = true
                        }
                    } ?: run {
                        if (newSearchedWord != "" && searchButtonIsHidden) {
                            searchButtonIsHidden = false
                        }
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchRhymesViewModel::class.java)
    }

    private fun searchWordForRhymes() {
        searchBar.text?.toString()?.let { searchWord ->
            lastSearchedWord = searchWord
            viewModel.fetchRhymesFor(searchWord)
        }
    }

}
