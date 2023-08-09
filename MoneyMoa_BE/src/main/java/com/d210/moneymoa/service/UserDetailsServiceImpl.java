package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Long userId = Long.parseLong(id);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userid: " + userId));

        // map member role to GrantedAuthority and create User object
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));

        return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), authorities);
    }

//    @Override
//    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
//
//        Long userId = Long.parseLong(id);
//        Member member = memberRepository.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with userid: " + userId));
//
//        return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), new ArrayList<>());
//    }
}

