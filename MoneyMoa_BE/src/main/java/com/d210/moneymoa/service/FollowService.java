package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Follows;

import java.util.List;

public interface FollowService {
    Follows followMember(Long fromMemberId, Long toMemberId);

    void unFollowMember(Long fromMemberId, Long toMemberId);

    List<Follows> myFollowingList(Long id);

    List<Follows> myFollowerList(Long id);
}
