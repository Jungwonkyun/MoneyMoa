<template>
  <v-container>
    <p>금융사전</p>
    <v-text-field
      clearable
      placeholder="키워드 검색"
      variant="underlined"
      v-model="searchWord"
      @keyup.enter="onSearch"
    >
      <template v-slot:append-inner>
        <v-icon @click="onSearch">mdi-magnify</v-icon>
      </template>
    </v-text-field>

    <v-expansion-panels variant="accordion">
      <DictionaryItem
        v-for="(item, index) in list"
        :key="index"
        :item="item"
        :searchWord="searchWord"
      />
    </v-expansion-panels>
    <br />
  </v-container>
  <DictionarySide />
</template>

<script setup>
import DictionaryItem from '../components/Dictionary/item/DictionaryItem.vue'
import DictionarySide from '../components/Dictionary/DictionarySide.vue'
import { ref } from 'vue'

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
const Item1 = {
  word: 'item1',
  description:
    '오케이 ㅋ adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
}
const Item2 = {
  word: 'item2',
  description:
    'Lorem ipsum dolor sit amet, consectetur  elit, sed do eiusmod tempor incididunt ut labore et dolore  aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
}
const Item3 = {
  word: 'item3',
  description:
    'Lorem ipsum dolor sit, consectetur  elit, sed do eiusmod tempor  ut labore et dolore magna aliqua. Ut  ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
}

const list = ref([Item1, Item2, Item3])
const listall = [Item1, Item2, Item3]
</script>
<style></style>
