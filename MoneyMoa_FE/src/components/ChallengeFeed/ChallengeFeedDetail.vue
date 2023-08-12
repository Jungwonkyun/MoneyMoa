<template>
  <v-card class="mx-auto" max-width="700" :elevation="10">
    <v-carousel hide-delimiter-background show-arrows="hover">
      <v-carousel-item v-for="(img, index) in imgs" :key="index">
        <v-img :src="img" height="100%" class="align-end text-white" cover>
          <v-card-title>{{ challenge }} </v-card-title>
        </v-img>
      </v-carousel-item>
    </v-carousel>

    <v-row align="center" class="mt-1">
      <v-col cols="9" class="d-flex flex-row justify-start">
        <v-card-subtitle v-for="(hashtag, index) in hashtags" :key="index">
          <v-chip @click.stop="searchFeed(해시태그1)">{{ hashtag }}</v-chip>
        </v-card-subtitle>
      </v-col>

      <v-col cols="3" class="flex">
        <v-btn
          @click.stop="addFeedLike(feedId)"
          size="large"
          icon="mdi-heart"
          variant="text"
        ></v-btn>
        <v-btn
          icon="mdi-delete"
          variant="text"
          @click.stop="deleteFeed(feedId)"
          v-if="condition"
          size="large"
        ></v-btn>
        <UpdateFeed v-if="condition" class="px-10" />
      </v-col>
    </v-row>
    <v-divider></v-divider>

    <v-card-text class="my-5">
      {{ content }}
    </v-card-text>
    <v-divider></v-divider>
    <v-card-text v-for="(com, index) in comments" :key="index">
      {{ com.nickname }}: {{ com.content }}
      <v-btn icon="mdi-delete" variant="text"></v-btn>
    </v-card-text>
    <v-text-field
      class="px-5"
      v-model="comment"
      label="댓글 달기..."
      variant="underlined"
      append-inner-icon="mdi-comment-check-outline"
      @click:append-inner="addComment"
      @keyup.enter="addComment"
    ></v-text-field>
  </v-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import challengeFeed from '@/api/challengeFeed.js'
import { useCookies } from 'vue3-cookies'
import UpdateFeed from './item/UpdateFeed.vue'

// 쿠키 사용
const { cookies } = useCookies()

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

const route = useRoute()
const router = useRouter()
const feedId = ref(route.params.feedId)

// 화면에 표시할 데이터
const content = ref('')
const challenge = ref('')
const hashtags = ref([])
const comments = ref([])
const imgs = ref([])

// 댓글 작성 위한 모델
const comment = ref('')

// 피드 삭제 버튼 보여주기 위한 조건
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
    content.value = response.data.feed.content
    challenge.value = response.data.feed.challengeId
    // 정규식을 사용하여 '#'으로 시작하는 단어를 추출하여 리스트로 만듦
    hashtags.value = response.data.feed.hashtag.match(/#[^\s#]+/g) || []
    comments.value = response.data.comments
    imgs.value = response.data.feed.fileUrls
    console.log('comments', comments.value)
  })
})

// 마운트 시에 유저 피드 목록 조회하여 이 피드가 해당 유저 피드면 삭제 버튼 보여주기
onMounted(() => {
  const res = challengeFeed.getUserFeedList(memberId.value).then((response) => {
    const data = response.data.feedList
    // data 순회하면서 id만 추출
    const feedIdList = data.map((item) => item.id)
    const intFeedId = parseInt(feedId.value)
    // feedIdList에 현재 피드 id가 있으면 condition을 true로 변경
    if (feedIdList.includes(intFeedId)) {
      condition.value = true
    }
  })
})

// 댓글 생성 및 댓글 목록 조회
const addComment = async () => {
  try {
    const commentData = {
      content: comment.value
    }

    // 댓글 생성
    await challengeFeed.postComment(feedId.value, commentData)

    // 댓글 목록 업데이트
    const response = await challengeFeed.fetchFeedDetail(feedId.value)
    comments.value = response.data.comments

    // 입력 필드 초기화
    comment.value = ''
  } catch (error) {
    console.error('에러 발생:', error)
  }
}
</script>
<style>
.flex {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  margin: auto;
}
</style>
