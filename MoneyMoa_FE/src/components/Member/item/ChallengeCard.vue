<template>
  <v-carousel-item class="mx-auto" v-for="(challenge, index) in challenges" :key="index">
    <v-card>
      <v-img :src="challenge.fileUrls[0]" class="align-end text-white" max-height="650">
        <v-card-title>
          <span class="shadow-text">{{ challenge.title }} </span></v-card-title
        >
      </v-img>

      <v-card-actions>
        <v-btn color="orange-lighten-2" variant="text" @click="show = !show"> 상세보기 </v-btn>

        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" width="auto">
          <template v-slot:activator="{ props }">
            <v-btn icon="mdi-pencil" v-bind="props"></v-btn>
          </template>
          <v-card>
            <v-card-title class="text-center mt-4">
              <span class="text-h5">챌린지 수정하기</span>
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
                      v-model="files"
                      multiple
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
                @click=";(dialog = false), updateChallenge(challenge.id)"
              >
                만들기
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-btn icon="mdi-delete" @click="deleteChallenge(challenge.id)"></v-btn>
      </v-card-actions>

      <v-expand-transition>
        <div v-show="show">
          <v-divider></v-divider>
          <v-card-text> 챌린지 내용: {{ challenge.content }} </v-card-text>
          <v-divider></v-divider>
          <v-card-text> 챌린지 종료 날짜: {{ challenge.period }} </v-card-text>
          <v-card-text> 목표 금액: {{ challenge.goalAmount }} </v-card-text>
          <v-card-text> 챌린지 시작 날짜: {{ challenge.startDate }} </v-card-text>
        </div>
      </v-expand-transition>
    </v-card>
  </v-carousel-item>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue'
import challengeApi from '@/api/challenge.js'
import { useCookies } from 'vue3-cookies'
import { useRoute, useRouter } from 'vue-router'

// 라우터 사용
const route = useRoute()
const router = useRouter()

// 쿠키 사용
const { cookies } = useCookies()

// 라우터로 타고 들어온 멤버 아이디
const memberId = ref(route.params.id)

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const loginMemberId = ref(memberInfo.value.id)

// 챌린지 목록을 담을 배열
const challenges = ref([])

// 챌린지 생성을 위한 변수
const dialog = ref(false)

// 챌린지ID 저장
const challengeId = ref('')

// 챌린지 상세보기 위한 변수
const show = ref(false)

// 챌린지 삭제 API 호출
const deleteChallenge = (challengeId) => {
  const deleteChallenge = challengeApi.deleteChallenge
  deleteChallenge(challengeId).then((response) => {
    console.log(response) // 응답 확인
    const getChallengeList = challengeApi.getChallengeList
    getChallengeList(loginMemberId.value).then((response) => {
      console.log(response.data.challenges)
      challenges.value = response.data.challenges
    })
  })
}

// 챌린지 생성 위한 변수
const title = ref('')
const content = ref('')
const period = ref('')
const goalAmount = ref('')
const files = ref([])

// 챌린지 수정하기 버튼 눌렀을 때
const updateChallenge = (challengeId) => {
  // 챌린지 데이터 생성
  const challenge = {
    content: content.value,
    goalAmount: parseInt(goalAmount.value),
    period: period.value,
    title: title.value
  }

  // 챌린지 수정 API 호출
  // 챌린지 생성 API 호출
  const updateChallenge = challengeApi.updateChallenge
  updateChallenge(challengeId, challenge).then((response) => {
    console.log(response) // 응답 확인
  })
}

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

watch(memberId, async (newMemberId) => {
  const response = await challengeApi.getChallengeList(newMemberId)
  console.log(response)
  challenges.value = response.data.challenges
})

//// 마운트 시에 챌린지 리스트 API 호출, memberId가 변경되면 다시 호출
onMounted(() => {
  const res = challengeApi.getChallengeList(memberId.value)
  res.then((response) => {
    console.log(response.data.challenges)
    challenges.value = response.data.challenges
  })
})
</script>
<style></style>
