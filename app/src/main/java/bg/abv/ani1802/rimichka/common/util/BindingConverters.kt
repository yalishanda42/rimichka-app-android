package bg.abv.ani1802.rimichka.common.util

import android.view.View
import androidx.databinding.BindingConversion

@BindingConversion
fun convertBooleanToVisibility(isVisible: Boolean): Int {
    return if (isVisible) View.VISIBLE else View.GONE
}