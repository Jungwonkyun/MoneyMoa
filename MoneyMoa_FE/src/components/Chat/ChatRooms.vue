<template>
  <h3>채팅방 목록</h3>
  <v-card v-for="(room, index) in roomList" :key="index" variant="outlined">
    <v-container>
      <v-row>
        <v-card-title>{{ room.name }}</v-card-title>
        <v-card-subtitle>{{ room.userCount }}명 참여중</v-card-subtitle>
        <router-link
          :to="{
            name: 'chatroomdetail',
            params: { roomId: room.roomId }
          }"
        >
          <v-btn>입장</v-btn>
        </router-link>
      </v-row>
    </v-container>
  </v-card>
</template>
<script setup>
import { ref } from 'vue'
import { getRooms } from '@/api/chat'

const roomList = ref([])
getRooms().then((response) => {
  console.log(response.data)
  roomList.value = response.data
})
</script>
<style></style>
