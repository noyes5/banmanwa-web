package com.banmanwa.web.auth.controller;

import static com.banmanwa.web.auth.domain.Naver.NAVER_AUTH_URI;

import com.banmanwa.web.auth.domain.Naver;
import com.banmanwa.web.auth.dto.ProfileDto;
import com.banmanwa.web.auth.dto.ProfileTokenDto;
import com.banmanwa.web.auth.dto.TokenDto;
import com.banmanwa.web.auth.service.AuthService;
import com.banmanwa.web.member.service.MemberService;
import com.banmanwa.web.secret.SecretKey;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping("/api/naver")
public class NaverController {

    private final MemberService memberService;
    private final AuthService authService;

    public NaverController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/oauth")
    public String naverConnect() {
        String state = Naver.generateState();

        StringBuffer url = new StringBuffer();
        url.append(NAVER_AUTH_URI + "/oauth2.0/authorize?");
        url.append("client_id=" + SecretKey.NAVER_API_KEY);
        url.append("&response_type=code");
        url.append("&redirect_uri=http://localhost:8080/api/naver/callback");
        url.append("&state=" + state);

        return "redirect:" + url;
    }

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST}
            , produces = "application/json")
    public ResponseEntity<ProfileTokenDto> naverLogin(@RequestParam(value = "code") String code
            , @RequestParam(value = "state") String state) {
        TokenDto response = Naver.getAccessToken(code, state);

        ProfileDto profile = Naver.getUserInfo(response.getAccess_token());
        memberService.add(profile);
        ProfileTokenDto profileTokenDto = authService.createToken(profile);

        return ResponseEntity.ok().body(profileTokenDto);
    }

    @GetMapping(value = "/logout")
    public String naverLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("access_token");
        WebClient webclient = WebClient.builder()
                .baseUrl(NAVER_AUTH_URI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        webclient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2.0/token")
                        .queryParam("client_id", SecretKey.NAVER_API_KEY)
                        .queryParam("client_secret", SecretKey.NAVER_CLIENT_SECRET)
                        .queryParam("grant_type", "delete")
                        .queryParam("access_token", accessToken)
                        .queryParam("service_provider", "NAVER")
                        .build())
                .retrieve().bodyToMono(String.class).block();

        return "redirect:/";
    }
}
