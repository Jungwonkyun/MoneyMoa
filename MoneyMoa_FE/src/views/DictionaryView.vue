<template>
  <v-container>
    <v-sheet
      max-width="1200"
      class="mx-auto mt-8 rounded-lg px-10 py-5 animate__animated animate__fadeInLeft"
      min-height="450"
      width="100%"
      elevation="3"
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
import { ref, onMounted } from 'vue'
import 'animate.css'
import functions from '../api/dictionary.js'

const list = ref(null)
let listall = null
async function getDict() {
  try {
    const res = await functions.getDictionary()
    listall = res.data
    list.value = res.data
  } catch (error) {
    alert(error)
    console.error(error)
  }
}
onMounted(() => {
  getDict()
})

const searchWord = ref('')

function onSearch() {
  if (!searchWord.value) {
    list.value = listall
    return
  }
  list.value = listall.filter(
    (item) => item.term.includes(searchWord.value) || item.definition.includes(searchWord.value)
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
