<template>
  <v-container>
    <v-sheet
      max-width="900"
      class="mx-auto mt-8 rounded-lg px-10 py-5"
      min-height="450"
      width="100%"
    >
      <v-row>
        <v-col>
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
        </v-col>
      </v-row>
    </v-sheet>
    <v-sheet class="fixed">
      <DictionarySide />
    </v-sheet>
  </v-container>
</template>

<script setup>
import DictionaryItem from '../components/Dictionary/item/DictionaryItem.vue'
import DictionarySide from '../components/Dictionary/DictionarySide.vue'
import { ref } from 'vue'

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
const searchWord = ref('')
const list = ref([Item1, Item2, Item3])
const listall = [Item1, Item2, Item3]
function onSearch() {
  if (!searchWord) {
    list.value = listall
    return
  }
  list.value = listall.filter(
    (item) => item.word.includes(searchWord.value) || item.description.includes(searchWord.value)
  )
}
</script>
<style scoped lang="scss">
.fixed {
  position: fixed;
  bottom: 100px;
  right: 10%;
  border-radius: 100px;
}
</style>
