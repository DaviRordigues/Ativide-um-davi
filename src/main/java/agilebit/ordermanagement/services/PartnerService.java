package agilebit.ordermanagement.services;

import agilebit.ordermanagement.dtos.PartnerDTO;

import java.util.List;

public interface PartnerService {
    PartnerDTO addPartner(PartnerDTO partner);
    List<PartnerDTO> getAllPartners();
    PartnerDTO getPartnerById(String id);
    PartnerDTO updatePartner(String id, PartnerDTO partnerDetails);
    boolean deletePartner(String id);
}
