package ie.koala.fantascope.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wiki(val title: String, val url: String): Parcelable