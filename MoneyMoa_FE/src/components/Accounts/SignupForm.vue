<template>
  <v-container>
    <v-sheet
      max-width="800"
      class="mx-auto mt-8 rounded-lg px-10 py-5"
      elevation="3"
      min-height="700"
      width="100%"
    >
      <h1 class="my-16">회원가입</h1>

      <!-- 가입폼 -->

      <h3 class="mt-15 title-left">아이디(이메일)</h3>
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
            @keyup.enter="onAthentic"
          >
          </v-text-field>
        </v-col>
        <!-- 버튼 차지cols 변경해야함 -->
        <v-col cols="3" class="text-center">
          <v-btn
            @click.prevent="onAthentic"
            class="Athentic-Btn"
            :disabled="showBtn"
            elevation="3"
            height="40"
            >인증번호 전송</v-btn
          >
        </v-col>
      </v-row>
      <v-row v-if="sent">
        <h3>인증번호를 입력해 주세요</h3>
        <v-col>
          <v-text-field
            clearable
            placeholder="인증번호"
            variant="underlined"
            dense
            v-model="userAccess"
            :rules="isBlank"
            @keyup.enter="checkAccess"
          >
          </v-text-field>
        </v-col>
        <!-- 버튼 차지cols 변경해야함 -->
        <v-col cols="2" class="text-center">
          <v-btn @click.prevent="checkAccess" variant="flat" class="Athentic-Btn">인증하기</v-btn>
        </v-col>
      </v-row>

      <!-- 비밀번호 -->
      <v-row>
        <v-col>
          <h3 class="title-left">비밀번호</h3>
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
          <h3 class="title-left">이름</h3>
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
          <h3 class="title-left">닉네임</h3>
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
          <h3 class="title-left">생년월일</h3>
          <v-text-field
            type="date"
            variant="underlined"
            :max="today"
            v-model="birthday"
          ></v-text-field>
        </v-col>
      </v-row>

      <!-- 성별 -->
      <v-row>
        <v-col>
          <h3 class="title-left">성별</h3>
          <v-radio-group inline v-model="gender">
            <v-col class="d-flex justify-space-around">
              <v-radio label="선택하지 않음" :value="null"></v-radio>
              <v-radio label="남" value="male"></v-radio>
              <v-radio label="여" value="female"></v-radio>
            </v-col>
          </v-radio-group>
        </v-col>
      </v-row>
      <!-- 가입버튼 -->
      <v-row class="d-flex justify-space-around">
        <v-btn @click.prevent="onSignUp" class="Athentic-Btn mb-10" height="40" width="100"
          >가입하기</v-btn
        >
      </v-row>
    </v-sheet>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import functions from '@/api/member.js'
import { useRouter } from 'vue-router'
const router = useRouter()
const Email = ref(null)
const password1 = ref(null)
const password2 = ref(null)
const Name = ref(null)
const NickName = ref(null)
const birthday = ref(null)
// 유저가 입력한 인증번호
const userAccess = ref(null)
// 서버에서 받은 인증키
const Access = ref(null)
// 이메일 인증여부 변수
const isAuthentic = ref(false)
// v-if용 (인증번호 보냈는지 안보냈는지)
const sent = ref(false)
// 성별 변수
const gender = ref(null)

// 인증메일 보내기 보여줄지말지 함수
const showBtn = ref(false)

// 이메일 인증함수
async function onAthentic() {
  const isValid = Emailrules.every((rule) => typeof rule(Email.value) !== 'string')
  if (showBtn.value) {
    return
  }
  if (isValid) {
    showBtn.value = true
    try {
      const authResult = await functions.postEmailauth(Email.value)
      console.log(typeof Email)
      console.log(authResult)
      // 이메일 유효성 검사 통과시 로직 쓰기
      if (authResult.message === 'success') {
        sent.value = true
        alert('인증번호를 발송했습니다.')
        Access.value = authResult.emailAuth
      } else {
        alert('인증번호 전송에 실패했습니다.')
      }
      showBtn.value = false
    } catch (error) {
      console.error('에러 발생:', error)
    }
  } else {
    alert('이메일 형식이 올바르지 않습니다.')
  }
}
// 이메일 인증번호 맞는지 검사
function checkAccess() {
  if (userAccess.value === Access.value) {
    alert('인증되었습니다.')
    isAuthentic.value = true
  } else {
    alert('인증번호가 일치하지 않습니다. 다시 시도해 주세요.')
  }
}

const isBlank = [(value) => !!value || '필수 입력 값입니다.']
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
async function onSignUp() {
  if (!Email.value) {
    return alert('이메일을 입력해 주세요.')
  } else if (!isAuthentic.value) {
    return alert('이메일 인증을 완료해 주세요.')
  } else if (!password1.value) {
    alert('비밀번호를 입력해 주세요.')
  } else if (password1.value != password2.value) {
    return alert('비밀번호가 일치하지 않습니다.')
  } else if (!Name.value) {
    return alert('이름을 입력해 주세요.')
  } else if (!NickName.value) {
    return alert('닉네임을 입력해 주세요.')
  } else if (!birthday.value) {
    return alert('생년월일을 입력해 주세요.')
  }
  // 생일, 성별은 내일추가하기
  const member = {
    birthday: birthday.value,
    email: Email.value,
    name: Name.value,
    nickname: NickName.value,
    password: password1.value,
    gender: gender.value,
    valid: 0
  }
  try {
    const Signup = await functions.postSignup(member)
    if (Signup.message === 'success') {
      console.log(Signup)
      alert('가입이 완료되었습니다. 로그인해 주세요')
      router.push({ name: 'loginform' })
    }
  } catch (err) {
    console.log(err)
  }
}
const formattedDate = new Date()
const year = formattedDate.getFullYear()
const month = String(formattedDate.getMonth() + 1).padStart(2, '0') // 0부터 시작하므로 1을 더하고, 2자리로 만들기 위해 padStart 사용
const day = String(formattedDate.getDate()).padStart(2, '0') // 2자리로 만들기 위해 padStart 사용
const today = `${year}-${month}-${day}`
</script>
<style scoped lang="scss">
/* 임시스타일 */
h3 {
  margin-top: 30px;
}
.Athentic-Btn {
  color: white;
  background-color: $primary-color;
  border-radius: 20px;
}
.title-left {
  text-align: left;
}
</style>
