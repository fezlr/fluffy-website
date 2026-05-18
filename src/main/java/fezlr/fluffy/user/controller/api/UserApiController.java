package fezlr.fluffy.user.controller.api;

import fezlr.fluffy.user.dto.request.UserUpdateRequest;
import fezlr.fluffy.user.dto.response.UserUpdateResponse;
import fezlr.fluffy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserApiController {
    private final UserService userService;

    @PatchMapping("/update")
    public ResponseEntity<UserUpdateResponse> update(@Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.update(request));
    }
}
