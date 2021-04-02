package pt.unl.fct.grantapp.service

import org.springframework.stereotype.Service
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.ReviewerRepository
import pt.unl.fct.grantapp.model.dao.ReviewerDAO

@Service
class ReviewerService(val reviewerRepository: ReviewerRepository) {

    fun getAll(): MutableList<ReviewerDAO> = reviewerRepository.findAll()

    fun getOne(id: Long): ReviewerDAO = reviewerRepository.findById(id).orElseThrow {
        NotFoundException("Reviewer with id $id not found")
    }

    fun addOne(reviewer: ReviewerDAO) = reviewerRepository.save(reviewer)

    fun updateReviewer(id: Long, reviewer: ReviewerDAO) =
            getOne(id).let { it.update(reviewer); reviewerRepository.save(it) }

    fun delete(id: Long) = getOne(id).let { reviewerRepository.delete(it) }


}