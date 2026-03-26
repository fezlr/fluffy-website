package fezlr.fluffy.user.controller.api;

import fezlr.fluffy.user.dto.request.UserRequest;
import fezlr.fluffy.user.dto.response.UserResponse;
import fezlr.fluffy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {
    private final UserService userService;

    public ResponseEntity<UserResponse> save(@Valid UserRequest user) {
        log.info("Called saveUser() with BODY = {}", user);
        try {
            return ResponseEntity.ok(userService.save(user));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
