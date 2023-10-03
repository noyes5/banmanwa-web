package com.banmanwa.web.auth.controller;

import static com.banmanwa.web.auth.domain.Kakao.KAKAO_AUTH_URI;
import static com.banmanwa.web.auth.domain.Kakao.KAKAO_HOST_URI;

import com.banmanwa.web.secret.SecretKey;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import com.banmanwa.web.auth.domain.Kakao;


@Controller
@RequestMapping("/kakao")
public class KakaoController {

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
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session) {
        Map<String, String> tokens = Kakao.getKakaoAccessToken(code);
        session.setAttribute("access_token", tokens.get("access_token"));

        // 사용자 정보 받아오기
        JSONObject userInfo = Kakao.getKakaoUserInfo(tokens.get("access_token"));
        return null;
    }

    @GetMapping(value = "/logout")
    public void kakaoLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("access_token");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(KAKAO_HOST_URI + "/v1/user/logout");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        session.removeAttribute("access_token");
    }
}