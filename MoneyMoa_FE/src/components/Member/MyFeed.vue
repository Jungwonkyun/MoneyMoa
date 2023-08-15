<template>
  <v-container>
    <template v-for="(feedGroup, challengeId) in groupedFeeds" :key="challengeId">
      <v-row>
        <v-col v-for="(feed, index) in feedGroup" :key="index" cols="4">
          <router-link :to="`/challenge/feed/${feed.id}`">
            <v-card width="200" height="200" :elevation="10">
              <v-img class="align-end text-white" :src="feed.fileUrls[0]" cover>
                <v-card-title>{{ challengeId }}</v-card-title>
              </v-img>
              <v-card-subtitle>좋아요: {{ feed.feedLikeCount }} </v-card-subtitle>
            </v-card>
          </router-link>
        </v-col>
      </v-row>
    </template>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import challengeFeed from '@/api/challengeFeed.js'

const route = useRoute()
const memberId = computed(() => route.params.id)

const myFeed = ref([])

onMounted(async () => {
  const response = await challengeFeed.getUserFeedList(memberId.value)
  console.log(response)
  myFeed.value = response.data.feedList
})

// 챌린지 ID별로 그룹화된 배열을 계산된 속성으로 생성
const groupedFeeds = computed(() => {
  const groups = {}
  myFeed.value.forEach((feed) => {
    const challengeId = feed.challengeId
    if (!groups[challengeId]) {
      groups[challengeId] = []
    }
    groups[challengeId].push(feed)
  })
  return groups
})
</script>

<style></style>
