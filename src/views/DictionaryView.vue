<template>
  <div>
    <p>금융사전</p>
    <v-text-field
      clearable
      placeholder="키워드 검색"
      variant="underlined"
      append-inner-icon="mdi-magnify"
    >
    </v-text-field>

    <v-expansion-panels variant="accordion">
      <DictionaryItem v-for="(item, index) in list" :key="index" :item="item.value" />
    </v-expansion-panels>
    <br />
  </div>
  <DictionarySide />
</template>

<script setup>
import DictionaryItem from '../components/Dictionary/item/DictionaryItem.vue'
import DictionarySide from '../components/Dictionary/DictionarySide.vue'
import { ref } from 'vue'

const Item1 = ref({
  word: 'item1',
  description:
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
})
const Item2 = ref({
  word: 'item2',
  description:
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
})
const Item3 = ref({
  word: 'item3',
  description:
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
})

const list = ref([Item1, Item2, Item3])
</script>
<style></style>
