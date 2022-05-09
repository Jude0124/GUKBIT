package com.gukbit.service;


import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.domain.PreAuthUserData;
import com.gukbit.domain.UploadFile;
import com.gukbit.domain.User;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.PreAuthUserDataRepository;
import com.gukbit.repository.RateRepository;
import com.gukbit.repository.UploadFileRepository;
import com.gukbit.repository.UserRepository;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthUserDataRepository authUserDataRepository;
    private final RateRepository rateRepository;
    private final CourseRepository courseRepository;
    private final PreAuthUserDataRepository preAuthUserDataRepository;
    private final UploadFileRepository uploadFileRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthUserDataRepository authUserDataRepository,
        RateRepository rateRepository, CourseRepository courseRepository,
        PreAuthUserDataRepository preAuthUserDataRepository,
        UploadFileRepository uploadFileRepository) {
        this.userRepository = userRepository;
        this.authUserDataRepository = authUserDataRepository;
        this.rateRepository = rateRepository;
        this.courseRepository = courseRepository;
        this.preAuthUserDataRepository = preAuthUserDataRepository;
        this.uploadFileRepository = uploadFileRepository;
    }

    public void joinUser(User user) {
        userRepository.save(user);
    }

    public int idCheck(String id) {
        User user = userRepository.findByUserId(id);
        if (user == null) return 0;
        if (user.getUserId().equals(id)) {
            return 1;
        }
        return 0;
    }

    //마이페이지에서 가져온 수정데이터를 가지고 validation 검사 후 유저데이터 조작, 과정정보 수정 및 평점 삭제
    @Transactional
    public void updateCheck(UpdateUserData updateUserData, BindingResult bindingResult, HttpServletRequest request) {
        //비밀번호가 일치하지 않을 때
        if (!updateUserData.getChangePassword().equals(updateUserData.getChangePasswordCheck())) {
            bindingResult.addError(
                new FieldError("updateUserData", "changePassword", "비밀번호가 일치하지 않습니다."));
            bindingResult.addError(
                new FieldError("updateUserData", "changePasswordCheck", "비밀번호가 일치하지 않습니다."));
            return;
        }

        //비밀번호가 비어있지 않을 때 (성공 케이스)
        //비어 있는 경우는 프론트에서 처리
        if (!updateUserData.getChangePassword().isEmpty()
            && !updateUserData.getChangePasswordCheck().isEmpty()) {
            User user = updateUserData.getUser();
            if (updateUserData.getChangePassword() != null)
                user.setPassword(updateUserData.getChangePassword());
            this.updateUser(user);

            updateSession(request, user);
        }

        //만약 드랍박스가 선택 되었다면
        if (request.getParameter("courseDropBox") != null) {
            String[] temp = request.getParameter("courseDropBox").split("/");

            String courseId = temp[0];
            Course course = courseRepository.findAllById(courseId).get(0);
            System.out.println("course = " + course);
            String academyCode = course.getAcademyCode();
            String courseName = course.getName();

            int session = Integer.parseInt(temp[1]);

            //원래 인증이 된 사용자의 경우
            if (updateUserData.getAuthUserData() != null) {
                updateUserData.getAuthUserData().setAcademyCode(academyCode);
                updateUserData.getAuthUserData().setCourseId(courseId);
                updateUserData.getAuthUserData().setCourseName(courseName);
                updateUserData.getAuthUserData().setSession(session);
                updateUserData.getUser().setAuth(1);
                userRepository.save(updateUserData.getUser());
                authUserDataRepository.save(updateUserData.getAuthUserData());
                updateSession(request, updateUserData.getUser());
                if (updateUserData.getRate() != null) {
                    rateRepository.deleteByUserId(updateUserData.getRate().getUserId());
                }
            } else {
                //회원 가입할 때 빈 authUserData와 rate를 만들어 놓으면 좋을거 같다
                AuthUserData authUserData = new AuthUserData(updateUserData.getUser().getUserId(),
                    academyCode, courseId, courseName, session);
                updateUserData.getUser().setAuth(1);
                userRepository.save(updateUserData.getUser());
                authUserDataRepository.save(authUserData);
                updateUserData.setAuthUserData(authUserData);
                updateSession(request, updateUserData.getUser());
            }
        }
    }

    //유저의 값이 존재하면 수정 없으면 저장
    public void updateUser(User user) {
        if (userRepository.findByUserId(user.getUserId()) != null)
            userRepository.save(user);
    }

    //해당 유저 정보 삭제
    public void deleteUser(User user) {
        if (authUserDataRepository.findByUserId(user.getUserId()) != null)
            authUserDataRepository.delete(authUserDataRepository.findByUserId(user.getUserId()));
        if (rateRepository.findByUserId(user.getUserId()) != null)
            rateRepository.delete(rateRepository.findByUserId(user.getUserId()));
        userRepository.delete(user);
    }

    //사용자의 인증정보와 평점 작성 정보를 가져오는 함수
    public void makeUpdateUser(UpdateUserData updateUserData) {
        updateUserData.setAuthUserData(
            authUserDataRepository.findByUserId(updateUserData.getUser().getUserId()));
        updateUserData.setRate(rateRepository.findByUserId(updateUserData.getUser().getUserId()));
    }

    public AuthUserData getAuthUserData(String userId) {
        return authUserDataRepository.findByUserId(userId);
    }

    public void updateSession(HttpServletRequest request, User user) {
        HttpSession userSession = request.getSession();

        //세션에 로그인 유저 정보 저장
        userSession.setAttribute(SessionConst.LOGIN_USER, user);
    }


    public Map<String, String> ocrService(MultipartFile ocrFile) {
        String apiURL = "https://aebb11c320dd4cfca45990eca440b43f.apigw.ntruss.com/custom/v1/15639/ff487b347776c3629bf3c0d5b8f4a98702e2fcd1740893ec1c61b6cd4c00eb33/infer";
        String secretKey = "Vk51RlZ3eHlJQ0JkamhXbUlBbkR1QkFITm1WRWx6aGo=";

        String originalName = ocrFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

        String uuid = UUID.randomUUID().toString();

        Path directoryPath = Paths.get("src/main/resources/static/images/mypage/");
        try {
        // mypage 디렉토리 생성
            Files.createDirectories(directoryPath);
            System.out.println(directoryPath + " 디렉토리가 생성되었습니다.");
        }catch (IOException e) {
            e.printStackTrace();
        }
        String savefileName =
            "src/main/resources/static/images/mypage/" + File.separator + uuid + "_" + fileName;

        Path savePath = Paths.get(savefileName);

        try {
            ocrFile.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> ocrInfo = null;
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
            image.put("format", savefileName.substring(savefileName.lastIndexOf(".") + 1));
            System.out.println(savefileName.substring(savefileName.lastIndexOf(".") + 1));
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.add(image);
            json.put("images", images);
            String postParams = json.toString();

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            long start = System.currentTimeMillis();
            File file = new File(savefileName);
            writeMultiPart(wr, postParams, file, boundary);
            wr.close();
            /* 완료 후 디렉토리 내 파일 삭제 */
            file.delete();
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

            /* response값 json으로 바꾸기 */
            String strResponse = response.toString();

            JSONParser jsonParser = new JSONParser();
            // 문자열을 JSONObject 객체로 변환시키는 jsonParser
            JSONObject jsonObject = (JSONObject) jsonParser.parse(strResponse);
            JSONArray jsonImages = (JSONArray) jsonObject.get("images");
            JSONObject jsonImagesArray = (JSONObject) jsonImages.get(0);
            JSONArray jsonImagesFields = (JSONArray) jsonImagesArray.get("fields");
            ocrInfo = new HashMap<>();
            for (int i = 0; i < jsonImagesFields.size();
                i++) { // 해당 JSONArray객체에 값을 차례대로 가져와서 읽습니다.
                JSONObject imsi = (JSONObject) jsonImagesFields.get(i);
                String name = (String) imsi.get("name");
                String inferText = (String) imsi.get("inferText");
                ocrInfo.put(name, inferText);
            }
            System.out.println(ocrInfo);

            return ocrInfo;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(ocrInfo);
            return ocrInfo;
        }
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

    // 전화번호를 통해 유저 정보 찾기
    public String findIdByTel(String tel) {
        User user = userRepository.findByTel(tel);
        String message;
        if (user == null) {
            message = "회원 정보를 찾을 수 없습니다.";
        } else {
            message = "회원님의 ID는 [" + user.getUserId() + "] 입니다";
        }
        return message;
    }
        // 메일 주소를 통해 유저 정보 찾기
        public String findIdByEmail (String email){
            User user = userRepository.findByEmail(email);
            String message;
            if (user == null) {
                message = "회원 정보를 찾을 수 없습니다.";
            } else {
                message = "회원님의 ID는 [" + user.getUserId() + "] 입니다";
            }
            return message;
        }

        // 해당 id의 정보와 email이 일치하는가
        public int checkEmail (String id, String email){
            User user = userRepository.findByUserId(id);
            if (user.getEmail().equals(email)) {
                return 1;
            }
            return 0;
        }

        // 해당 id의 정보와 전화번호가 일치하는가
        public int checkTel (String id, String tel){
            User user = userRepository.findByUserId(id);
            if (user.getTel().equals(tel)) {
                return 1;
            }
            return 0;
        }

    public void changePassword(String id, String password){
            User user = userRepository.findByUserId(id);
//        System.out.println(user.getPassword()); // 변경 이전 확인
            user.setPassword(password);
//        System.out.println(user.getPassword()); // 변경 이후 확인
            updateUser(user);
        }


        public Boolean setPreAuthUser (UploadFile saveFile, User loginUser, PreAuthUserData preAuthUserData){
            String courseId = preAuthUserData.getCourseId();
            int session = preAuthUserData.getSession();
            try {
                preAuthUserData.setUserId(loginUser.getUserId());
                preAuthUserData.setCourseName(courseRepository.findByIdAndSession(courseId, session).getName());
                preAuthUserData.setSaveFileName(saveFile.getSaveFileName());
                preAuthUserData.setAcademyCode(courseRepository.findByIdAndSession(courseId, session).getAcademyCode());
                preAuthUserData.setFilePath(saveFile.getFilePath());
                preAuthUserData.setUploadFilePath(
                    String.valueOf(new StringBuilder(saveFile.getFilePath()).delete(0,25)));
                preAuthUserData.setRegisterDate(LocalDateTime.now());
                /* DB 저장 */
                preAuthUserDataRepository.save(preAuthUserData);
                System.out.println(preAuthUserData);
                /* user 권한 숫자 변경 */
                User user = userRepository.findByUserId(loginUser.getUserId());
                System.out.println(user);
                user.setAuth(2);
                updateUser(user);
                return true;
            } catch (Exception e){
                e.printStackTrace();
                /* 오류 발생 시 저장했던 사진파일 삭제 */
                uploadFileRepository.delete(saveFile);
                String filePath = saveFile.getFilePath();
                File deleteFile = new File(filePath);
                if (deleteFile.exists()){
                    deleteFile.delete();
                    System.out.println("파일 삭제 완료");
                }
                else {
                    System.out.println("삭제할 파일이 존재하지 않습니다.");
                }
                return false;
            }
        }
        public User checkUser(User loginUser){
        User user=userRepository.findByUserId(loginUser.getUserId());
        return user;
        }

    public PreAuthUserData getPreAuthUserData(String userId) {
        return preAuthUserDataRepository.findByUserId(userId);
    }
}
