<template>
  <v-card class="mx-auto" max-width="700" :elevation="10">
    <v-carousel hide-delimiter-background show-arrows="hover">
      <v-carousel-item v-for="(fileUrl, index) in fileUrls" :key="index">
        <v-img :src="fileUrl" height="100%" class="align-end text-white" cover>
          <v-card-title
            ><span class="shadow-text">{{ challengeTitle }}</span>
          </v-card-title>
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
        {{ feedLikeCount }}
        <v-btn @click.stop="addFeedLike" size="large" icon="mdi-heart" variant="text"></v-btn>
        <v-btn
          icon="mdi-delete"
          variant="text"
          @click.stop="deleteFeed(feedId)"
          v-if="deleteCondition"
          size="large"
        ></v-btn>
        <UpdateFeed v-if="deleteCondition" class="px-10" />
      </v-col>
    </v-row>
    <v-divider></v-divider>
    <v-card-text class="my-5">
      <v-hover v-slot="{ isHovering, props }">
        <h3 v-bind="props">
          {{ nickname }}:
          <v-expand-transition>
            <v-card :elevation="10" v-if="isHovering"
              ><v-btn variant="text">유저 페이지</v-btn
              ><v-btn variant="text" @click="addFollowing">팔로잉 </v-btn></v-card
            >
          </v-expand-transition>
        </h3>
        {{ content }}
      </v-hover>
    </v-card-text>
    <v-divider></v-divider>
    <div v-for="(comment, index) in comments" :key="index">
      <PostComment :comment="comment" @after-delete="afterDelete" />
    </div>
    <v-text-field
      class="px-5"
      v-model="commentContent"
      label="댓글 작성..."
      variant="underlined"
      append-inner-icon="mdi-comment-check-outline"
      @click:append-inner="addComment()"
      @keyup.enter="addComment"
    ></v-text-field>
  </v-card>
</template>
<script setup>
import { ref, onMounted, onUpdated } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import challengeFeed from '@/api/challengeFeed.js'
import memberApi from '@/api/member.js'
import { useCookies } from 'vue3-cookies'
import UpdateFeed from './item/UpdateFeed.vue'
import PostComment from './item/PostComment.vue'

import { apiInstance } from '@/api/index.js'

// 쿠키 사용
const { cookies } = useCookies()
const api = apiInstance()

// 멤버 정보 호출
const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

const route = useRoute()
const router = useRouter()
const feedId = ref(route.params.feedId)

// 화면에 표시할 데이터
const content = ref('')
const challenge = ref('')
const challengeTitle = ref('')
const hashtags = ref([])
const comments = ref([])
const imgs = ref([])
const nickname = ref('')
const feedLikeCount = ref(0)
const fileUrls = ref([])

// 팔로우 보낼 때 사용할 작성자 id
const feedWriterId = ref(0)

// 댓글 작성 모델
const commentContent = ref('')

// 피드 삭제 버튼 보여주기 위한 조건
const deleteCondition = ref(false)

const deleteFeed = async () => {
  try {
    await challengeFeed.deleteFeed(feedId.value)
    // 삭제 성공 후에 이전 화면으로 이동
    router.go(-1)
  } catch (error) {
    console.error('피드 삭제 중 에러:', error)
  }
}

// 마운트 시에 피드 상세 조회 API 호출, memberId가 변경되면 다시 호출
onMounted(async () => {
  try {
    const response = await challengeFeed.fetchFeedDetail(feedId.value)
    console.log(response)
    fileUrls.value = response.data.feed.fileUrls
    console.log(fileUrls.value)
    content.value = response.data.feed.content
    // imgs.value = response.data.feed.fileUrls
    challenge.value = response.data.feed.challengeId
    challengeTitle.value = response.data.feed.challengeTitle
    // 정규식을 사용하여 '#'으로 시작하는 단어를 추출하여 리스트로 만듦
    hashtags.value = response.data.feed.hashtag.match(/#[^\s#]+/g) || []
    comments.value = response.data.comments
    feedLikeCount.value = response.data.feed.feedLikeCount
    nickname.value = response.data.feed.nickname
    feedWriterId.value = response.data.feed.memberId
  } catch (error) {
    console.error('피드 상세 조회 중 에러:', error)
  }
})

// 마운트 시에 유저 피드 목록 조회하여 이 피드가 해당 유저 피드면 삭제 버튼 보여주기
onMounted(async () => {
  challengeFeed.getUserFeedList(memberId.value).then((response) => {
    const data = response.data.feedList
    // data 순회하면서 id만 추출
    const feedIdList = data.map((item) => item.id)
    const intFeedId = parseInt(feedId.value)
    // feedIdList에 현재 피드 id가 있으면 condition을 true로 변경
    if (feedIdList.includes(intFeedId)) {
      deleteCondition.value = true
    }
  })
  memberApi.fetchFollowingList().then((response) => {
    console.log(response)
  })
})

// 마운트하면 팔로우 여부 확인

// 댓글 생성 및 댓글 목록 조회
const addComment = async () => {
  try {
    const commentData = {
      content: commentContent.value
    }

    // 댓글 생성
    await challengeFeed.postComment(feedId.value, commentData)

    // 댓글 목록 업데이트
    const response = await challengeFeed.fetchFeedDetail(feedId.value)
    comments.value = response.data.comments

    // 입력 필드 초기화
    commentContent.value = ''
  } catch (error) {
    console.error('에러 발생:', error)
  }
}

// emit받은 댓글 삭제 이후 데이터 다시 넣기
const afterDelete = (response) => {
  comments.value = response
}

// 좋아요
const addFeedLike = async () => {
  try {
    res = await challengeFeed.addFeedLike(feedId.value)
  } catch (error) {
    console.error('좋아요 에러:', error)
  }
}

// 팔로잉
const addFollowing = async () => {
  try {
    console.log(feedWriterId.value)
    await memberApi.addFollowing(feedWriterId.value).then((response) => {
      console.log(response)
    })
  } catch (error) {
    console.error('팔로잉 에러:', error)
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
