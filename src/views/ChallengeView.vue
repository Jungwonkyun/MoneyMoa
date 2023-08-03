<template>
  <v-container>
    <SearchBar @custom-event="handleEvent" />
    <router-view :search-word="searchWord"></router-view>
  </v-container>
</template>
<script setup>
import { ref, watch } from 'vue'
import SearchBar from '@/components/Challenge/item/SearchBar.vue'

const searchWord = ref(null)

const handleEvent = (word) => {
  console.log(typeof word)
  // console.log(word)
  searchWord.value = word
}
</script>
<style></style>
