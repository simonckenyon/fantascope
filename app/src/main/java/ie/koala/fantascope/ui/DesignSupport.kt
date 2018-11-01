package ie.koala.fantascope.ui

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * see https://gist.github.com/sargunster/418c0fd98eb765fa9aca
 */

fun View.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun View.snackbar(text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun View.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}, actionText: CharSequence, action: (View) -> Unit): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.setAction(actionText, action)
    snack.show()
    return snack
}

fun View.snackbar(text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}, actionText: Int, action: (View) -> Unit): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.setAction(actionText, action)
    snack.show()
    return snack
}
fun Fragment.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar =
        view!!.snackbar(text, duration, init)

fun Fragment.snackbar(text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar =
        view!!.snackbar(text, duration, init)

fun Activity.snackbar(@IdRes view: View, text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar =
        view.snackbar(text, duration, init)

fun Activity.snackbar(@IdRes view: View, text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar =
        view.snackbar(text, duration, init)

fun Fragment.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}, actionText: CharSequence, action: (View) -> Unit): Snackbar =
        view!!.snackbar(text, duration, init, actionText, action)

fun Fragment.snackbar(text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}, actionText: Int, action: (View) -> Unit): Snackbar =
        view!!.snackbar(text, duration, init, actionText, action)

fun Activity.snackbar(@IdRes view: View, text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}, actionText: CharSequence, action: (View) -> Unit): Snackbar =
        view.snackbar(text, duration, init, actionText, action)

fun Activity.snackbar(@IdRes view: View, text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}, actionText: Int, action: (View) -> Unit): Snackbar =
        view.snackbar(text, duration, init, actionText, action)

