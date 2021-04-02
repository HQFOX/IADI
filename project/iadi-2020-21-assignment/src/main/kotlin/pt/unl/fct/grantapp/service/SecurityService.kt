package pt.unl.fct.grantapp.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

import pt.unl.fct.grantapp.model.ApplicationRepository
import pt.unl.fct.grantapp.model.GrantCallRepository
import pt.unl.fct.grantapp.model.InstitutionRepository
import pt.unl.fct.grantapp.model.StudentRepository

import pt.unl.fct.grantapp.model.*
import pt.unl.fct.grantapp.model.dao.ApplicationDAO



@Service
class SecurityService(
        var grants: GrantCallRepository,
        var students: StudentRepository,

        var institutions: InstitutionRepository,
        var applications: ApplicationRepository,
        var reviewers: ReviewerRepository,
        var panels : EvaluationPanelRepository

) {

    fun canEditGrant(principal: UserDetails, grantId: Long): Boolean {
        val grant = grants.findById(grantId)
        return (grant.isPresent && grant.get().sponsor.username == principal.username)
    }

    fun canEditStudent(principal: UserDetails, studentId: Long): Boolean { // studentId can be institutionId
        var finalBooleanAccess = false


        val student = students.findById(studentId) // checking if its id student

        val institution = institutions.findInstitutionDAOByUsername(principal.username)

        if (student.isPresent) {
            if (student.get().username == principal.username) finalBooleanAccess = true
        }

        if (institution.isPresent) {
            if (institution.get().name == student.get().institution.name) finalBooleanAccess = true
        }
        return finalBooleanAccess
    }

    fun canAddStudent(principal: UserDetails): Boolean {
        // TODO - Verificar se é uma institution e pode adicionar um estudante ao sistema.
        return true

    }


    fun canAddReview(principal: UserDetails): Boolean {
      //  val institution = institutions.findInstitutionDAOByUsername(principal.username)
       // return institution.isPresent
        return true
    }

    fun canModifyApplication(principal: UserDetails, appId: Long): Boolean {
        val app = applications.findByIdWithStudent(appId)
        return (app.isPresent && principal.username == app.get().student.username)
    }

    fun isOwnerOfGrant(principal: UserDetails, grantId: Long): Boolean {
        val grant = grants.findByIdWithSponsor(grantId)
        return (grant.isPresent && principal.username == grant.get().sponsor.username)
    }

    fun canUpdateApplicationStatus(principal: UserDetails, applicationId:Long) : Boolean {
        //TODO - Verificar se o reviewer é chair do evaluation panel
        var application = applications.findById(applicationId)
            if(!application.isEmpty){
                var grantId: Long = application.get().grant.id
                var grant = grants.findById(grantId)
                if(!grant.isEmpty){
                    var evalPanelId: Long = grant.get().panel.id
                    var panel = panels.findById(evalPanelId)
                    if(!panel.isEmpty){
                        var chairId: Long = panel.get().chair.id
                        var chair = reviewers.findById(chairId)
                            if(chair.get().username == principal.username)
                                return true
                    }
                }
            }

        return false
    }


}