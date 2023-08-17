<template>
  <v-card class="mx-auto" max-width="700" :elevation="10">
    <v-carousel hide-delimiters show-arrows="hover" v-model="selectedSlide">
      <v-carousel-item
        v-for="(fileUrl, index) in feed.fileUrls"
        :key="index"
        :src="fileUrl"
        cover
        :value="index"
      >
        <v-card-title>
          <span class="shadow-text text-white">{{ feed.challengeTitle }}</span>
        </v-card-title>
      </v-carousel-item>
    </v-carousel>

    <v-row align="center" class="mt-1">
      <v-col cols="9" class="d-flex flex-row justify-start">
        <v-card-subtitle v-for="(hashtag, index) in hashtags" :key="index">
          <v-chip @click.stop="searchFeed(hashtag)">{{ hashtag }}</v-chip>
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
              <v-btn v-if="!isFollowing" variant="text" @click="addFollowing(feed.memberId)"
                >팔로우</v-btn
              >
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import challengeFeed from '@/api/challengeFeed.js'
import memberApi from '@/api/member.js'
import { useCookies } from 'vue3-cookies'
import UpdateFeed from './item/UpdateFeed.vue'
import PostComment from './item/PostComment.vue'

import { apiInstance } from '@/api/index.js'

const { cookies } = useCookies()
const api = apiInstance()

const memberInfo = ref(cookies.get('member'))
const memberId = ref(memberInfo.value.id)

const getProfileImg = memberApi.getSombodyInfoApi

const route = useRoute()
const router = useRouter()
const feedId = ref(route.params.feedId)

const comments = ref([])
const feed = ref({})
const hashtags = ref([])
const likeCount = ref(0)
const commentContent = ref('')
const deleteCondition = ref(false)
const likedList = ref([])
const isFollowing = ref(false)
const selectedSlide = ref(0)

const deleteFeed = async () => {
  try {
    await challengeFeed.deleteFeed(feedId.value)
    router.push('/challenge/feedList').then(() => {
      location.reload()
    })
  } catch (error) {
    console.error('피드 삭제 중 에러:', error)
  }
}

onMounted(async () => {
  try {
    challengeFeed.fetchFeedDetail(feedId.value).then((response) => {
      const feedData = response.data.feed
      for (const likedMember of response.data.likedMembers) {
        likedList.value.push(likedMember.memberId)
      }
      getProfileImg(response.data.feed.memberId).then((response) => {
        feedData.imgUrl = response.data.sombody.imageUrl
      })
      feed.value = feedData
      hashtags.value = response.data.feed.hashtag.match(/#[^\s#]+/g) || []
      likeCount.value = response.data.likeCount

      memberApi.fetchFollowingList().then((response) => {
        const followingList = response.data['my following list']
        for (const following of followingList) {
          if (following.toMemberId === feedData.memberId) {
            isFollowing.value = true
            break
          }
        }
      })

      const promises = response.data.comments.map((comment) =>
        getProfileImg(comment.memberId).then((response) => ({
          ...comment,
          imgUrl: response.data.sombody.imageUrl
        }))
      )
      Promise.all(promises).then((res) => {
        comments.value = res
      })
    })
    selectedSlide.value = 0
  } catch (error) {
    console.error('피드 상세 조회 중 에러:', error)
  }
})

onMounted(async () => {
  challengeFeed.getUserFeedList(memberId.value).then((response) => {
    const data = response.data.feedList
    const feedIdList = data.map((item) => item.id)
    const intFeedId = parseInt(feedId.value)
    if (feedIdList.includes(intFeedId)) {
      deleteCondition.value = true
    }
  })
})

const addComment = async () => {
  try {
    const commentData = {
      content: commentContent.value
    }
    await challengeFeed.postComment(feedId.value, commentData)
    const response = await challengeFeed.fetchFeedDetail(feedId.value)
    comments.value = response.data.comments
    commentContent.value = ''
  } catch (error) {
    console.error('에러 발생:', error)
  }
}

const afterDelete = (response) => {
  comments.value = response
}

const addFeedLike = async () => {
  try {
    if (!likedList.value.includes(parseInt(memberId.value))) {
      await challengeFeed.addFeedLike(memberId.value).then((response) => {
        likeCount.value++
      })
    } else {
      await challengeFeed.deleteFeedLike(memberId.value).then((response) => {
        likeCount.value--
      })
    }
  } catch (error) {
    console.error('좋아요 에러:', error)
  }
}

const deleteFeedLike = async () => {
  try {
    await challengeFeed.deleteFeedLike(feedId.value).then((response) => {
      likeCount.value--
    })
  } catch (error) {
    console.error('좋아요 취소 에러:', error)
  }
}

const addFollowing = async (memberId) => {
  try {
    await memberApi.addFollowing(memberId).then((response) => {
      console.log(response)
    })
  } catch (error) {
    console.error('팔로잉 에러:', error)
  }
}

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
