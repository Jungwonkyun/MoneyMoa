<template>
  <v-container>
    <v-row>
      <v-col class="text-center">
        <h1>비밀번호 찾기</h1>
        <v-row>
          <v-col>
            <v-text-field
              clearable
              placeholder="아이디(이메일)"
              variant="underlined"
              dense
              v-model="Email"
              :rules="Emailrules"
            >
            </v-text-field>
          </v-col>
          <v-col cols="2">
            <v-btn
              @click.prevent="onAthentic"
              variant="flat"
              class="text-none text-white"
              color="blue-darken-4"
              >인증하기</v-btn
            >
          </v-col>
        </v-row>
        <v-row>
          <v-col class="text-center">
            <router-link :to="{ name: 'loginform' }"
              ><v-btn variant="flat" class="text-none text-white" color="blue-darken-4"
                >로그인 페이지로 이동</v-btn
              ></router-link
            >
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
const Email = ref(null)
// 이메일 인증함수
function onAthentic() {
  const isValid = Emailrules.every((rule) => typeof rule(Email.value) !== 'string')
  if (isValid) {
    // 이메일 유효성 검사 통과시 로직 쓰기
    console.log('통과')
  } else {
    console.log('검증 오류가 발생했습니다.')
  }
}

// 이메일 유효성 검사
const Emailrules = [
  // (value) => !!value || '필수 입력 값입니다.',
  (value) => (value || '').length <= 20 || '최대 20자까지 입력 가능합니다.',
  (value) => {
    const pattern =
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    return pattern.test(value) || '이메일 형식이 아닙니다.'
  }
]
</script>
<style></style>
