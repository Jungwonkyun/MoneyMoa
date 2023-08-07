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

      <v-col cols="2" align="center">
        <v-icon @click="addFeedLike(feedId)" size="large" icon="mdi-heart" />
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
import { useRoute } from 'vue-router'
import functions from '@/api/challegeFeed.js'

const route = useRoute()
const feedId = ref(route.params.feedId)

const content = ref('')
const challenge = ref('')
const hashtags = ref([])

// 마운트 시에 피드 상세 조회 API 호출, memberId가 변경되면 다시 호출
onMounted(() => {
  const res = functions.fetchFeedDetail(feedId.value).then((response) => {
    console.log(response.data.result.data)
    const data = ref('')
    data.value = response.data.result.data
    content.value = data.value.content
    challenge.value = data.value.challenge
    // 정규식을 사용하여 '#'으로 시작하는 단어를 추출하여 리스트로 만듦
    hashtags.value = data.value.hashtag.match(/#[^\s#]+/g) || []
  })
})

const addFeedLike = functions.addFeedLike

const searchFeed = functions.searchFeed
</script>
<style></style>
