<template>
  <v-container>
    <v-sheet
      max-width="600"
      class="mx-auto mt-8 rounded-lg px-10 py-5"
      elevation="3"
      min-height="700"
      width="100%"
    >
      <h1 class="mb-15 mt-10">이메일로 로그인</h1>
      <p class="text-left my-5">
        <span class="bold"> 아직 회원이 아니신가요? </span>
        <router-link class="router-link-style" :to="{ name: 'signupform' }"
          >회원가입하러 가기</router-link
        >
      </p>
      <!-- 로그인폼 -->
      <v-row>
        <v-col cols="12">
          <v-text-field
            width="100%"
            clearable
            label="아이디(이메일)"
            variant="outlined"
            dense
            v-model="Email"
            :rules="rules"
            @keyup.enter="onLogin"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <v-text-field
            clearable
            label="비밀번호"
            variant="outlined"
            dense
            v-model="password"
            :type="visible ? 'text' : 'password'"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            @click:append-inner="visible = !visible"
            :rules="rules"
            @keyup.enter="onLogin"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col class="d-flex align-center">
          <router-link class="router-link-style" :to="{ name: 'findpassword' }"
            ><span class="underline">비밀번호 찾기</span></router-link
          >
        </v-col>
        <v-col class="text-right">
          <v-btn @click.prevent="onLogin" variant="flat" elevation="3" height="50" class="login-Btn"
            >로그인</v-btn
          >
        </v-col>
      </v-row>
      <v-row class="my-8">
        <v-col class="d-flex align-center" xs="1">
          <v-divider class="border-opacity-25"></v-divider
        ></v-col>
        <v-col class="text-center px-0 xs-10" cols="4">
          <span class="social-Text">소셜 회원으로 이용하기 </span>
        </v-col>
        <v-col class="d-flex align-center"
          ><v-divider class="border-opacity-25"></v-divider>
        </v-col>
      </v-row>
      <v-row>
        <v-col align="center">
          <v-img :src="naverLogo" width="200" @click="naverLogin" class="buttons"></v-img>
        </v-col>
        <v-col align="center">
          <v-img :src="kakaoLogo" width="215" @click="kakaoLogin" class="buttons"></v-img>
        </v-col>
      </v-row>
    </v-sheet>
  </v-container>
</template>

<script setup>
import naverLogo from '../../assets/img/네이버로그인.png'
import kakaoLogo from '../../assets/img/카카오로그인.png'
import functions from '@/api/member.js'
import { ref } from 'vue'
import { useAccountStore } from '@/stores/accountStore.js'
import { useRouter, useRoute } from 'vue-router'
import img from '../../assets/img/default_image.png'

const account = useAccountStore()
const router = useRouter()
const route = useRoute()
const Email = ref(null)
const password = ref(null)

// 로그인 로직, 빈값이면 경고창
// 로그인 구현시 - 로그인 실패시 비밀번호 초기화
async function onLogin() {
  if (!Email.value) {
    return alert('이메일을 입력해 주세요.')
  } else if (!password.value) {
    return alert('비밀번호를 입력해 주세요.')
  }
  try {
    const loginInfo = {
      email: Email.value,
      password: password.value
    }
    const loginResult = await functions.postLogin(loginInfo)
    if (loginResult.data.message === 'fail') {
      alert('존재하지 않는 아이디거나 비밀번호가 일치하지 않습니다.')
    } else if (loginResult.data.message === 'success') {
      if (loginResult.data.member.valid === 0) {
        return alert('탈퇴한 회원입니다.')
      }
      // 유저 이미지가 있으면 유저이미지 불러오기
      let urlData = img
      if (loginResult.data.member.imageUrl) {
        urlData = loginResult.data.member.imageUrl
      }

      const member = {
        id: loginResult.data.member.id,
        role: loginResult.data.member.role,
        nickname: loginResult.data.member.nickname,
        oauthProvider: loginResult.data.member.oauthProvider,
        introduce: loginResult.data.member.introduce,
        imageUrl: urlData,
        valid: loginResult.data.member.valid
      }
      console.log(loginResult.data)
      const token = loginResult.data['jwt token'].accessToken
      const refreshToken = loginResult.data['jwt token'].refreshToken
      const data = {
        member: member,
        token: token,
        refreshToken: refreshToken
      }
      account.onLogin(data)
      router.push({ name: 'home' })
    }
  } catch (err) {
    alert('로그인에 실패하였습니다. 다시 시도해 주세요.')
    console.log(err)
  }
}

// 빈값이면 표시 될 값
const rules = [(value) => !!value || '필수 입력 값입니다.']

// 패스워드 인풋 로직
const visible = ref(false)

// 카카오로그인
async function kakaoLogin() {
  try {
    await functions.openkakaoLogin()
  } catch (error) {
    alert(error)
    console.error(error)
  }
}

// 네이버 로그인
async function naverLogin() {
  try {
    await functions.openNaverLogin()
  } catch (error) {
    alert(error)
    console.error(error)
  }
}
</script>

<style lang="scss" scoped>
.buttons {
  cursor: pointer;
}
.router-link-style {
  text-decoration: none;
  color: $primary-color;
}
.login-Btn {
  color: white;
  background-color: $primary-color;
  border-radius: 50px;
  width: 130px;
}
.social-Text {
  font-size: medium;
}
.bold {
  font-weight: bold;
}
.underline {
  text-decoration-line: underline;
  font-weight: 500;
}
</style>
