<template>
  <v-container>
    <v-card elevation="24" class="rounded-xl">
      <v-carousel cycle hide-delimiters show-arrows="hover" height="100%" width="100%">
        <ChallengeCard :challenges="challenges" @update="update" />
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
                      v-model="goalAmount"
                      label="목표 금액을 입력하세요"
                      required
                      variant="solo-filled"
                    ></v-text-field>
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
                      label="기간을 입력하세요"
                      variant="solo-filled"
                    ></v-text-field>
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
                      v-if="previewImageUrl"
                      :src="previewImageUrl"
                      alt="Uploaded Image"
                      style="max-width: 100%; max-height: 400px; margin: 20px auto; display: block"
                    />
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue-darken-1" variant="text" @click="dialog = false"> 닫기 </v-btn>
              <v-btn
                color="blue-darken-1"
                variant="text"
                @click=";(dialog = false), createChallenge()"
              >
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
import { ref, onMounted } from 'vue'
import functions from '@/api/challenge.js'
import { useCookies } from 'vue3-cookies'
import ChallengeCard from '@/components/Member/item/ChallengeCard.vue'

const { cookies } = useCookies()

// 챌린지 목록을 담을 배열
const challenges = ref([])

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

// 챌린지 생성 위한 변수
const title = ref('')
const content = ref('')
const period = ref('')
const goalAmount = ref('')

// 챌린지 생성 메서드
const createChallenge = () => {
  // data 객체 업데이트
  const data = ref({
    title: title.value,
    content: content.value,
    goalAmount: parseInt(goalAmount.value),
    id: memberId.value,
    period: period.value
  })

  // 챌린지 생성 API 호출
  const postChallenge = functions.postChallenge
  postChallenge(data.value).then((response) => {
    console.log(response) // 응답 확인
  })

  // 챌린지 생성 후 필요한 초기화 작업 등 수행 가능
  // 예: 입력 필드 초기화, 이미지 미리보기 초기화 등
}

// 챌린지 생성 다이얼로그(모달)를 위한 변수 초기값은 false
const dialog = ref(false)

// 파일 미리보기를 위한 URL
const previewImageUrl = ref('')

// 파일 업로드를 처리하는 메서드
const handleFileUpload = (event) => {
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

// 챌린지 업데이트
const update = () => {
  const getChallengeList = functions.getChallengeList
  getChallengeList(memberId.value).then((response) => {
    console.log(response.data.challenges)
    challenges.value = response.data.challenges
  })
}

//// 마운트 시에 챌린지 리스트 API 호출, memberId가 변경되면 다시 호출
onMounted(() => {
  const res = functions.getChallengeList(memberId.value)
  res.then((response) => {
    console.log(response.data.challenges)
    challenges.value = response.data.challenges
  })
})
</script>
<style>
.text-center {
  text-align: center;
}

.input-btn {
  align-items: center;
  margin: 20px auto;
  width: 40%;
}
</style>
