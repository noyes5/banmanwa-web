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
import java.util.Enumeration;
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
    public String kakaoLogin(@RequestParam("code") String code, RedirectAttributes ra, HttpSession session, HttpServletResponse response, Model model) {

        System.out.println("kakao code :" + code);
        Map<String, String> tokens = getKakaoAccessToken(code);
        System.out.println(tokens.get("access_token"));

        // 사용자 정보 가져오기
        getKakaoUserInfo(tokens.get("access_token"));
        session.setAttribute("access_token", tokens.get("access_token"));
        return null;
    }

    private void getKakaoUserInfo(String access_token) {
        RestTemplate restTemplate = new RestTemplate();
        String reqUrl = "/v2/user/me";
        URI uri = URI.create("https://kapi.kakao.com" + reqUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> apiResponse = restTemplate.postForEntity(uri, restRequest, JSONObject.class);
        JSONObject responseBody = apiResponse.getBody();

        Map<String, Object> map = (Map<String, Object>) responseBody.get("kakao_account");
        System.out.println(map);
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

    @GetMapping(value = "/logout")
    public void kakaoLogout(HttpSession session) {
        final Enumeration<String> attributeNames = session.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            System.out.println(attributeNames.nextElement());
        }

        String accessToken = (String) session.getAttribute("access_token");
        System.out.println("accessToken : " + accessToken);

        RestTemplate restTemplate = new RestTemplate();
        String reqUrl = "/v1/user/logout";
        URI uri = URI.create("https://kapi.kakao.com" + reqUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> apiResponse = restTemplate.postForEntity(uri, restRequest, JSONObject.class);
        JSONObject responseBody = apiResponse.getBody();
        session.removeAttribute("access_token");
        Object id = responseBody.get("id");
        System.out.println("user id : " + id);
    }
}