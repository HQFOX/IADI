package pt.unl.fct.grantapp.service

import org.springframework.stereotype.Service
import pt.unl.fct.grantapp.exceptions.NotFoundException
import pt.unl.fct.grantapp.model.ReviewRepository
import pt.unl.fct.grantapp.model.dao.ReviewDAO


@Service
class ReviewService(val reviewRepository: ReviewRepository) {

    fun getAll(): MutableList<ReviewDAO> =
            reviewRepository.findAll()

    fun getOne(id: Long): ReviewDAO = reviewRepository.findById(id).orElseThrow {
        NotFoundException("Review with id $id not found")
    }

    fun addOne(review: ReviewDAO) =
        //if(review.reviewer.institution != review.application.student.institution) {
            reviewRepository.save(review)
        //}



    fun updateReview(id: Long, reviewer: ReviewDAO) =
            getOne(id).let { it.update(reviewer); reviewRepository.save(it) }

    fun delete(id: Long) = getOne(id).let { reviewRepository.delete(it) }

    fun getAllByAppId(appId: Long): Iterable<ReviewDAO> = reviewRepository.findByAppIdWithAll(appId);
}