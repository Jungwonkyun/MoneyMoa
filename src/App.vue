<template>
  <v-app>
    <HeaderItem></HeaderItem>
    <v-main>
      <v-container>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import HeaderItem from './components/common/HeaderItem.vue'
</script>
