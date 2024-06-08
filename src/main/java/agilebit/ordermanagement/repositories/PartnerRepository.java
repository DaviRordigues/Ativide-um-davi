package agilebit.ordermanagement.repositories;

import agilebit.ordermanagement.entities.PartnerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends MongoRepository<PartnerEntity, String> {
}
