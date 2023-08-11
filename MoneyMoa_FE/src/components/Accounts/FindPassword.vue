<template>
  <v-container>
    <v-sheet
      max-width="800"
      class="mx-auto mt-8 rounded-lg px-10 py-5"
      min-height="400"
      width="100%"
      elevation="3"
    >
      <v-row>
        <v-col class="text-center">
          <h1 class="my-10">비밀번호 찾기</h1>
          <v-row>
            <v-col>
              <v-text-field
                clearable
                placeholder="아이디(이메일)"
                variant="underlined"
                dense
                v-model="Email"
                :rules="Emailrules"
                @keyup.enter="onAthentic"
              >
              </v-text-field>
            </v-col>
            <v-col cols="2">
              <v-btn @click.prevent="onAthentic" variant="flat" class="Athentic-Btn"
                >인증하기</v-btn
              >
            </v-col>
          </v-row>
          <v-row>
            <v-col class="text-center mt-10">
              <router-link :to="{ name: 'loginform' }"
                ><v-btn variant="flat" class="Athentic-Btn"
                  >로그인 페이지로 돌아가기</v-btn
                ></router-link
              >
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-sheet>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
import functions from '../../api/member.js'
const Email = ref(null)
// 이메일 인증함수
async function onAthentic() {
  const isValid = Emailrules.every((rule) => typeof rule(Email.value) !== 'string')
  if (isValid) {
    // 이메일 유효성 검사 통과시 로직 쓰기
    try {
      const res = await functions.postfindpassword(Email.value)
      console.log(res)

      if (res.message === 'success') {
        console.log(res.message)
        alert('임시번호를 발송하였습니다. 메일을 확인해 주세요.')
      } else {
        alert('존재하지 않는 메일입니다.')
      }
    } catch (err) {
      console.log(err)
    }
  } else {
    alert('이메일 형식이 아닙니다.')
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
<style scoped lang="scss">
.Athentic-Btn {
  color: white;
  background-color: $primary-color;
  border-radius: 20px;
}
</style>
