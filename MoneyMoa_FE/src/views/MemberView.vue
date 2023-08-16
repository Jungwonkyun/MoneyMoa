<template>
  <v-container>
    <v-row justify="space-around">
      <v-col cols="4">
        <UserInfo />
      </v-col>
      <v-col cols="8">
        <router-view />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import UserInfo from '@/components/Member/UserInfo.vue'
</script>

<style></style>
