package com.example.mainactivity.models

/**
 * Created by Huu Hoang on 2019-05-22.
 */

data class User(
    var fullName: String? = "",
    var userName: String? = "",
    var id: String? = "",
    var email: String? = "",
    var imageURL: String

) {
    constructor(): this("","","","", "")

}