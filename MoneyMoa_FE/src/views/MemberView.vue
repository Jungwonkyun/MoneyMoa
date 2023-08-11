<template>
  <v-container>
    <v-row justify="space-around">
      <v-col cols="3">
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
</script>

<style></style>
