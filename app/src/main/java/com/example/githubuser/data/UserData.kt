package com.example.githubuser.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    var username: String? = "",
    var photo: String? = "",
    var id: Int? = 0,
    var name: String? = "",
    var company: String? = "",
    var location: String? = "",
    var repository: Int? = 0,
    var followers: String? = "",
    var following: String? = ""
): Parcelable {
}
