package bg.abv.ani1802.rimichka.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.abv.ani1802.rimichka.R
import bg.abv.ani1802.rimichka.common.RhymesRecyclerViewAdapter
import bg.abv.ani1802.rimichka.databinding.SearchRhymesFragmentBinding

class SearchRhymesFragment : Fragment() {

    companion object {
        fun newInstance() = SearchRhymesFragment()
    }

    private lateinit var binding: SearchRhymesFragmentBinding
    private lateinit var viewModel: SearchRhymesViewModel
    private lateinit var searchBar: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView

    private var adapter: RhymesRecyclerViewAdapter? = null
        set(value) {
            field = value
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_rhymes_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBar = view.findViewById(R.id.search_edit_text)
        searchButton = view.findViewById(R.id.search_button)
        recyclerView = view.findViewById(R.id.fetched_rhymes_recycler_view)

        searchButton.setOnClickListener { onTapSearchButton() }
        searchBar.setOnKeyListener isHandled@ { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                onTapSearchButton()
                return@isHandled true
            }
            return@isHandled false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchRhymesViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.rhymeViewModels.observe(this, Observer { rhymes ->
            context?.let { context ->
                adapter = RhymesRecyclerViewAdapter(context, rhymes)
            }
        })
        viewModel.searchQuery.observe(this, Observer {
            viewModel.onSearchQueryChanged()
        })
    }

    private fun onTapSearchButton() {
        hideKeyboard()
        searchBar.clearFocus()
        viewModel.fetchRhymes()
    }

    private fun hideKeyboard() {
        val context = context ?: return
        val inputManager = getSystemService(context, InputMethodManager::class.java) ?: return
        inputManager.hideSoftInputFromWindow(searchBar.windowToken, 0)
    }
}
