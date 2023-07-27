<template>
  <v-container>
    <v-row>
      <v-col v-for="(feed, index) in myFeed[0].Feed" :key="index" cols="4">
        <v-card :width="250" height="200" :elevation="10" class="m-30">
          <v-img :src="feed.img_url"></v-img>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
import { ref } from 'vue'

export default {
  setup() {
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
    const myFeed = ref(dummy)
    return {
      myFeed
    }
  }
}
</script>
<style></style>
