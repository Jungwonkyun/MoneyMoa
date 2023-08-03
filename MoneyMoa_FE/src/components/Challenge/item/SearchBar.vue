<template>
  <v-text-field
    label="검색"
    placeholder="Search..."
    variant="solo-filled"
    v-model="searchWord"
    type="text"
    @input="handleSearch"
    @keyup.enter="searchFeed(searchWord)"
    append-inner-icon="mdi-magnify"
    @click:append-inner="searchFeed(searchWord)"
  >
  </v-text-field>
</template>
<script setup>
import { ref } from 'vue'
import functions from '@/api/challegeFeed.js'

const searchWord = ref('')
const handleSearch = (e) => {
  searchWord.value = e.target.value
  console.log(searchWord.value)
}

const searchFeed = functions.searchFeed
</script>
<style></style>
