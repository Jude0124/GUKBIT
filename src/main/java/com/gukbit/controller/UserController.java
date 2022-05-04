package com.gukbit.controller;


import com.gukbit.domain.User;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.service.UserService;
import com.gukbit.session.SessionConst;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //  회원가입
    @PostMapping("/processRegister")
    public String processRegistration(User user) {
        try {
            userService.joinUser(user);
            System.out.println("UserController.processRegistration");
            return "/view/register/register-success";
        } catch (DataIntegrityViolationException e) {
            System.out.println("email already exist");
            return "/view/register/register-fail";
        }
    }

    //  아이디 중복확인
    @PostMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestBody String id) throws Exception {
        int count = 0;
        if (id != null) count = userService.idCheck(id);
        return count;
    }

    @GetMapping("/mypageAuth")
    public String myPageAuthGet(Model model){
        model.addAttribute("pwCheck", new PwCheck());
        return "view/mypage/mypage-auth";
    }

    @PostMapping("/mypage")
    public String joinMyPage(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model,
                             @ModelAttribute PwCheck pwCheck, BindingResult bindingResult) {


        if(pwCheck.getPassword().equals(""))
            bindingResult.addError(new FieldError("pwCheck","password","비밀번호가 비었습니다."));
        else if(!pwCheck.getPassword().equals(loginUser.getPassword()))
            bindingResult.addError(new FieldError("pwCheck","password","비밀번호가 다릅니다"));
        
        
        if (bindingResult.hasErrors()) {
            return "view/mypage/mypage-auth";
        }



        UpdateUserData updateUserData = new UpdateUserData(loginUser);
        userService.makeUpdateUser(updateUserData);
        model.addAttribute("updateUserData", updateUserData);

        return "/view/mypage/mypage";
    }

    @PostMapping("/mypage/update")
    public String updateMyPage(@ModelAttribute UpdateUserData updateUserData, BindingResult bindingResult, HttpServletRequest request) {
        userService.makeUpdateUser(updateUserData);
        userService.updateCheck(updateUserData, bindingResult,request);

        if (bindingResult.hasErrors()) {
            return "view/mypage/mypage";
        }
        return "redirect:/";
    }

    @GetMapping("/mypage/delete")
    public String deleteMyPage(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, HttpServletRequest request) {
        userService.deleteUser(loginUser);
        //성공 했다면
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @Getter @Setter //여기서만 쓸 클래스
    public static class PwCheck {
        String password;
    }

    /* mypage OCR 사진 업로드 */
    @PostMapping("/mypage/ocr")
    public String testOcr(@RequestParam("ocrFile") MultipartFile ocrFile) throws IOException {
        String apiURL = "https://aebb11c320dd4cfca45990eca440b43f.apigw.ntruss.com/custom/v1/15639/ff487b347776c3629bf3c0d5b8f4a98702e2fcd1740893ec1c61b6cd4c00eb33/infer";
        String secretKey = "Vk51RlZ3eHlJQ0JkamhXbUlBbkR1QkFITm1WRWx6aGo=";
        String imageFile = ocrFile.getOriginalFilename();
        System.out.println(imageFile);

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod("POST");
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());

            JSONObject image = new JSONObject();
            image.put("format", imageFile.substring(imageFile.lastIndexOf(".")+1));
            System.out.println(imageFile.substring(imageFile.lastIndexOf(".")+1));
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.add(image);
            json.put("images", images);
            String postParams = json.toString();

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            long start = System.currentTimeMillis();
            File file = new File(imageFile);
            writeMultiPart(wr, postParams, file, boundary);
            wr.close();

            /* 서버 응답 */
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/";
    }
    private static void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws
        IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }
}