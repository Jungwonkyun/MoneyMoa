package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Follows;
import com.d210.moneymoa.repository.FollowReposiitory;
import com.d210.moneymoa.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowServiceImpl implements FollowService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowReposiitory followReposiitory;

    @Override
    public Follows followMember(Long fromMemberId, Long toMemberId) {
        String fromMemberNickName  =  memberRepository.findById(fromMemberId).get().getNickname();
        String toMemberNickName  =  memberRepository.findById(toMemberId).get().getNickname();

        Follows follows  = Follows.builder()
                .toMemberId(toMemberId)
                .fromMemberId(fromMemberId)
                .toMemberNickname(toMemberNickName)
                .fromMemberNickname(fromMemberNickName)
                .build();

        followReposiitory.save(follows);

        return follows;
    }

    @Override
    public void unFollowMember(Long fromMemberId, Long toMemberId) {
        Optional<Follows> oFollows = followReposiitory.findByFromMemberIdAndToMemberId(fromMemberId,toMemberId);
        //해당객체가 존재한다면 지워버리기
        oFollows.ifPresent(follows -> followReposiitory.delete(follows));
        return;
    }

    @Override
    public List<Follows> myFollowingList(Long id) {
        return followReposiitory.findByFromMemberId(id);
    }

    @Override
    public List<Follows> myFollowerList(Long id) {
        return followReposiitory.findByToMemberId(id);
    }
}
