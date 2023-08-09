<template>
  <v-container>
    <v-row>
      <v-col v-for="(feed, index) in myFeed" :key="index" cols="4">
        <router-link :to="`/challenge/feed/${feed.id}`">
          <v-card width="100%" height="100%" :elevation="10" class="m-30">
            <v-img
              class="align-end text-white"
              src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
              cover
            >
              <v-card-title>{{ feed.challenge }}</v-card-title>
            </v-img>
            <v-card-subtitle>현재까지 모은 금액: {{ feed.depositAmount }} </v-card-subtitle>
          </v-card>
        </router-link>
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import challengeFeed from '@/api/challengeFeed.js'

const dummy = ref([
  {
    message: 'success',
    Feed: [
      {
        id: 1,
        img_url: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
        content: '첫 입금!',
        likes: '33',
        current: '333',
        created_at: '2023-07-28',
        hashtag: [
          {
            feed_no: 1,
            content: '#첫_입금'
          }
        ],
        feedcomment: [
          {
            feed_no: 1,
            member_id: 1,
            content: '축하해요!',
            created_at: '2023-07-28'
          }
        ]
      },
      {
        id: 2,
        img_url: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
        content: '두 번째 입금!',
        likes: '33',
        current: '333',
        created_at: '2023-07-29',
        hashtag: [
          {
            feed_no: 2,
            content: '#2_입금'
          }
        ],
        feedcomment: [
          {
            feed_no: 2,
            member_id: 2,
            content: '2222축하해요!',
            created_at: '2023-07-29'
          }
        ]
      },
      {
        id: 3,
        img_url: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
        content: '두 번째 입금!',
        likes: '33',
        current: '333',
        created_at: '2023-07-29',
        hashtag: [
          {
            feed_no: 2,
            content: '#2_입금'
          }
        ],
        feedcomment: [
          {
            feed_no: 2,
            member_id: 2,
            content: '2222축하해요!',
            created_at: '2023-07-29'
          }
        ]
      },
      {
        id: 4,
        img_url: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
        content: '두 번째 입금!',
        likes: '33',
        current: '333',
        created_at: '2023-07-29',
        hashtag: [
          {
            feed_no: 2,
            content: '#2_입금'
          }
        ],
        feedcomment: [
          {
            feed_no: 2,
            member_id: 2,
            content: '2222축하해요!',
            created_at: '2023-07-29'
          }
        ]
      },
      {
        id: 5,
        img_url: 'https://cdn.vuetifyjs.com/images/parallax/material.jpg',
        content: '두 번째 입금!',
        likes: '33',
        current: '333',
        created_at: '2023-07-29',
        hashtag: [
          {
            feed_no: 2,
            content: '#2_입금'
          }
        ],
        feedcomment: [
          {
            feed_no: 2,
            member_id: 2,
            content: '2222축하해요!',
            created_at: '2023-07-29'
          }
        ]
      }
    ]
  }
])
const myFeed = ref([])

const route = useRoute()
const memberId = ref(route.params.id)

onMounted(() => {
  const res = challengeFeed.getUserFeedList(memberId.value).then((response) => {
    console.log(response.data.result.data)
    myFeed.value = response.data.result.data
  })
})
</script>
<style></style>
