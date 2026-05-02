package tn.esprit.employee.Controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.employee.Entities.Notification;
import tn.esprit.employee.Entities.NotificationType;
import tn.esprit.employee.Services.IServices.INotificationService;

import javax.crypto.SecretKey;
import java.security.Principal;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private final INotificationService notificationService;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Void> broadcast(@RequestParam String message, @RequestParam NotificationType type) {
        notificationService.sendToAll(message, type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{username}")
    public ResponseEntity<Void> sendToUser(@PathVariable String username, @RequestParam String message, @RequestParam NotificationType type) {
        notificationService.sendToUser(username, message, type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<Page<Notification>> getMyNotifications(
            Principal principal,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        String username = null;
        
        System.out.println(">>> [Manual-Security] /my check started");

        if (principal != null) {
            username = principal.getName();
            System.out.println(">>> [Manual-Security] Identity found via Principal: " + username);
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println(">>> [Manual-Security] Principal NULL, checking Bearer token...");
            try {
                String jwt = authHeader.substring(7);
                SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
                username = claims.getSubject();
                System.out.println(">>> [Manual-Security] Identity RECOVERED via JWT: " + username);
            } catch (Exception e) {
                System.err.println(">>> [Manual-Security] JWT extraction failure: " + e.getMessage());
            }
        } else {
            System.err.println(">>> [Manual-Security] CRITICAL: No Principal and No Bearer Token!");
        }

        if (username == null) {
            System.err.println(">>> [Manual-Security] Access DENIED → 401");
            return ResponseEntity.status(401).build();
        }

        System.out.println(">>> [Manual-Security] Access GRANTED for: " + username);
        return ResponseEntity.ok(notificationService.getMyNotifications(username, PageRequest.of(page, size)));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            Principal principal,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        String username = null;
        if (principal != null) {
            username = principal.getName();
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String jwt = authHeader.substring(7);
                SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
                username = claims.getSubject();
            } catch (Exception e) { }
        }

        if (username == null) return ResponseEntity.ok(0L);
        return ResponseEntity.ok(notificationService.getUnreadCount(username));
    }


}
