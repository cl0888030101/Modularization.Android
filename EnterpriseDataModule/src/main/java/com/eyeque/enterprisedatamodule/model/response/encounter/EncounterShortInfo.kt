package com.eyeque.enterprisedatamodule.model.response.encounter

data class EncounterShortInfo(
    val createdAt: String,
    val id: Long,
    val optician: Optician,
    val opticianQhState: String,
    val status: String
)

data class Optician(
    val userDetails: UserDetails
)

data class UserDetails(
    val email: String,
    val firstName: String,
    val id: Long,
    val lastName: String,
    val phoneNumber: String
)