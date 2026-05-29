package com.oualid.CRMSaas.Web.Controllers.comercialeController;

import com.oualid.CRMSaas.Entity.Publicite;
import com.oualid.CRMSaas.Services.ComercialeService.PubliciteService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/comerciale/publicite")
public class PubliciteController {

    private final PubliciteService publiciteService;

    public PubliciteController(PubliciteService publiciteService) {
        this.publiciteService = publiciteService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Publicite create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("titre") String titre,
            @RequestParam("contenue") String contenue,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        return publiciteService.create(userDetails.getUsername(), titre, contenue, image);
    }

    @GetMapping
    public Page<Publicite> getAll(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return publiciteService.getAll(userDetails.getUsername(), page, size);
    }

    @GetMapping("/{id}")
    public Publicite getOne(@PathVariable Long id) {
        return publiciteService.getOne(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Publicite update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestParam(value = "titre", required = false) String titre,
            @RequestParam(value = "contenue", required = false) String contenue,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        return publiciteService.update(userDetails.getUsername(), id, titre, contenue, image);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        publiciteService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<Map<String, String>> sendToClients(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody SendRequest request
    ) {
        publiciteService.sendToClients(userDetails.getUsername(), id, request.clientIds(), request.sendAll());
        return ResponseEntity.ok(Map.of("message", "Publication envoyée avec succès"));
    }

    public record SendRequest(List<UUID> clientIds, boolean sendAll) {}
}
