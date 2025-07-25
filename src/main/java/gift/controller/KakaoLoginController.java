package gift.controller;

import gift.dto.KakaoLoginResponseDto;
import gift.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        KakaoLoginResponseDto responseDto = kakaoService.getAccessToken(code);
        System.out.println("responseDto = " + responseDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
