package pt.unl.fct.grantapp.service

import org.springframework.stereotype.Service
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.EvaluationPanelRepository
import pt.unl.fct.grantapp.model.dao.EvaluationPanelDAO
import pt.unl.fct.grantapp.model.dao.ReviewDAO
import pt.unl.fct.grantapp.model.dao.ReviewerDAO

@Service
class EvaluationPanelService(val evaluationPanelRepository: EvaluationPanelRepository) {

    fun getAll(): MutableList<EvaluationPanelDAO> = evaluationPanelRepository.findAll()

    fun getOne(id: Long): EvaluationPanelDAO = evaluationPanelRepository.findById(id)
            .orElseThrow {
                NotFoundException("There is no Evaluation Panel with id: $id")
            }

    fun addOne(panel: EvaluationPanelDAO) = evaluationPanelRepository.save(panel)

    fun update(newEvaluationPanel: EvaluationPanelDAO) =
            getOne(newEvaluationPanel.id)
    //TODO COMPLETAR

    fun delete(id: Long) {
        getOne(id).let { evaluationPanelRepository.delete(it) }
    }

    fun getPanelChair(id: Long): ReviewerDAO =
            getOne(id).let { it.chair }

    fun updatePanelChair(id: Long, chair: ReviewerDAO) {
        getOne(id).let { it.updateChair(chair); evaluationPanelRepository.save(it) }
    }

    fun getAllMembers(id: Long): Iterable<ReviewerDAO> =
            getOne(id).let { it.members }

    fun addOneMember(panelId: Long, reviewerDAO: ReviewerDAO) =
        getOne(panelId).let { it.addMember(reviewerDAO) ; evaluationPanelRepository.save(it) }


    fun deleteOneMember(panelId: Long, reviewerDAO: ReviewerDAO) {
        getOne(panelId).let { it.removeMember(reviewerDAO); evaluationPanelRepository.save(it) }
    }

    fun getAllReviews(id: Long): Iterable<ReviewDAO> =
            getOne(id).let { it.reviews }

    fun getPanelsOfChair(id: Long) : Iterable<EvaluationPanelDAO> =
        evaluationPanelRepository.findEvaluationPanelDAOByChair_Id(id)
}