<template>
  <v-container>
    <v-row>
      <v-col>
        대화 중인 사용자 목록
        <v-card
          v-for="(item, index) in dmRooms"
          :key="index"
          class="dm-card"
          @click="enterDM(item)"
          variant="flat"
        >
          <v-card-title>{{ item.receiver }}</v-card-title>
          <v-divider />
        </v-card>
      </v-col>
      <v-col>
        <v-card v-if="!roomEmpty" variant="tonal">click list to start DM</v-card>
        <DMDetail v-else :room="selectedRoom" />
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getDMRooms, getRoomDetail, enterRoom } from '@/api/chat'
import { useCookies } from 'vue3-cookies'
import DMDetail from './DMDetail.vue'

const { cookies } = useCookies()
const router = useRouter()
const dmRooms = ref([])
const selectedRoom = ref({})
const roomEmpty = ref(false)

getDMRooms().then((response) => {
  dmRooms.value = response.data.dmList
  console.log(dmRooms.value)
})

function enterDM(DMRoom) {
  enterRoom(DMRoom.roomId).then((response) => {
    // selRoom.value = response.data.chatroomInfo
    selectedRoom.value = DMRoom
    roomEmpty.value = true
  })
}
</script>
<style scoped lang="scss"></style>
