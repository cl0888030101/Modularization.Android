package com.eyeque.enterprisedatamodule.model.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class PatientResponse(
    val patientId: String,
    val id: Long,
    val lastTest: Date?,
    val patientInfo: PatientInfo?)

data class PatientInfo(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val gender: String?,
    val dob: String?,
    val address: PatientAddress?)

data class PatientAddress(
    val country: String?,
    val premise: String?,
    val postalCode: String?,
    val locality: String?,
    @SerializedName("administrativeArea")
    val state: String?,
    @SerializedName("thoroughfare")
    val street:String?)
