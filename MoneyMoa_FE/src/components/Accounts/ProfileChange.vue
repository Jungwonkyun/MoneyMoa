<template>
  <v-container>
    <v-sheet
      max-width="700"
      class="mx-auto mt-8 rounded-lg px-10 py-5"
      elevation="3"
      min-height="450"
      width="100%"
    >
      <v-row>
        <v-col>
          <h1 class="my-10">회원정보 변경</h1>
        </v-col>
      </v-row>
      <v-row v-if="cropping">
        <v-col>
          <cropper
            :stencil-component="CircleStencil"
            ref="cropperRef"
            :src="previewURL"
            :aspectRatio="1"
            :minWidth="100"
            :minHeight="100"
            @change="onCrop"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col class="text-right">
          <v-btn role="button" @click="cropImage" v-show="cropping" class="Btns">자르기</v-btn>
          <v-btn
            role="button"
            @click="cancelImg"
            v-show="cropping"
            class="ms-3 Btns"
            color="red lighten-5"
            >취소</v-btn
          >
        </v-col>
      </v-row>
      <h3 class="my-7" v-if="cropping">미리보기</h3>
      <v-row>
        <v-col>
          <v-avatar size="150" class="elevation-10">
            <v-img :src="userImg" width="100" v-if="!isChanged"></v-img>
            <v-img :src="croppedImage" v-if="isChanged" class="avatar-image"></v-img>
          </v-avatar>
          <v-file-input
            multiple
            @change="previewChangeImg"
            label="이미지를 선택해 주세요."
            accept="image/*"
            v-model="UploadImg"
            class="my-10"
          ></v-file-input>
        </v-col>
      </v-row>

      <v-row>
        <v-col>
          <h3 class="title-left">닉네임</h3>
          <v-text-field
            clearable
            variant="underlined"
            dense
            v-model="nickname"
            :rules="rules"
            :maxlength="15"
            counter
          ></v-text-field>
        </v-col>
      </v-row>

      <v-row>
        <v-col>
          <h3 class="title-left">자기소개</h3>
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
      <v-row v-if="isGeneral">
        <v-col>
          <h3 class="title-left">비밀번호</h3>
          <v-text-field
            clearable
            label="비밀번호"
            variant="underlined"
            dense
            v-model="changedPassword1"
            :type="visible ? 'text' : 'password'"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            @click:append-inner="visible = !visible"
            :rules="PassWordrules"
          ></v-text-field>

          <h3 class="title-left">비밀번호 확인</h3>
          <v-text-field
            @keyup.enter="onUpdate"
            clearable
            label="비밀번호 확인"
            variant="underlined"
            dense
            v-model="changedPassword2"
            :type="visible ? 'text' : 'password'"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            @click:append-inner="visible = !visible"
            :rules="PassCheckWordrules"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col class="text-left">
          <v-btn @click="quitService" class="Btns">회원탈퇴</v-btn>
        </v-col>
        <v-col class="text-right mb-5">
          <!-- 개발땐 홈으로 돌아가게 -->
          <v-btn class="Btns mr-5" color="red" @click="cancel">취소</v-btn>
          <v-btn class="Btns" @click="onUpdate">수정</v-btn>
        </v-col>
      </v-row>
    </v-sheet>
  </v-container>
</template>
<script setup>
// 유저 이미지로 바꿔야함
import { ref, computed, onMounted } from 'vue'
import { useRouter, onBeforeRouteLeave } from 'vue-router'
import { useAccountStore } from '@/stores/accountStore'
import { useCookies } from 'vue3-cookies'
import functions from '../../api/member.js'
import { Cropper, CircleStencil } from 'vue-advanced-cropper'
import 'vue-advanced-cropper/dist/style.css'
const { cookies } = useCookies()
console.log(cookies.get('member'))
const router = useRouter()
const isPass = ref(false)

onMounted(() => {
  getMember()
})
const userImg = ref(null)

const UploadImg = ref(null)
const introduce = ref(null)
const nickname = ref(null)
const isChanged = computed(() => {
  return UploadImg.value && UploadImg.value.length > 0
})
const changedPassword1 = ref(null)
const changedPassword2 = ref(null)
const visible = ref(false)

function cancel() {
  router.push({ name: 'home' }).then(() => {
    window.scrollTo(0, 0)
  })
}

// 현재 유저 소셜로그인 유무 변수
const isGeneral = computed(() => {
  return !!cookies.get('member') && cookies.get('member').oauthProvider === 'GENERAL'
})
// 닉 비어있으면 안됑
const rules = [(value) => !!value || '필수 입력 값입니다.']

// 비밀번호 유효성 검사
const PassWordrules = [
  (value) =>
    !value ||
    ((value || '').length >= 8 && (value || '').length <= 16) ||
    '8~16자까지 입력 가능합니다.',
  (value) =>
    /^[a-zA-Z0-9!@#$%^&*()+,-./:;<=>?@[₩\]_`{|}~]+$/.test(value) ||
    '사용 가능한 특수문자를 포함하여 입력해주세요.',
  (value) => (value || '').length <= 16 || '비밀번호는 16자를 초과할 수 없습니다.'
]
// 패스워드 확인
const PassCheckWordrules = [
  (value) => (value || '') === changedPassword1.value || '비밀번호가 일치하지 않습니다.'
]
// 유효성 검사후 수정 로직
async function onUpdate() {
  console.log(cropping.value)
  const isValid = PassWordrules.every((rule) => typeof rule(changedPassword1.value) !== 'string')
  if (!nickname.value) {
    alert('닉네임은 필수 사항입니다.')
    return
  } else if (!isValid) {
    return alert('비밀번호 형식이 올바르지 않습니다.')
  } else if (changedPassword1.value != changedPassword2.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  } else if (cropping.value) {
    return alert('이미지 선택을 완료해 주세요')
  }

  // 비밀번호 비어있지 않으면 비밀번호도 변경
  // 비번안바꿨으면 프로필로 보내기, 바꿨으면 로그인창
  // 지금은 그냥 홈으로

  try {
    const member = {
      nickname: nickname.value,
      introduce: introduce.value,
      password: `${changedPassword1.value}`
    }
    const jsonBlob = new Blob([JSON.stringify(member)], { type: 'application/json' })

    const data = new FormData()
    if (UploadImg.value && UploadImg.value.length > 0) {
      console.log(uploadedImage.value)
      data.append('file', uploadedImage.value)
    }
    data.append('MemberUpdateInfo', jsonBlob)
    const res = await functions.postUpdatedMember(data)
    isPass.value = true
    console.log(res.updatedMember)
    const updatedMember = {
      id: res.updatedMember.id,
      role: res.updatedMember.role,
      nickname: res.updatedMember.nickname,
      oauthProvider: res.updatedMember.oauthProvider,
      introduce: res.updatedMember.introduce,
      imageUrl: res.updatedMember.imageUrl,
      valid: res.updatedMember.valid
    }
    cookies.set('member', updatedMember)
  } catch (err) {
    console.log(err)
    return alert('수정에 실패했습니다.')
  }
  router.push({ name: 'home' }).then(() => {
    location.reload()
    window.scrollTo(0, 0)
  })
}

// 회원탈퇴 함수
async function quitService() {
  const answer = window.confirm(
    '회원 탈퇴 시 회원님의 모든 정보가 사라지며 재가입이 힘들 수 있습니다. 정말로 탈퇴하시겠습니까?'
  )
  if (answer) {
    try {
      const result = await functions.deletequitService()
      if (result.message === 'success') {
        const account = useAccountStore()
        account.onLogout()
        isPass.value = true
        alert('탈퇴가 완료되었습니다.')
        router.push({ name: 'home' }).then(() => {
          location.reload()
        })
      }
    } catch (err) {
      console.log(err)
    }
  }
}

// 페이지 떠날시 아래문구, 회원탈퇴나 수정 클릭시에도 문구만 바꾸면됨
// isPass같은 변수만들어서 회원탈퇴나 수정 클릭하면  true로 바꾸기, 새로고침 안해도 false로 바뀌면 냅두고 안바뀌면 next()전에 false로 다시 바꿔버리기
onBeforeRouteLeave((to, from, next) => {
  if (isPass.value) {
    console.log(isPass.value)
    return next()
  }
  if (window.confirm('사이트에서 나가시겠습니까? 변경사항이 저장되지 않을 수 있습니다.')) {
    const account = useAccountStore()
    account.setPwdChecked(false)
    console.log(account.pwdChecked)
    next()
  } else {
    next(false)
  }
})

// 회원정보 불러오기 함수
function getMember() {
  const member = cookies.get('member')
  console.log(member)
  introduce.value = member.introduce
  nickname.value = member.nickname
  userImg.value = member.imageUrl
}

const cropperRef = ref(null)

const cropping = ref(false)
const previewURL = ref(null)
const croppedImage = ref(null)
const uploadedImage = ref(null)
// 이건 유저가 사진 올리면 미리보기 파일이벤트임
function previewChangeImg() {
  const reader = new FileReader()
  reader.onload = (e) => {
    previewURL.value = e.target.result
    uploadedImage.value = e.target.result
  }
  cropping.value = true
  reader.readAsDataURL(UploadImg.value[0])
}

function onCrop() {
  croppedImage.value = cropperRef.value.getResult().canvas.toDataURL()
}
function cropImage() {
  cropping.value = false
  const canvas = cropperRef.value.getResult().canvas
  canvas.toBlob((blob) => {
    uploadedImage.value = blob
    console.log(uploadedImage.value)
  })
}
function cancelImg() {
  cropping.value = false
  UploadImg.value = []
}
</script>
<style scoped lang="scss">
textarea {
  resize: none;
}
.Btns {
  color: white;
  background-color: $primary-color;
  border-radius: 20px;
}
.title-left {
  text-align: left;
}
.avatar-image {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 150%;
  height: 150%;
  object-fit: cover;
}
</style>
