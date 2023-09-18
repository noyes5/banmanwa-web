package com.banmanwa.web.auth.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/kakao")
public class KakaoController {

    @GetMapping(value = "/oauth")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + SecretKey.KAKAO_API_KEY);
        url.append("&redirect_uri=http://localhost:8080/kakao/callback");
        url.append("&response_type=code");

        return "redirect:" + url.toString();
    }

    @RequestMapping(value = "/callback", produces="application/json", method = {RequestMethod.GET, RequestMethod.POST})
    public String kakaoLogin(@RequestParam("code")String code, RedirectAttributes ra, HttpSession session, HttpServletResponse response, Model model) {

        System.out.println("kakao code :" + code);
        Map<String, String> tokens = getKakaoAccessToken(code);
        System.out.println("access_token = " + tokens.get("access_token"));
        System.out.println("refresh_token = " + tokens.get("refresh_token"));
        return null;
    }

    public Map<String, String> getKakaoAccessToken(String code) {
        String accessToken = "";

        RestTemplate restTemplate = new RestTemplate();
        String reqUrl = "/oauth/token";
        URI uri = URI.create("https://kauth.kakao.com" + reqUrl);

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

        return tokens;
    }
}