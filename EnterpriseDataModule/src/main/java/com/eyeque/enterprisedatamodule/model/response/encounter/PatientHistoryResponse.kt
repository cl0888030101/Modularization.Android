package com.eyeque.enterprisedatamodule.model.response.encounter

 data class PatientHistoryResponse(
    val alcoholUse: String,
    val allergyHistory: List<String>,
    val contactsSatisfaction: String,
    val currentOtherDiseases: String?,
    val digitalPrescriptions: Boolean,
    val diseaseHistory: List<DiseaseHistory>,
    val eyeglassSatisfaction: String?,
    val lastExamDate: String?,
    val medicationHistory: List<String>,
    val otherDiseasesAfterExam: String?,
    val previousOtherDiseases: String?,
    val socialHistory: String?,
    val telemedicineVisit: Boolean,
    val tobaccoUse: String?,
    val uncorrectedODAcuity: String?,
    val uncorrectedOSAcuity: String?,
    val uncorrectedPD: Double?
)

data class DiseaseHistory(
    val name: String,
    val type: String
)