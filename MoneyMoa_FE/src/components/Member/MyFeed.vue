<template>
  <v-container>
    <v-row>
      <v-col v-for="(feed, index) in myFeed" :key="index" cols="4">
        <router-link :to="`/challenge/feed/${feed.id}`">
          <v-card width="300" height="300" :elevation="10">
            <v-img class="align-end text-white" :src="feed.fileUrls[0]" cover> </v-img>
          </v-card>
        </router-link>
      </v-col>
    </v-row>
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
  console.log(myFeed.value)
})

// 챌린지 ID별로 그룹화된 배열을 계산된 속성으로 생성
// const groupedFeeds = computed(() => {
//   const groups = {}
//   myFeed.value.forEach((feed) => {
//     const challengeId = feed.challengeId
//     if (!groups[challengeId]) {
//       groups[challengeId] = []
//     }
//     groups[challengeId].push(feed)
//   })
//   console.log(groups)
//   return groups
// })
</script>

<style></style>
