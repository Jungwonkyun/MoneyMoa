<template>
  <h3>채팅방 목록</h3>
  <v-card v-for="(room, index) in roomList" :key="index">
    <v-card-title>{{ room.name }}</v-card-title>
  </v-card>
</template>
<script setup>
import { ref } from 'vue'
import { getRooms } from '@/api/chat'

const roomList = ref([])
getRooms().then((response) => {
  console.log(response)
})
</script>
<style></style>
