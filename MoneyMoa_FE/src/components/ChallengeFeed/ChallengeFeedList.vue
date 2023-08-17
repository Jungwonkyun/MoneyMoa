<template>
  <v-card
    v-for="(feed, index) in feeds"
    :key="index"
    class="mx-auto my-12"
    max-width="700"
    :elevation="3"
  >
    <v-carousel hide-delimiters show-arrows="hover">
      <v-carousel-item
        v-for="(file, i) in feed.fileUrls"
        :key="i"
        :src="file"
        cover
      ></v-carousel-item>
    </v-carousel>
    <v-card-item>
      <v-card-title class="font-weight-bold my-2">{{ feed.challengeTitle }}</v-card-title>

      <v-card-subtitle>
        <span class="me-1">{{ feed.hashtag }}</span>

        <v-icon color="error" icon="mdi-fire-circle" size="small"></v-icon>
      </v-card-subtitle>
    </v-card-item>

    <v-card-text>
      <div class="my-4 text-subtitle-1">{{ feed.content }}</div>
    </v-card-text>

    <v-divider class="mx-4 mb-1"></v-divider>

    <router-link :to="'/challenge/feed/' + feed.id">
      <v-btn variant="text">댓글 확인하기</v-btn>
    </router-link>
  </v-card>
  <InfiniteLoading @infinite="load" />
</template>

<script setup>
import { ref, watchEffect, onMounted } from 'vue'
import InfiniteLoading from 'v3-infinite-loading'
import 'v3-infinite-loading/lib/style.css'
import { useChallengeFeedStore } from '../../stores/challengeFeedStore'
import { storeToRefs } from 'pinia'
import challengeFeedApi from '@/api/challengeFeed.js'
import memberApi from '@/api/member.js'
import default_image from '../../assets/img/default_image.png'
import { useRouter } from 'vue-router'

// useRouter 사용
const router = useRouter()

// 기본이미지
const defaultImage = default_image

// getsombodyinfoapi를 통해 프로필 이미지 가져오기
const getProfileImg = memberApi.getSombodyInfoApi

// 스토어 사용
const challengeFeedStore = useChallengeFeedStore()
const { searchWord } = storeToRefs(challengeFeedStore)

let feeds = ref([])

const load = async ($state) => {
  console.log('loading...')

  try {
    // 만약 searchWord가 존재한다면 검색어를 통해 피드를 불러온다.
    if (searchWord.value) {
      await challengeFeedApi.searchFeed(searchWord.value).then((response) => {
        const feedList = response.data
        feedList.forEach((feed) => {
          getProfileImg(feed.memberId).then((response) => {
            console.log(response.data.sombody.imageUrl)
            feed[imgUrl] = response.data.sombody.imageUrl
          })
        })
        console.log(feedList)
        const data = feedList.reverse()
        // 만약 데이터가 2개 이하라면
        // $state.complete()를 호출하여 더 이상 데이터를 로딩하지 않고 완료 상태로 변경
        if (data.length < 2) $state.complete()
        else {
          // feeds.value에 모든 data 배열의 모든 요소를 병합
          feeds.value.push(...data)
          // $state.loaded()를 호출하여 더 많은 데이터를 요청할 수 있도록 로딩 상태를 유지
          $state.loaded()
        }
      })
    } else {
      // searchWord가 존재하지 않는다면 모든 피드를 불러온다.
      challengeFeedApi.fetchAllFeedList().then((response) => {
        const promises = response.data.feedList.map((feed) =>
          getProfileImg(feed.memberId).then((response) => ({
            ...feed,
            imgUrl: response.data.sombody.imageUrl ? response.data.sombody.imageUrl : defaultImage
          }))
        )
        Promise.all(promises).then((res) => {
          console.log(res)
          const data = res.reverse()
          // 만약 데이터가 2개 이하라면
          // $state.complete()를 호출하여 더 이상 데이터를 로딩하지 않고 완료 상태로 변경
          if (data.length < 2) $state.complete()
          else {
            // feeds.value에 모든 data 배열의 모든 요소를 병합
            feeds.value.push(...data)
            // $state.loaded()를 호출하여 더 많은 데이터를 요청할 수 있도록 로딩 상태를 유지
            $state.loaded()
          }
        })
      })
    }
  } catch (error) {
    $state.error()
  }
}

// 클릭하면 디테일 페이지로 이동
const goToDetail = (feedId) => {
  router.push(`/challenge/feedDetail/' + ${feedId}`)
}

// `searchWord` 변경 시 `load` 함수 호출
watchEffect(() => {
  console.log('searchWord', searchWord.value)
  feeds.value = []
  // `load` 함수 호출
  load({
    complete() {},
    loaded() {},
    error() {}
  })
})

onMounted(() => {
  searchWord.value = ''
})
</script>

<style>
.result {
  gap: 5px;
  font-weight: 300;
  width: 100%;
  height: 600px;
  text-align: center;
  background: #f4f4f4;
}

.card-container {
  max-height: 700px; /* 화면에 보여질 최대 높이 설정 */
  overflow-y: auto; /* 스크롤을 추가해야하는 영역을 정의 */
  width: 1000px;
  margin: 0 auto;
}

.profile-img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}
</style>
