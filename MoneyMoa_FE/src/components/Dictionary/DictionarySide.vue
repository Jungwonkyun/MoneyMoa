<template>
  <!-- 금융사전 메뉴 -->
  <v-menu
    v-model="menu"
    :close-on-content-click="false"
    location="start"
    transition="fab-transition"
  >
    <template v-slot:activator="{ props }">
      <v-sheet class="dic-Btn rounded-circle d-flex align-center" v-bind="props" elevation="4">
        <!-- <v-icon icon="mdi-magnify"></v-icon> -->
        <v-img :src="question_moa" :height="60"></v-img>
      </v-sheet>
    </template>
    <!-- 여기서 크기조절 -->
    <v-sheet rounded="3">
      <v-card width="500" max-height="500" class="d-flex flex-column">
        <v-card-title>금융사전</v-card-title>
        <v-list class="d-flex flex-column align-center">
          <!-- 금융사전 검색창입니다 -->
          <v-text-field
            clearable
            class="dictionary-search"
            placeholder="용어 검색"
            variant="underlined"
            append-inner-icon="mdi-magnify"
            @keyup.enter="onSearch"
            v-model="searchWord"
          >
            <template #append-inner-icon>
              <v-icon @click="onSearch" icon="mdi-magnify"></v-icon>
            </template>
          </v-text-field>
        </v-list>
        <v-list>
          <v-expansion-panels variant="accordion">
            <DictionaryItem
              v-for="(item, index) in list"
              :key="index"
              :item="item"
              :searchWord="searchWord"
            />
          </v-expansion-panels>
        </v-list>
        <v-card-actions class="text-right">
          <v-btn variant="text" @click="menu = false" class="ml-auto"> 닫기 </v-btn>
        </v-card-actions>
      </v-card>
    </v-sheet>
  </v-menu>
</template>

<script setup>
import { ref } from 'vue'
import DictionaryItem from './item/DictionaryItem.vue'
import question_moa from '@/assets/img/question_moa.png'

const searchWord = ref('')
function onSearch() {
  if (searchWord.value) {
    list.value = listall.filter(
      (item) => item.word.includes(searchWord.value) || item.description.includes(searchWord.value)
    )
  } else {
    list.value = listall
  }
}
const menu = ref(false)

const Item1 = {
  word: 'item1',
  description:
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
}
const Item2 = {
  word: 'item2',
  description:
    'Loremipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
}
const Item3 = {
  word: 'item3',
  description:
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
}

const listall = [Item1, Item2, Item3]
const list = ref(listall)
</script>
<style scoped lang="scss">
.dic-Btn {
  background-color: $primary-color;
  color: white;
  width: 80px;
  height: 80px;
  text-align: center;
  line-height: 60px;
  cursor: pointer;
}
.dictionary-search {
  align-self: center;
  width: 90%;
}
</style>
