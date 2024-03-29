package com.gukbit.service;


import com.gukbit.domain.*;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.repository.*;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthUserDataRepository authUserDataRepository;
    private final RateRepository rateRepository;
    private final CourseRepository courseRepository;
    private final PreAuthUserDataRepository preAuthUserDataRepository;
    private final UploadFileRepository uploadFileRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final ImageService imageService;

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
    }

    //유저의 값이 존재하면 수정 없으면 저장 안함
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

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public List<User> getSearchUserList(String userId) {
        return userRepository.findByUserIdContaining(userId);
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public void deleteUserRole(String userId) {
        User user = userRepository.findByUserId(userId);
        user.setRole("ROLE_USER");
        userRepository.save(user);
        authUserDataRepository.delete(authUserDataRepository.findByUserId(userId));
    }

    public void lockToggle(JSONObject jsonObject) {
        String userId = (String) jsonObject.get("userId");
        Boolean lockUser = (Boolean) jsonObject.get("userLock");
        User user = userRepository.findByUserId(userId);
        user.setLockUser(!lockUser);
        userRepository.save(user);
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
        } catch (IOException e) {
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

            return ocrInfo;
        } catch (Exception e) {
            return ocrInfo;
        }
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
    public String findIdByEmail(String email) {
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
    public int checkEmail(String id, String email) {
        User user = userRepository.findByUserId(id);
        if (user.getEmail().equals(email)) {
            return 1;
        }
        return 0;
    }

    // 해당 id의 정보와 전화번호가 일치하는가
    public int checkTel(String id, String tel) {
        User user = userRepository.findByUserId(id);
        if (user.getTel().equals(tel)) {
            return 1;
        }
        return 0;
    }

    public void changePassword(String id, String password) {
        User user = userRepository.findByUserId(id);
        user.setPassword(password);
        updateUser(user);
    }


    public User checkUser(CustomUserDetails customUserDetails) {
        return userRepository.findByUserId(customUserDetails.getUser().getUserId());
    }

    public List<PreAuthUserData> getPreAuthUserDataList() {
        return preAuthUserDataRepository.findAll();
    }

    public PreAuthUserData getPreAuthUserData(Integer aid) {
        return preAuthUserDataRepository.findById(aid).orElse(null);
    }

    public Boolean setPreAuthUser(UploadFile saveFile, CustomUserDetails customUserDetails, PreAuthUserData preAuthUserData) {
        String courseId = preAuthUserData.getCourseId();
        int session = preAuthUserData.getSession();
        try {
            preAuthUserData.setUserId(customUserDetails.getUser().getUserId());
            preAuthUserData.setCourseName(courseRepository.findByIdAndSession(courseId, session).getName());
            preAuthUserData.setSaveFileName(saveFile.getSaveFileName());
            preAuthUserData.setAcademyCode(courseRepository.findByIdAndSession(courseId, session).getAcademyCode());
            preAuthUserData.setFilePath(saveFile.getFilePath());
            preAuthUserData.setUploadFilePath(
                    String.valueOf(new StringBuilder(saveFile.getFilePath()).delete(0, 25)));
            preAuthUserData.setRegisterDate(LocalDateTime.now());
            /* DB 저장 */
            preAuthUserDataRepository.save(preAuthUserData);
            /* user 권한 숫자 변경 */
            User user = userRepository.findByUserId(customUserDetails.getUser().getUserId());
            user.setRole("ROLE_PRE_AUTH");
            updateUser(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            /* 오류 발생 시 저장했던 사진파일 삭제 */
            uploadFileRepository.delete(saveFile);
            String filePath = saveFile.getFilePath();
            File deleteFile = new File(filePath);
            if (deleteFile.exists()) {
                deleteFile.delete();
            } else {
            }
            return false;
        }
    }

    @Transactional
    public void checkUserRate(String username) throws NullPointerException {
        if (authUserDataRepository.findByUserId(username) != null) {
            if (rateRepository.findByUserId(username) != null) {
                rateRepository.deleteByUserId(username);
            }
            authUserDataRepository.delete(authUserDataRepository.findByUserId(username));
        }
    }

    public PreAuthUserData getPreAuthUserDataByUserId(String userId) {
        return preAuthUserDataRepository.findByUserId(userId);
    }

    public Page<Board> checkUserBoard(String userId, Pageable pageable){
        Sort sort = Sort.by("date").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        Page<Board> userBoard = boardRepository.findAllByAuthor(userId, pageable);
        return userBoard;
    }
    public Page<Reply> checkUserReply(String userId, Pageable pageable){
        Sort sort = Sort.by("rDate").descending();
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);
        Page<Reply> userReply = replyRepository.findAllByrAuthor(userId, pageable);
        return userReply;
    }

    public UserDetails getRecentUserDetails(){
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    @Transactional
    public void saveProfileImage(MultipartFile profileFile, String selectedBasicProfile) {
        String rootLocation = "src/main/resources/static/images/mypage/profile/";
        String userId = getRecentUserDetails().getUsername();
        User user = userRepository.findByUserId(userId);
        if(user.getProfileImageName()!=null && !(user.getProfileImageName().equals("1"))
            && !(user.getProfileImageName().equals("2")) && !(user.getProfileImageName().equals("3"))){
            imageService.deleteFile(user.getProfileImageName());
        }
        if (!profileFile.getOriginalFilename().isEmpty()){  // 이미지 첨부 시 무조건 이미지로 저장
            try {
                UploadFile saveFile = imageService.store(rootLocation,profileFile);
                user.setProfileImageName(String.valueOf(saveFile.getSaveFileName()));
                userRepository.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            switch (selectedBasicProfile){
                case "0": user.setProfileImageName(null);
                    userRepository.save(user);
                    break;
                case "1": user.setProfileImageName("1");
                    userRepository.save(user);
                    break;
                case "2": user.setProfileImageName("2");
                    userRepository.save(user);
                    break;
                case "3": user.setProfileImageName("3");
                    userRepository.save(user);
                    break;
            }

        }
    }
    public void modifyRole(String userId, String role){
        User user = userRepository.findByUserId(userId);
        user.setRole(role);
        userRepository.save(user);
    }
}
