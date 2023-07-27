<template>
  <v-container>
    <h1>회원가입</h1>

    <!-- 가입폼 -->

    <form>
      <h3>아이디(이메일)</h3>
      <!-- 이메일 입력, 버튼 정렬 -->
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
        <!-- 버튼 차지cols 변경해야함 -->
        <v-col cols="2" class="text-center">
          <v-btn
            @click.prevent="onAthentic"
            variant="flat"
            class="text-none text-white"
            color="blue-darken-4"
            >인증하기</v-btn
          >
        </v-col>
      </v-row>

      <!-- 비밀번호 -->
      <v-row>
        <v-col>
          <h3>비밀번호</h3>
          <v-row>
            <v-col>
              <v-text-field
                clearable
                placeholder="비밀번호"
                variant="underlined"
                dense
                v-model="password1"
                :type="visible1 ? 'text' : 'password'"
                :append-inner-icon="visible1 ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append-inner="visible1 = !visible1"
                :rules="PassWordrules"
                :maxlength="20"
                :counter="20"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-text-field
                clearable
                placeholder="비밀번호 확인"
                variant="underlined"
                dense
                v-model="password2"
                :type="visible2 ? 'text' : 'password'"
                :append-inner-icon="visible2 ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append-inner="visible2 = !visible2"
                :rules="PassCheckWordrules"
                :maxlength="20"
                :counter="20"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-col>
      </v-row>

      <!-- 이름, 닉네임 -->
      <v-row>
        <!-- 이름칸 -->
        <v-col>
          <h3>이름</h3>
          <v-text-field
            clearable
            placeholder="이름"
            variant="underlined"
            dense
            v-model="Name"
            :rules="CheckName"
            :maxlength="10"
            counter
          ></v-text-field>
        </v-col>
        <!-- 닉네임칸 -->
        <v-col>
          <h3>닉네임</h3>
          <v-text-field
            clearable
            placeholder="최대 15자 까지만 입력 가능합니다."
            variant="underlined"
            dense
            v-model="NickName"
            :rules="CheckNickName"
            :maxlength="15"
            counter
          ></v-text-field>
        </v-col>
      </v-row>

      <!-- 생년월일 -->
      <v-row>
        <v-col>
          <h3>생년월일</h3>
          <v-text-field type="date" variant="underlined"></v-text-field>
        </v-col>
      </v-row>

      <!-- 성별 -->
      <v-row>
        <v-col>
          <h3>성별</h3>
          <v-radio-group inline>
            <v-col class="d-flex justify-space-around">
              <v-radio label="선택하지 않음" value="null" selected></v-radio>
              <v-radio label="남" value="male"></v-radio>
              <v-radio label="여" value="female"></v-radio>
            </v-col>
          </v-radio-group>
        </v-col>
      </v-row>
      <!-- 가입버튼 -->
      <v-row class="d-flex justify-space-around">
        <v-btn
          @click.prevent="onSignUp"
          variant="flat"
          class="text-none text-white"
          color="blue-darken-4"
          >가입하기</v-btn
        >
      </v-row>
    </form>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'

const Email = ref(null)
const password1 = ref(null)
const password2 = ref(null)
const Name = ref(null)
const NickName = ref(null)

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
  (value) => !!value || '필수 입력 값입니다.',
  (value) => (value || '').length <= 20 || '최대 20자까지 입력 가능합니다.',
  (value) => {
    const pattern =
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    return pattern.test(value) || '이메일 형식이 아닙니다.'
  }
]

// 패스워드 표시 변수
const visible1 = ref(false)
const visible2 = ref(false)

// 비밀번호 유효성 검사
const PassWordrules = [
  (value) => !!value || '필수 입력 값입니다.',
  (value) =>
    ((value || '').length >= 8 && (value || '').length <= 16) || '8~16자까지 입력 가능합니다.',
  (value) =>
    /^[a-zA-Z0-9!@#$%^&*()+,-./:;<=>?@[₩\]_`{|}~]+$/.test(value) ||
    '사용 가능한 특수문자를 포함하여 입력해주세요.',
  (value) => (value || '').length <= 16 || '비밀번호는 16자를 초과할 수 없습니다.'
]
// 패스워드 확인
const PassCheckWordrules = [
  (value) => !!value || '필수 입력 값입니다.',
  (value) => (value || '') === password1.value || '비밀번호가 일치하지 않습니다.'
]

// 이름 유효성 검사
const CheckName = [
  (value) => !!value || '이름을 입력해 주세요.',
  (value) =>
    !(/^[ㄱ-ㅎ]+$/.test(value) || /^[ㅏ-ㅣ]+$/.test(value)) || '입력 형식이 올바르지 않습니다.',
  (value) => /^[\uac00-\ud7af]{1,10}$/.test(value) || '한글만 입력가능합니다.'
]
// 닉네임 유효성 검사
const CheckNickName = [
  (value) => !!value || '닉네임을 입력해 주세요.',
  (value) => /^[a-zA-Z0-9ㄱ-ㅎ가-힣]*$/.test(value) || '한글, 영어, 숫자만 입력 가능합니다.',

  // 초성 닉네임도 가능하게 할거면 밑에거 지우면됨
  (value) =>
    !(/^[ㄱ-ㅎ]+$/.test(value) || /^[ㅏ-ㅣ]+$/.test(value)) || '입력 형식이 올바르지 않습니다.'
]

// 가입하기 버튼
function onSignUp(e) {
  console.log(e)
}
</script>
<style>
/* 임시스타일 */
h3 {
  margin-top: 30px;
}
</style>
