package bg.abv.ani1802.rimichka.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import bg.abv.ani1802.rimichka.R

class SearchRhymesFragment : Fragment() {

    companion object {
        fun newInstance() = SearchRhymesFragment()
    }

    private lateinit var viewModel: SearchRhymesViewModel
    private lateinit var searchBar: EditText
    private lateinit var searchButton: Button

    private var lastSearchedWord: String? = null
    private var searchButtonIsHidden: Boolean = true
        set(value: Boolean) {
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

        searchButton.setOnClickListener { v: View -> searchWordForRhymes(v) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchRhymesViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun searchWordForRhymes(view: View) {
        lastSearchedWord = searchBar.text.toString()
        // TODO: Ask the ViewModel to fetch rhymes
        // TODO: Update UI
    }

}
