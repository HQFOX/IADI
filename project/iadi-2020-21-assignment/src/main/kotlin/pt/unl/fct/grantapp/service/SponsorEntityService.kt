package pt.unl.fct.grantapp.service

import javassist.NotFoundException
import org.springframework.stereotype.Service
import pt.unl.fct.grantapp.model.SponsorEntityRepository
import pt.unl.fct.grantapp.model.dao.GrantCallDAO
import pt.unl.fct.grantapp.model.dao.SponsorEntityDAO

@Service
class SponsorEntityService(val sponsors: SponsorEntityRepository) {

    fun getAll(): Iterable<SponsorEntityDAO> = sponsors.findAll()
    fun addOne(sponsor: SponsorEntityDAO) = sponsors.save(sponsor)

    fun getOne(id: Long): SponsorEntityDAO = sponsors.findById(id).orElseThrow {
        NotFoundException("Sponsor with id $id not found")
    }

    fun getGrantCalls(id: Long): Iterable<GrantCallDAO> {
        val sponsor = sponsors.getGrantsById(id)
                .orElseThrow { NotFoundException("There is no sponsor entity with id $id") }

        return sponsor.grantCalls
    }

    fun updateSponsor(id: Long, sponsor: SponsorEntityDAO) =
            getOne(id).let { it.update(sponsor); sponsors.save(it) }

    fun deleteSponsor(id: Long) = getOne(id).let { sponsors.delete(it) }


}