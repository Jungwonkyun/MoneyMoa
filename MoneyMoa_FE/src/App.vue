<template>
  <v-app>
    <v-main>
      <HeaderItem></HeaderItem>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import HeaderItem from './components/common/HeaderItem.vue'
</script>
