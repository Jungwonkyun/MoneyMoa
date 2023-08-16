<template>
  <v-row v-if="condition" class="justify-end">
    <v-row justify="center">
      <v-dialog v-model="dialog" persistent width="auto">
        <template v-slot:activator="{ props }">
          <v-btn icon="mdi-pencil" variant="text" v-bind="props"> </v-btn>
        </template>
        <v-card>
          <v-card-title class="text-center mt-4">
            <span class="text-h5">피드 작성하기</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-textarea
                    clearable
                    label="내용을 입력하세요"
                    variant="solo-filled"
                    v-model="content"
                  ></v-textarea>
                </v-col>
                <v-col cols="12">
                  <v-select
                    :items="challengeList"
                    label="챌린지를 선택하세요"
                    required
                    variant="solo-filled"
                    v-model="challenge"
                  ></v-select>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    label="해시태그를 추가하세요"
                    required
                    variant="solo-filled"
                    v-model="hashtag"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    label="이번에 저축하는 금액을 입력하세요"
                    variant="solo-filled"
                    v-model="depositAmount"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-file-input label="사진을 추가하세요" multiple v-model="files"></v-file-input>
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
              @click=";(dialog = false), submitFeedData()"
            >
              만들기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </v-row>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import functions from '@/api/challenge.js'
import challengeFeedApi from '@/api/challengeFeed.js'
import { useCookies } from 'vue3-cookies'

// 쿠키 사용
const { cookies } = useCookies()

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

// 피드 생성하기 버튼을 눌렀을 때 나타나는 다이얼로그
const dialog = ref(false)

// 피드 생성할 때 보여줄 챌린지 리스트
const challengeList = ref([])

// condition정의하여 false로 초기화
const condition = ref(false)

// 피드 데이터 만들기
const challenge = ref('')
const content = ref('')
const depositAmount = ref('')
const hashtag = ref('')
const challengeset = ref([])
const challengeId = ref('')
const files = ref([])

// 피드 생성하기 버튼 눌렀을 때
const submitFeedData = () => {
  for (const set of challengeset.value) {
    if (set.title === challenge.value) {
      challengeId.value = set.id
    }
  }

  // 피드 데이터 생성
  const feed = {
    content: content.value,
    depositAmount: parseInt(depositAmount.value),
    hashtag: hashtag.value
  }

  // 폼 데이터 생성
  const formData = new FormData()
  for (const file of files.value) {
    formData.append('files', file)
  }
  formData.append('feed', JSON.stringify(feed))
  // 폼 데이터 확인
  for (const pair of formData.entries()) {
    console.log(pair[0], pair[1], typeof pair[1])
  }

  // 피드 생성 API 호출
  const createFeed = challengeFeedApi.createFeed
  createFeed(formData, challengeId.value).then((response) => {
    console.log(response)
  })
}

// 마운트되면 챌린지 목록 불러옴
// 만약 챌린지 없다면 피드 생성 버튼 안보이게 처리
onMounted(() => {
  const getChallengeList = functions.getChallengeList
  getChallengeList(memberId.value).then((response) => {
    for (const challengeKey in response.data.challenges) {
      const challenge = response.data.challenges[challengeKey]
      const id = challenge.id
      const title = challenge.title
      challengeList.value.push(title)
      challengeset.value.push({ id, title })
    }
    challengeList.value = response.data.challenges
    // 챌린지 리스트 길이가 0 보다 크면 버튼 보이게 처리
    if (challengeList.value.length > 0) {
      condition.value = true
    }
  })
})
</script>
<style></style>
