<template>
  <v-container class="animate__animated animate__fadeInDown">
    <v-card elevation="5" class="rounded-xl mx-auto" max-width="650">
      <v-carousel cycle hide-delimiters show-arrows="hover" height="100%" width="100%">
        <ChallengeCard />
      </v-carousel>
    </v-card>
    <!-- 챌린지 생성 다이얼로그 -->
    <v-row class="justify-end pa-3 mt-5">
      <v-row justify="center">
        <v-dialog v-model="dialog" width="auto">
          <template v-slot:activator="{ props }">
            <v-btn color="primary" v-bind="props"> 챌린지 생성하기 </v-btn>
          </template>
          <v-card>
            <v-card-title class="text-center mt-4">
              <span class="text-h5">챌린지 생성하기</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12">
                    <v-text-field
                      v-model="title"
                      label="챌린지 이름을 설정하세요"
                      required
                      variant="solo-filled"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-text-field
                      class="amount-input"
                      v-model.number="goalAmount"
                      hide-details
                      single-line
                      label="금액"
                      min="0"
                      @keypress="onKeyPress"
                      @input="onInput"
                    />
                  </v-col>
                  <v-col cols="12">
                    <v-textarea
                      v-model="content"
                      clearable
                      label="내용을 입력하세요"
                      variant="solo-filled"
                    ></v-textarea>
                  </v-col>
                  <v-col cols="12">
                    <v-text-field
                      v-model="period"
                      label="개월 단위로 기간을 입력하세요. ex) 3, 6, 12"
                      variant="solo-filled"
                      @keypress="onKeyPress"
                      @input="onInput2"
                    ></v-text-field>
                  </v-col>
                  <!-- 사진 자르기 -권종률 -->
                  <v-col cols="12">
                    <ImageCropper
                      :previewURL="previewImageUrl"
                      v-if="isCrop"
                      @cancel="cancelCrop"
                      @cropimage="cropimage"
                    />
                  </v-col>
                  <v-col cols="12">
                    <!-- 파일 업로드를 위한 input 요소 추가 -->
                    <v-file-input
                      prepend-icon="mdi-camera"
                      label="이미지를 선택하세요"
                      variant="solo-filled"
                      @change="handleFileUpload"
                      class="input-btn"
                    ></v-file-input>
                  </v-col>
                  <v-col cols="12">
                    <!-- 사진 미리보기를 위한 img 요소 추가 -->
                    <img
                      v-if="previewResult"
                      :src="previewResult"
                      alt="Uploaded Image"
                      style="max-width: 100%; max-height: 400px; margin: 20px auto; display: block"
                    />
                  </v-col>
                </v-row>
              </v-container>
              <v-alert type="error" text="입력하지 않은 항목이 있습니다." v-if="isblank"></v-alert>
              <v-alert type="error" text="사진 자르기를 완료해 주세요." v-if="iscropping"></v-alert>
              <v-alert type="error" text="사진이 없습니다." v-if="isnonePic"></v-alert>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn class="BtnsRed" variant="text" @click="closeCreate" elevation="3">
                닫기
              </v-btn>
              <v-btn class="Btns" variant="text" elevation="3" @click="createChallenge()">
                만들기
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import challengeApi from '@/api/challenge.js'
import { useCookies } from 'vue3-cookies'
import ChallengeCard from '@/components/Member/item/ChallengeCard.vue'
import 'animate.css'
import ImageCropper from './item/ImageCropper.vue'
const { cookies } = useCookies()

const onKeyPress = (event) => {
  if (event.key < '0' || event.key > '9') {
    event.preventDefault()
  }
}
const onInput = (event) => {
  const value = event.target.value.replace(/[^0-9]/g, '')
  goalAmount.value = isNaN(parseInt(value)) ? '' : parseInt(value)
}
const onInput2 = (event) => {
  const value = event.target.value.replace(/[^0-9]/g, '')
  period.value = isNaN(parseInt(value)) ? '' : parseInt(value)
}

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

// 챌린지 생성 위한 변수
const title = ref('')
const content = ref('')
const period = ref('')
const goalAmount = ref('')
const files = ref([])
const isnonePic = ref(false)
const isblank = ref(false)
const iscropping = ref(false)

const closeCreate = () => {
  dialog.value = false
  content.value = ''
  period.value = ''
  title.value = ''
  goalAmount.value = ''
  files.value = []
  isnonePic.value = false
  isblank.value = false
  iscropping.value = false
}

// 챌린지 생성하기 버튼 눌렀을 때
const createChallenge = async () => {
  if (!title.value || !content.value || !period.value || !goalAmount.value) {
    isblank.value = true
    setTimeout(() => {
      isblank.value = false
    }, 4000)
    return
  } else if (isCrop.value) {
    iscropping.value = true
    setTimeout(() => {
      iscropping.value = false
    }, 4000)
    return
  } else if (files.value.length <= 0) {
    isnonePic.value = true
    setTimeout(() => {
      isnonePic.value = false
    }, 4000)
    return
  }
  // 챌린지 데이터 생성
  const challenge = {
    content: content.value,
    goalAmount: parseInt(goalAmount.value),
    period: period.value,
    title: title.value
  }
  // 폼 데이터 생성
  const formData = new FormData()
  for (const file of files.value) {
    formData.append('files', file)
  }
  formData.append('challenge', JSON.stringify(challenge))

  // 폼 데이터 확인
  for (const pair of formData.entries()) {
    console.log(pair[0], pair[1], typeof pair[1])
  }

  // 챌린지 생성 API 호출
  const postChallenge = challengeApi.postChallenge
  await postChallenge(formData).then((response) => {
    console.log(response) // 응답 확인
  })

  // 챌린지 생성 후 필요한 초기화 작업 등 수행 가능
  // 예: 입력 필드 초기화, 이미지 미리보기 초기화 등
  dialog.value = false

  // 새로고침하기
  location.reload()
}

// 챌린지 생성 다이얼로그(모달)를 위한 변수 초기값은 false
const dialog = ref(false)

// 파일 미리보기를 위한 URL
const previewImageUrl = ref('')
const isCrop = ref(false)
const previewResult = ref(null)
// 파일 업로드를 처리하는 메서드

function cancelCrop() {
  isCrop.value = false
}
function cropimage(e) {
  console.log(e)
  const reader = new FileReader()
  reader.onload = (event) => {
    previewResult.value = event.target.result
  }
  reader.readAsDataURL(e)
  files.value.push(e)
  isCrop.value = false
}
const handleFileUpload = (event) => {
  isCrop.value = true
  //event.target.files[0]를 통해 업로드한 파일 정보를 가져옵니다.
  const file = event.target.files[0]

  //FileReader 객체를 생성하여 파일을 읽습니다.
  const reader = new FileReader()

  //파일이 읽혔을 때 발생하는 onload 이벤트를 이용하여,
  //파일의 미리보기를 위한 base64 형태의 URL을 생성하고, previewImageUrl에 저장합니다.
  reader.onload = () => {
    previewImageUrl.value = reader.result
  }

  if (file) {
    // 파일을 읽어오고, onload 이벤트가 발생하여 미리보기를 업데이트합니다.
    reader.readAsDataURL(file)
  }
}
</script>
<style scoped lang="scss">
.text-center {
  text-align: center;
}

.input-btn {
  align-items: center;
  margin: 20px auto;
  width: 40%;
}
.Btns {
  color: white;
  background-color: $primary-color;
  border-radius: 20px;
}
.BtnsRed {
  color: white;
  background-color: #f44336;
  border-radius: 20px;
}
</style>
