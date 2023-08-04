<template>
  <v-card class="mx-auto" max-width="700">
    <v-img
      class="align-end text-white"
      height="600"
      src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
      cover
    >
    </v-img>

    <v-row align="center" class="mt-1">
      <v-col cols="10" class="d-flex flex-row justify-start">
        <v-card-subtitle>
          <v-chip @click="searchFeed(해시태그1)">#해시태그1</v-chip>
        </v-card-subtitle>
        <v-card-subtitle>
          <v-chip>#해시태그2</v-chip>
        </v-card-subtitle>
      </v-col>
      <v-col cols="2" align="center">
        <v-icon @click="addFeedLike(feedId)" size="large" icon="mdi-heart" />
      </v-col>
    </v-row>

    <v-card-text>
      <div>본문</div>
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
import { mdiAccount } from '@mdi/js'

const route = useRoute()
const feedId = ref(route.params.feedId)

// 마운트 시에 피드 상세 조회 API 호출, memberId가 변경되면 다시 호출
onMounted(() => {
  const res = functions.fetchFeedDetail(feedId.value)
})

const addFeedLike = functions.addFeedLike
// res를 파싱해서 화면에 표시

const searchFeed = functions.searchFeed
</script>
<style></style>
