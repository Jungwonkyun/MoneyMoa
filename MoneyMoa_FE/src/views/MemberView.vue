<template>
  <v-container>
    <v-row>
      <v-col cols="6">
        <UserInfo />
      </v-col>
      <v-col cols="6">
        <router-view />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import UserInfo from '@/components/Member/UserInfo.vue'
// import { computed } from 'vue'
// import { useRoute } from 'vue-router'

// const route = useRoute()
// const memberId = computed(() => route.params.id)

// ref(route.params.id)

// console.log('멤버뷰' + memberId.value)
</script>

<style></style>
