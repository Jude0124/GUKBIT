package com.gukbit.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
@Table(name = "pre_auth_user_data")
@NoArgsConstructor
public class PreAuthUserData {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aid;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "academy_code")
    private String academyCode;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "session")
    private Integer session;

    @Column(name = "save_file_name")
    private String saveFileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "upload_file_path")
    private String uploadFilePath;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    public PreAuthUserData(String userId, String academyCode, String courseId, String courseName, Integer session, String saveFileName,
        String filePath, String uploadFilePath, LocalDateTime registerDate) {
        this.userId = userId;
        this.academyCode = academyCode;
        this.courseId = courseId;
        this.courseName = courseName;
        this.session = session;
        this.saveFileName = saveFileName;
        this.filePath = filePath;
        this.uploadFilePath = uploadFilePath;
        this.registerDate = registerDate;
    }
}
