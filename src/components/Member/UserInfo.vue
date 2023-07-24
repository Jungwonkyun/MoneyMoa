<template lang="">
  <v-container align="center">
    <v-img
      :width="300"
      aspect-ratio="16/9"
      cover
      src="https://cdn.vuetifyjs.com/images/parallax/material.jpg"
    ></v-img>
    <div>
      {{ nickname }}
    </div>
    <div>
      {{ aboutMe }}
    </div>
    <v-btn @click="following">팔로잉</v-btn>
    <v-btn>DM보내기</v-btn>
  </v-container>
</template>

<script>
import { ref } from 'vue'
import functions from '@/api/member.js'

export default {
  setup() {
    const profileImage = ref('프로필 이미지')
    const nickname = ref('닉네임')
    const aboutMe = ref('자기소개')
    const following = functions.followingApiCall

    return {
      nickname,
      aboutMe,
      profileImage,
      following
    }
  }
}
</script>
<style lang=""></style>
