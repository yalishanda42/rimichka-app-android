package bg.abv.ani1802.rimichka.favorites

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import bg.abv.ani1802.rimichka.R

class FavoriteRhymesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteRhymesFragment()
    }

    private lateinit var viewModel: FavoriteRhymesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_rhymes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoriteRhymesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
