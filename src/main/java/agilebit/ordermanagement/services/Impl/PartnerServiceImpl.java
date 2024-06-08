package agilebit.ordermanagement.services;

import agilebit.ordermanagement.dtos.PartnerDTO;
import agilebit.ordermanagement.entities.PartnerEntity;
import agilebit.ordermanagement.repositories.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Override
    public PartnerDTO addPartner(PartnerDTO partnerDTO) {
        PartnerEntity partner = PartnerEntity.builder()
                .nome(partnerDTO.getNome())
                .build();
        partner.generateId(); // Gera o ID baseado no nome
        PartnerEntity savedPartner = partnerRepository.save(partner);
        return mapToDTO(savedPartner);
    }

    @Override
    public List<PartnerDTO> getAllPartners() {
        return partnerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PartnerDTO getPartnerById(String id) {
        return partnerRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public PartnerDTO updatePartner(String id, PartnerDTO partnerDetails) {
        PartnerEntity existingPartner = partnerRepository.findById(id).orElse(null);
        if (existingPartner == null) {
            return null;
        }
        existingPartner.setNome(partnerDetails.getNome());
        existingPartner.generateId();
        PartnerEntity updatedPartner = partnerRepository.save(existingPartner);
        return mapToDTO(updatedPartner);
    }

    @Override
    public boolean deletePartner(String id) {
        PartnerEntity existingPartner = partnerRepository.findById(id).orElse(null);
        if (existingPartner != null) {
            partnerRepository.delete(existingPartner);
            return true;
        }
        return false;
    }

    private PartnerDTO mapToDTO(PartnerEntity partner) {
        return PartnerDTO.builder()
                .id(partner.getId())
                .nome(partner.getNome())
                .build();
    }
}
