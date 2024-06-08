package agilebit.ordermanagement.controllers;

import agilebit.ordermanagement.dtos.PartnerDTO;
import agilebit.ordermanagement.services.PartnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partners")
@AllArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping
    public ResponseEntity<PartnerDTO> addPartner(@RequestBody PartnerDTO partnerDTO) {
        PartnerDTO createdPartner = partnerService.addPartner(partnerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPartner);
    }

    @GetMapping
    public ResponseEntity<List<PartnerDTO>> getAllPartners() {
        List<PartnerDTO> partners = partnerService.getAllPartners();
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerDTO> getPartnerById(@PathVariable String id) {
        PartnerDTO partner = partnerService.getPartnerById(id);
        if (partner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(partner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerDTO> updatePartner(@PathVariable String id,
                                                    @RequestBody PartnerDTO partnerDetails) {
        PartnerDTO updatedPartner = partnerService.updatePartner(id, partnerDetails);
        if (updatedPartner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPartner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable String id) {
        boolean deleted = partnerService.deletePartner(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
