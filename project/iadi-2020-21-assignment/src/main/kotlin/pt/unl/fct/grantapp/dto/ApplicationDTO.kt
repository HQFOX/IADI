package pt.unl.fct.grantapp.dto

import com.fasterxml.jackson.annotation.JsonFormat
import pt.unl.fct.grantapp.model.dao.AppDataItemDAO
import pt.unl.fct.grantapp.model.dao.ApplicationDAO
import java.util.*

data class StudentApplicationDTO(
        val studentId: Long,
        val grantId: Long,
        val application: ApplicationDTO,
        val dataItems: List<AppDataItemDTO>
)

data class ApplicationDTO(
        val applicationId: Long,
        val introduction: String,
        val relatedWork: String,
        val workPlan: String,
        val publications: String,
        val status: String
) {
    constructor(app: ApplicationDAO) : this(app.id, app.introduction, app.relatedWork, app.workPlan, app.publications, app.status)
}

data class MyApplicationDTO(
        val applicationId: Long,
        val introduction: String,
        val relatedWork: String,
        val workPlan: String,
        val publications: String,
        val status: String,
        val grantId: Long,
        val grantTitle: String,
        val grantFunding: Long,
        val grantDescription: String,
        @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
        val grantCloseDate: Date
) {
    constructor(app: ApplicationDAO) : this(app.id, app.introduction, app.relatedWork, app.workPlan, app.publications, app.status, app.grant.id, app.grant.title, app.grant.funding, app.grant.description, app.grant.closeDate)
}

data class AppDataItemDTO(
        val appDataItemId: Long,
        val dataItemId: Long,
        val data: String
) {
    constructor(appDataItem: AppDataItemDAO) : this(appDataItem.id, appDataItem.dataItem.id, appDataItem.data)
}

enum class ApplicationStatus {
    NotEvaluated,
    Declined,
    Approved
}