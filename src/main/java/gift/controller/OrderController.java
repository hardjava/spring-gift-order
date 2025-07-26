package gift.controller;

import gift.auth.LoginMember;
import gift.domain.Member;
import gift.dto.OrderRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<?> order(
            @LoginMember Member member,
            @Valid @RequestBody OrderRequestDto requestDto) {

        System.out.println("requestDto = " + requestDto);
        return null;
    }
}
