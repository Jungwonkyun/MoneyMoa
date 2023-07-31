<template>
  <!-- 금융사전 메뉴 -->
  <v-menu
    v-model="menu"
    :close-on-content-click="false"
    location="start"
    transition="fab-transition"
  >
    <template v-slot:activator="{ props }">
      <v-btn color="indigo" v-bind="props"> <v-icon icon="mdi-magnify"></v-icon> </v-btn>
    </template>
    <!-- 여기서 크기조절 -->
    <v-card width="500" max-height="500">
      <v-list>
        <v-list-item title="금융사전">
          <!-- 금융사전 검색창입니다 -->
          <v-text-field
            clearable
            placeholder="키워드 검색"
            variant="underlined"
            append-inner-icon="mdi-magnify"
          >
          </v-text-field>
        </v-list-item>
      </v-list>
      <v-divider></v-divider>

      <v-list>
        <v-expansion-panels variant="accordion">
          <DictionaryItem v-for="index in 100" :key="index" :item="list[0].value" />
        </v-expansion-panels>
      </v-list>

      <!-- 뭐하는앤지 모르겠는데 오류날까봐 일단놔둠 -->
      <v-card-actions>
        <v-spacer></v-spacer>
        <!--  -->

        <v-btn variant="text" @click="menu = false"> Cancel </v-btn>
      </v-card-actions>
    </v-card>
  </v-menu>
</template>

<script setup>
import { ref } from 'vue'
import DictionaryItem from './item/DictionaryItem.vue'

const menu = ref(false)

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
