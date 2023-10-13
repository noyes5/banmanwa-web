package com.banmanwa.web.auth.controller;

import com.banmanwa.web.auth.domain.Kakao;
import com.banmanwa.web.auth.dto.ProfileDto;
import com.banmanwa.web.member.domain.Member;
import com.banmanwa.web.member.service.MemberService;
import com.banmanwa.web.secret.SecretKey;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/kakao")
public class KakaoController {

    private static final String KAKAO_HOST_URI = "https://kapi.kakao.com";
    private static final String KAKAO_AUTH_URI = "https://kauth.kakao.com";

    private final MemberService memberService;

    public KakaoController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/oauth")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append(KAKAO_AUTH_URI + "/oauth/authorize?");
        url.append("client_id=" + SecretKey.KAKAO_API_KEY);
        url.append("&redirect_uri=http://localhost:8080/kakao/callback");
        url.append("&response_type=code");

        return "redirect:" + url.toString();
    }

    @RequestMapping(value = "/callback", produces = "application/json", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ProfileDto> kakaoLogin(@RequestParam("code") String code, HttpSession session) {
        Map<String, String> tokens = getKakaoAccessToken(code);
        session.setAttribute("access_token", tokens.get("access_token"));

        return ResponseEntity.ok().body(Kakao.getKakaoUserInfo(tokens.get("access_token")));
    }

    private JSONObject getKakaoUserInfo(String access_token) {
        RestTemplate restTemplate = new RestTemplate();
        String reqUrl = "/v2/user/me";
        URI uri = URI.create(KAKAO_HOST_URI + reqUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> apiResponse = restTemplate.postForEntity(uri, restRequest, JSONObject.class);
        JSONObject responseBody = apiResponse.getBody();

//        Map<String, Object> map = (Map<String, Object>) responseBody.get("kakao_account");
        return responseBody;
    }

    public Map<String, String> getKakaoAccessToken(String code) {
        String accessToken = "";

        RestTemplate restTemplate = new RestTemplate();
        String reqUrl = "/oauth/token";
        URI uri = URI.create(KAKAO_AUTH_URI + reqUrl);

        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.set("grant_type", "authorization_code");
        param.set("client_id", SecretKey.KAKAO_API_KEY);
        param.set("redirect_uri", "http://localhost:8080/kakao/callback");
        param.set("code", code);

        HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(param, headers);
        ResponseEntity<JSONObject> apiResponse = restTemplate.postForEntity(uri, restRequest, JSONObject.class);
        JSONObject responseBody = apiResponse.getBody();

        HashMap<String, String> tokens = new HashMap<>();

        accessToken = (String) responseBody.get("access_token");
        String refreshToken = (String) responseBody.get("refresh_token");

        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

/*        RestTemplate template = new RestTemplateBuilder()
            .defaultHeader("Authorization", "Bearer " + accessToken)
            .build();
        template.postForEntity(uri, restRequest, JSONObject.class);*/

        return tokens;
    }

    @GetMapping(value = "/logout")
    public String kakaoLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("access_token");
        WebClient webClient = WebClient.builder()
                .baseUrl(KAKAO_HOST_URI + "/v1/user/logout")
                .build();

        webClient.post()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve().bodyToMono(String.class).block();

        session.removeAttribute("access_token");
        session.removeAttribute("refresh_token");
        return "redirect:/";
    }
}