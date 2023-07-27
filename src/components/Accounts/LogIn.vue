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
                v-model="email"
                :rules="rules"
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
        <router-link :to="{ name: 'signup' }">회원가입</router-link>
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
            <v-img :src="naverLogo" :width="200"></v-img>
          </v-col>
          <v-col>
            <v-img :src="kakaoLogo" :width="200"></v-img>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import naverLogo from '@/assets/img/네이버로그인.png'
import kakaoLogo from '@/assets/img/카카오로그인.png'
import { ref } from 'vue'
const email = ref(null)
const password = ref(null)

// 로그인 로직, 빈값이면 경고창
// 로그인 구현시 - 로그인 실패시 비밀번호 초기화
function onLogin(e) {
  if (email.value && password.value) {
    console.log(e)
  } else if (!email.value) {
    alert('이메일을 입력해 주세요.')
  } else if (!password.value) {
    alert('비밀번호를 입력해 주세요.')
    console.log(email.value, password.value)
  }
}

// 이메일 양식 아니면 또는 빈값이면 표시 될 값
const rules = [(value) => !!value || '필수 입력 값입니다.']

// 패스워드 인풋 로직
const visible = ref(false)
</script>

<style>
.LoginInput {
  width: 500px;
}
</style>
