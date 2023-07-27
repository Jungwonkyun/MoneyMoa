<template>
  <v-container>
    <v-card :elevation="24" justify="center">
      <v-row>
        <v-col cols="12">
          <v-img
            class="profileImage"
            src="https://cdn.vuetifyjs.com/images/parallax/material.jpg"
            cover
          ></v-img>
        </v-col>
      </v-row>
      <v-card-title class="text-center ma-6">
        {{ nickname }}
      </v-card-title>
      <v-card-subtitle class="text-center">한줄소개</v-card-subtitle>
      <v-card-text class="text-center mb-6">
        {{ aboutMe }}
      </v-card-text>
      <v-row class="d-flex justify-space-evenly mt-6 mb-6">
        <v-btn cols="6" @click="following">팔로잉</v-btn>
        <v-btn cols="6">DM보내기</v-btn>
      </v-row>
      <v-divider class="border-opacity-20 mb-6"></v-divider>
      <router-link to="/member/myfeed" class="no-link-style text-center">
        <v-card-title>피드</v-card-title>
      </router-link>
      <router-link to="/member/followerlist" class="no-link-style text-center">
        <v-card-title>팔로워</v-card-title>
      </router-link>
      <router-link to="/member/followinglist" class="no-link-style text-center">
        <v-card-title>팔로잉</v-card-title>
      </router-link>
      <v-row class="ma-2">
        <v-col class="text-center">
          <v-btn>
            <router-link to="/member/myproducts" class="no-link-style text-center"
              >찜한 상품 목록
            </router-link>
          </v-btn>
        </v-col>
      </v-row>
      <v-row class="ma-2">
        <v-col class="text-center">
          <v-btn>
            <router-link to="/member/myproducts" class="no-link-style text-center"
              >정보 수정
            </router-link>
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script>
import { ref } from 'vue'
import functions from '@/api/member.js'

export default {
  setup() {
    const profileImage = ref('프로필 이미지')
    const nickname = ref('뚝시미')
    const aboutMe = ref('안녕하세요 뚝심이에용')
    const following = functions.callFollowingApi

    return {
      nickname,
      aboutMe,
      profileImage,
      following
    }
  }
}
</script>
<style>
.profileImage {
  margin: 20px;
}
.no-link-style {
  text-decoration: none; /* 밑줄 제거 */
  color: inherit; /* 색상 상속 */
  cursor: pointer; /* 커서를 손가락 모양으로 변경 (선택 사항) */
}
</style>
