//package com.d210.moneymoa.dto;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//@Setter
//@Getter
//@Entity
//@NoArgsConstructor
//public class AdminUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String email;
//
//    private String role;
//
//    private String nickname;
//
//    private String usernickname;
//
//    private String password;
//
//    @Builder
//    public AdminUser(String email, String nickname, String role, String password) {
//        this.email = email;
//        this.nickname = nickname;
//        this.usernickname = nickname;
//        this.role = role;
//        this.password = password;
//    }
//
//}
