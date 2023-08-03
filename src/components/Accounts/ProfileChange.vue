<template>
  <v-container>
    <v-row>
      <v-col>
        <h1>회원정보 변경</h1>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <v-img :src="img" width="100"></v-img>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <p>닉네임</p>
        <v-text-field
          clearable
          variant="underlined"
          dense
          v-model="NickName"
          :rules="rules"
          :maxlength="15"
          counter
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row>
      <v-col>
        <p>자기소개</p>
        <v-textarea
          no-resize
          variant="outlined"
          dense
          v-model="introduce"
          :maxlength="150"
          counter
        ></v-textarea>
      </v-col>
    </v-row>

    <v-row>
      <v-col> </v-col>
    </v-row>
    <v-row>
      <v-col>
        <p>비밀번호</p>
        <v-text-field
          clearable
          label="비밀번호"
          variant="underlined"
          dense
          v-model="changedPassword1"
          :type="visible ? 'text' : 'password'"
          :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="visible = !visible"
          :rules="rules"
        ></v-text-field>

        <p>비밀번호 확인</p>
        <v-text-field
          clearable
          label="비밀번호 확인"
          variant="underlined"
          dense
          v-model="changedPassword2"
          :type="visible ? 'text' : 'password'"
          :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="visible = !visible"
          :rules="rules"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn>회원탈퇴</v-btn>
      </v-col>
      <v-col>
        <!-- 개발땐 홈으로 돌아가게 -->
        <router-link :to="{ name: 'home' }"><v-btn>취소</v-btn></router-link>
        <v-btn @click="onUpdate">수정</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
// 유저 이미지로 바꿔야함
import { ref } from 'vue'
import img from '../../assets/img/default_image.png'
import { useRouter, onBeforeRouteLeave } from 'vue-router'
import { useAccountStore } from '@/stores/accountStore'
const router = useRouter()

// 스토어의 닉네임, 자기소개로 바꿔주기@@@
const NickName = ref('기존 닉네임')
const introduce = ref('기존 자기소개')
const rules = [(value) => !!value || '필수 입력 값입니다.']

const changedPassword1 = ref(null)
const changedPassword2 = ref(null)
const visible = ref(false)

// 유효성 검사후 수정 로직
function onUpdate() {
  const account = useAccountStore()
  if (!NickName.value) {
    alert('닉네임은 필수 사항입니다.')
    return
  } else if (changedPassword1.value != changedPassword2.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }
  // 비밀번호 비어있지 않으면 비밀번호도 변경 => 이건 백에서?
  // 비번안바꿨으면 프로필로 보내기, 바꿨으면 로그인창
  // 지금은 그냥 홈으로
  account.setPwdChecked(false)
  return router.push({ name: 'home' })
}

// 페이지 떠날시 아래문구, 회원탈퇴나 수정 클릭시에도 문구만 바꾸면됨
// isPass같은 변수만들어서 회원탈퇴나 수정 클릭하면  true로 바꾸기, 새로고침 안해도 false로 바뀌면 냅두고 안바뀌면 next()전에 false로 다시 바꿔버리기
onBeforeRouteLeave((to, from, next) => {
  console.log(from, to)
  const answer = window.confirm('사이트에서 나가시겠습니까? 변경사항이 저장되지 않을 수 있습니다.')
  if (answer) {
    const account = useAccountStore()
    account.setPwdChecked(false)
    console.log(account.pwdChecked)
    next()
  } else {
    next(false)
  }
})
</script>
<style>
textarea {
  resize: none;
}
</style>
