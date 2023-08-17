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
        <v-sheet class="d-flex flex-column align-center">
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
        </v-sheet>
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
import { ref, onMounted } from 'vue'
import DictionaryItem from './item/DictionaryItem.vue'
import question_moa from '@/assets/img/question_moa.png'
import functions from '../../api/dictionary.js'
const searchWord = ref('')
function onSearch() {
  if (searchWord.value) {
    list.value = listall.filter(
      (item) => item.term.includes(searchWord.value) || item.definition.includes(searchWord.value)
    )
  } else {
    list.value = listall
  }
}
const menu = ref(false)

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
