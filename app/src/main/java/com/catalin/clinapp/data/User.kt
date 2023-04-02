package com.catalin.clinapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val name: String? = null, val email: String? = null, val phone: String? = null, val type: String? = "Patient") :
    Parcelable {}
