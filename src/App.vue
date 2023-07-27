<template>
  <v-app>
    <HeaderItem></HeaderItem>
    <div></div>
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import HeaderItem from './components/common/HeaderItem.vue'
</script>
