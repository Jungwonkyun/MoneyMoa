<template>
  <v-container class="d-flex align-center flex-column">
    <!-- 로그인폼 -->
    <v-row>
      <v-col>
        <form class="LoginInput">
          <v-row class="d-flex align-center flex-column">
            <h1>이메일로 로그인</h1>
            <v-col cols="12">
              <v-text-field
                clearable
                label="아이디(이메일)"
                variant="underlined"
                dense
                v-model="Email"
                :rules="rules"
                @keyup.enter="onLogin"
              ></v-text-field>
              <v-text-field
                clearable
                label="비밀번호"
                variant="underlined"
                dense
                v-model="password"
                :type="visible ? 'text' : 'password'"
                :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append-inner="visible = !visible"
                :rules="rules"
                @keyup.enter="onLogin"
              ></v-text-field>
              <v-row>
                <v-col class="text-center">
                  <v-btn
                    @click.prevent="onLogin"
                    variant="flat"
                    class="text-none ms-4 text-white"
                    color="blue-darken-4"
                    >로그인</v-btn
                  >
                </v-col>
              </v-row>
            </v-col>
          </v-row>
        </form>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <router-link :to="{ name: 'signupform' }">회원가입</router-link>
        &nbsp;&nbsp;
        <router-link :to="{ name: 'findpassword' }">비밀번호 찾기</router-link>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <v-row>
          <v-col class="text-center">
            <span>소셜 회원으로 이용하기</span>
          </v-col>
        </v-row>
        <v-row>
          <!-- 소셜로그인 임시로 사진만 -->
          <v-col>
            <v-img :src="naverLogo" :width="200" @click="naverLogin" class="buttons"></v-img>
          </v-col>
          <v-col>
            <v-img :src="kakaoLogo" :width="200" @click="kakaoLogin" class="buttons"></v-img>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import naverLogo from '../../assets/img/네이버로그인.png'
import kakaoLogo from '../../assets/img/카카오로그인.png'
import functions from '@/api/member.js'
import { ref } from 'vue'
import { useAccountStore } from '@/stores/accountStore.js'
import { useRouter } from 'vue-router'

const account = useAccountStore()
const router = useRouter()
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
      const member = {
        id: loginResult.data.member.id,
        role: loginResult.data.member.role,
        nickname: loginResult.data.member.nickname
      }
      const token = loginResult.data['jwt token'].accessToken
      const data = {
        member: member,
        token: token
      }
      account.onLogin(data)
      alert('로그인 되었습니다.')
      router.push({ name: 'home' }).then(() => {
        location.reload()
      })
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

// 네이버로그인
function naverLogin() {
  window.location.replace(
    'https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=Cr4zB5lfM08ugwGyTXD4&state=zJ1F78TI5l&redirect_uri=http://i9d210.p.ssafy.io:9999/api/auth/naver'
  )
}

// 카카오로그인
function kakaoLogin() {
  window.location.replace(
    'https://kauth.kakao.com/oauth/authorize?client_id=6bca07d112514b4054347d4fd3bfaf53&redirect_uri=http://i9d210.p.ssafy.io:9999/api/auth/kakao&response_type=code'
  )
}
</script>

<style>
.LoginInput {
  width: 500px;
}
.buttons {
  cursor: pointer;
}
</style>
