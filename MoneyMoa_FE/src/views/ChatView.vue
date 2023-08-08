<template>
  <h1>open chat</h1>
  <router-view />
  <v-container>
    <v-row>
      <!-- <v-text-field v-model="text" />
      <v-btn @click="sendMessage">Send a message</v-btn> -->
    </v-row>
  </v-container>
</template>
<script setup>
import { ref, inject } from 'vue'
import { onMessage, onOpen, onClose, onError } from 'vue3-websocket'

const text = ref('')

// const socket = inject('socket')

// const sendMessage = () => socket.value.send(text.value)

// onOpen(() => {
//   console.log('WS connection is stable! ~uWu~')
// })

// onMessage((message) => {
//   console.log('Got a message from the WS: ', message)
// })

// onClose(() => {
//   console.log('No way, connection has been closed 😥')
// })

// onError((error) => {
//   console.error('Error: ', error)
// })
</script>
<style></style>
