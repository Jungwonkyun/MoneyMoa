<template>
  <v-container>
    <v-row>
      <v-col>
        <h1>비밀번호 확인</h1>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <h3>사용중인 비밀번호를 입력해 주세요.</h3>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
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
          @keyup.enter="onCheckPwd"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn @click="onCheckPwd">확인</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <!-- 로그인창 아니라 유저프로필로 가야함 임시임 -->
        <router-link :to="{ name: 'loginform' }"><v-btn>돌아가기</v-btn></router-link>
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
import { useAccountStore } from '@/stores/accountStore'
import { useRouter } from 'vue-router'
import functions from '../../api/member'
import { useCookies } from 'vue3-cookies'

const account = useAccountStore()
const { cookies } = useCookies()
// 라우터 푸쉬를 위한 라우터 인풋
const router = useRouter()
// 패스워드 인증 전, 후 상태관리 스토어

// 패스워드 로직
const password = ref(null)
const rules = [(value) => !!value || '비밀번호를 입력해 주세요.']
const visible = ref(false)

async function onCheckPwd() {
  try {
    const myinfo = await functions.getMyInfoApi(cookies.get('accessToken'))
    if (myinfo) {
      // 여기다 비밀번호 맞는지 아닌지 로직 짜기
      if (myinfo.data.password === password.value) {
        account.setPwdChecked(true)
        router.push({ name: 'profilechange' })
      } else {
        alert('비밀번호가 일치하지 않습니다.')
      }
    } else {
      alert('세션이 만료되었습니다. 다시 로그인 해주세요.')
      router.push({ name: 'loginform' }).then(() => {
        location.reload()
      })
    }
  } catch (err) {
    console.log(err)
  }
}
</script>
<style></style>
