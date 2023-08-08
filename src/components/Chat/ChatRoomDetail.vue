<template>
  <v-card variant="outlined">
    <v-card-title>{{ room.name }}</v-card-title>
    <v-card-subtitle>{{ room.userCount }}명 참여중</v-card-subtitle>
  </v-card>
  <v-sheet v-for="(msg, index) in room.chatmessage" elevation="10" rounded>
    {{ msg.sender }}: {{ msg.message }}
  </v-sheet>
</template>
<script setup>
import { ref, inject } from 'vue'
import { onMessage, onOpen, onClose, onError } from 'vue3-websocket'
import { getRoomDetail } from '@/api/chat'
import { useRoute } from 'vue-router'

const route = useRoute()
const room = ref({})
getRoomDetail(route.params.roomId).then((response) => {
  room.value = response.data
})
</script>
<style></style>
