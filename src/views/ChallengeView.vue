<template>
  <v-container>
    <v-row>
      <v-col cols="11" class="ma-auto">
        <SearchBar v-if="shouldShowSearchBar" @custom-event="handleEvent" />
      </v-col>
      <v-col v-if="condition" cols="1" class="ma-auto">
        <v-row class="justify-end">
          <v-row justify="center">
            <v-dialog v-model="dialog" persistent width="auto">
              <template v-slot:activator="{ props }">
                <v-btn color="primary" v-bind="props"> 피드 생성하기 </v-btn>
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
                        ></v-textarea>
                      </v-col>
                      <v-col cols="12">
                        <v-select
                          :items="challengeList"
                          label="챌린지를 선택하세요"
                          required
                          variant="solo-filled"
                        ></v-select>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          label="해시태그를 추가하세요"
                          required
                          variant="solo-filled"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          label="기간을 입력하세요"
                          variant="solo-filled"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue-darken-1" variant="text" @click="dialog = false"> 닫기 </v-btn>
                  <v-btn color="blue-darken-1" variant="text" @click="dialog = false">
                    만들기
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-row>
        </v-row>
      </v-col>
    </v-row>
    <router-view :search-word="searchWord"></router-view>
  </v-container>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import SearchBar from '@/components/Challenge/item/SearchBar.vue'
import functions from '@/api/challenge.js'
import { useCookies } from 'vue3-cookies'

// 쿠키 사용
const { cookies } = useCookies()

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

// 피드 생성하기 버튼을 눌렀을 때 나타나는 다이얼로그
const dialog = ref(false)

// 피드 생성할 때 보여줄 챌린지 리스트
const challengeList = ref('')

const searchWord = ref(null)

const handleEvent = (word) => {
  console.log(typeof word)
  // console.log(word)
  searchWord.value = word
}

// 특정 라우터 뷰에서만 SearchBar를 보여줄지 여부를 계산하는 computed 속성
const shouldShowSearchBar = computed(() => {
  // window.location.pathname가 '/challenge/feed/post'인 경우에는 false를 반환
  return window.location.pathname !== '/challenge/feed/post'
})

// v-if 조건문에 사용할 변수
const condition = ref(false)

// 마운트되면 챌린지 목록 불러옴
// 만약 챌린지 없다면 피드 생성 버튼 안보이게 처리
onMounted(() => {
  const getChallengeList = functions.getChallengeList
  getChallengeList(memberId.value).then((response) => {
    console.log(response.data.result.data) // 응답 확인
    challengeList.value = response.data.result.data
    if (response.data.result.data.length > 0) {
      condition.value = true
    }
  })
})
</script>
<style></style>
