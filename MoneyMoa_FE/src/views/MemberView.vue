<template>
  <v-container>
    <v-row>
      <v-col col="2" sm="4">
        <UserInfo />
      </v-col>
      <v-col cols="10" sm="5" offset-sm="3" offset-xl="1">
        <router-view />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import UserInfo from '@/components/Member/UserInfo.vue'
</script>

<style></style>
