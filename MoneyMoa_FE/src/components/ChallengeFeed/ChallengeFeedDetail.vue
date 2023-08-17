<template>
  <v-card class="mx-auto" max-width="700" :elevation="10">
    <v-carousel hide-delimiters show-arrows="hover">
      <v-carousel-item
        v-for="(fileUrl, index) in feed.fileUrls"
        :key="index"
        :src="fileUrl"
        cover
        :value="index"
      >
        <v-card-title
          ><span class="shadow-text text-white">{{ feed.challengeTitle }}</span>
        </v-card-title>
      </v-carousel-item>
    </v-carousel>

    <v-row align="center" class="mt-1">
      <v-col cols="9" class="d-flex flex-row justify-start">
        <v-card-subtitle v-for="(hashtag, index) in hashtags" :key="index">
          <v-chip @click.stop="searchFeed(해시태그1)">{{ hashtag }}</v-chip>
        </v-card-subtitle>
      </v-col>

      <v-col cols="3" class="flex px-0 ma-0">
        <v-btn @click.stop="addFeedLike" size="large" icon="mdi-heart" variant="text"></v-btn>
        {{ likeCount }}

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
    <v-card-text>
      <div class="profile-container">
        <v-menu open-on-hover>
          <template v-slot:activator="{ props }">
            <img :src="feed.imgUrl" class="profile-img" />
            <v-btn variant="text" class="profile-nickname" v-bind="props">
              {{ feed.nickname }}
            </v-btn>
          </template>
          <v-list>
            <v-list-item>
              <v-btn variant="text" @click="toUserPage(feed.memberId)">유저페이지</v-btn>
            </v-list-item>
            <v-list-item>
              <v-btn variant="text" @click="addFollowing">팔로우</v-btn>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
      <div class="my-4 text-subtitle-1">{{ feed.content }}</div>
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
import { ref, onMounted, watchEffect, toRefs } from 'vue'
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

// getsombodyinfoapi를 통해 프로필 이미지 가져오기
const getProfileImg = memberApi.getSombodyInfoApi

const route = useRoute()
const router = useRouter()
const feedId = ref(route.params.feedId)

// 댓글 데이터 담을 변수
const comments = ref([])

// 피드 데이터 담을 변수
const feed = ref({})

// 해시태그 담을 변수
const hashtags = ref([])

// 좋아요 수
const likeCount = ref(0)

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
    router.push('/challenge/feedList').then(() => {
      location.reload()
    })
  } catch (error) {
    console.error('피드 삭제 중 에러:', error)
  }
}

// feed관찰하다가 변경 사항
// feed.value = response.data.feed
// // 정규식을 사용하여 '#'으로 시작하는 단어를 추출하여 리스트로 만듦
// hashtags.value = response.data.feed.hashtag.match(/#[^\s#]+/g) || []
// likeCount.value = response.data.likeCount

// 마운트 시에 피드 상세 조회 API 호출, memberId가 변경되면 다시 호출
onMounted(async () => {
  try {
    challengeFeed.fetchFeedDetail(feedId.value).then((response) => {
      console.log(response)
      const feedData = response.data.feed
      getProfileImg(response.data.feed.memberId).then((response) => {
        console.log(response.data.sombody.imageUrl)
        feedData.imgUrl = response.data.sombody.imageUrl
      })
      console.log(feedData)
      feed.value = feedData
      // 정규식을 사용하여 '#'으로 시작하는 단어를 추출하여 리스트로 만듦
      hashtags.value = response.data.feed.hashtag.match(/#[^\s#]+/g) || []
      likeCount.value = response.data.likeCount
      const promises = response.data.comments.map((comment) =>
        getProfileImg(comment.memberId).then((response) => ({
          ...comment,
          imgUrl: response.data.sombody.imageUrl
        }))
      )
      Promise.all(promises).then((res) => {
        console.log(res)
        comments.value = res
      })
    })
  } catch (error) {
    console.error('피드 상세 조회 중 에러:', error)
  }
})

console.log(feed)

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
    await challengeFeed.addFeedLike(feedId.value).then((response) => {
      console.log(response)
      likeCount.value++
    })
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

// 유저 페이지로 이동
const toUserPage = (memberId) => {
  console.log(memberId)
  router.push(`/member/${memberId}`)
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
.profile-img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.profile-container {
  display: flex; /* 수평 정렬을 위해 flex 컨테이너로 설정 */
  align-items: center; /* 요소들을 수직 가운데 정렬 */
}

.profile-nickname {
  font-size: 17px;
  font-weight: 500;
}
</style>
