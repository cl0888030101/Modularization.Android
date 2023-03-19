package com.eyeque.enterprisedatamodule.model.response

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(val userDetails: UserDetails?)

data class UserDetails(
    val firstName: String?,
    val lastName: String?,
    val license: String?,
    val occupation: Occupation?,
    val phoneNumber: String?,
    val signatureImage: String?,
    val agreeTerms: Boolean?,
    val email: String

)

data class Occupation(
   val occupation: String?,
   val abbreviation: String?,
   val requiresLicense: Boolean?
)


