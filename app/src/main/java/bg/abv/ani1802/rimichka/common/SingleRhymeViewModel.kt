package bg.abv.ani1802.rimichka.common

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel

class SingleRhymeViewModel(
    val displayedText: String,
    val isMarked: ObservableBoolean = ObservableBoolean(false)
) : ViewModel() {

    fun toggle() {
        isMarked.set(!isMarked.get())
        // TODO: save the rhyme
    }
}