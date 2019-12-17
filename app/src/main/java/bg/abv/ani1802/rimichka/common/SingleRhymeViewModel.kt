package bg.abv.ani1802.rimichka.common

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel

class SingleRhymeViewModel(
    val displayedText: String,
    val onToggleListener: (Boolean) -> Unit,
    val onClickRhyme: () -> Unit
) : ViewModel() {

    val isMarked: ObservableBoolean = ObservableBoolean(false)

    fun toggle() {
        isMarked.set(!isMarked.get())
        onToggleListener(isMarked.get())
    }

    fun onClick() {
        onClickRhyme()
    }
}