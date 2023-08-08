<template>
  <v-card class="mx-auto" max-width="700" :elevation="10">
    <v-img
      class="align-end text-white"
      height="600"
      src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
      cover
    >
      <v-card-title>{{ challenge }} </v-card-title>
    </v-img>

    <v-row align="center" class="mt-1">
      <v-col cols="10" class="d-flex flex-row justify-start">
        <v-card-subtitle v-for="(hashtag, index) in hashtags" :key="index">
          <v-chip @click="searchFeed(해시태그1)">{{ hashtag }}</v-chip>
        </v-card-subtitle>
      </v-col>

      <v-col cols="2" class="flex">
        <v-icon @click="addFeedLike(feedId)" size="large" icon="mdi-heart" />
        <v-space></v-space>
        <v-icon v-if="condition" @click="deleteFeed(feedId)" size="large" icon="mdi-delete" />
      </v-col>
    </v-row>

    <v-card-text class="my-5">
      {{ content }}
    </v-card-text>

    <v-card-text>
      <div>댓글</div>
      <div>댓글</div>
      <div>댓글</div>
      <div>댓글</div>
      <div>댓글</div>
      <div>댓글</div>
    </v-card-text>
  </v-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import challengeFeed from '@/api/challengeFeed.js'
import { useCookies } from 'vue3-cookies'

// 쿠키 사용
const { cookies } = useCookies()

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

const route = useRoute()
const router = useRouter()
const feedId = ref(route.params.feedId)

const content = ref('')
const challenge = ref('')
const hashtags = ref([])

const condition = ref(false)

const deleteFeed = async () => {
  try {
    await challengeFeed.deleteFeed(feedId.value)
    // 삭제 성공 후에 다른 라우터로 이동
    router.push('/challenge/feedlist') // 이동하려는 라우터의 경로를 지정합니다.
  } catch (error) {
    console.error('피드 삭제 중 에러:', error)
  }
}
// 마운트 시에 피드 상세 조회 API 호출, memberId가 변경되면 다시 호출
onMounted(() => {
  const res = challengeFeed.fetchFeedDetail(feedId.value).then((response) => {
    const data = ref('')
    data.value = response.data.result.data
    content.value = data.value.content
    challenge.value = data.value.challenge
    // 정규식을 사용하여 '#'으로 시작하는 단어를 추출하여 리스트로 만듦
    hashtags.value = data.value.hashtag.match(/#[^\s#]+/g) || []
  })
})

// 마운트 시에 유저 피드 목록 조회하여 이 피드가 해당 유저 피드면 삭제 버튼 보여주기
onMounted(() => {
  const res = challengeFeed.getUserFeedList(memberId.value).then((response) => {
    const data = response.data.result.data
    // data 순회하면서 id만 추출
    const feedIdList = data.map((item) => item.id)
    console.log(feedIdList)
    const intFeedId = parseInt(feedId.value)
    // feedIdList에 현재 피드 id가 있으면 condition을 true로 변경
    if (feedIdList.includes(intFeedId)) {
      condition.value = true
    }
  })
})
</script>
<style>
.flex {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  padding-right: 20px;
}
</style>
