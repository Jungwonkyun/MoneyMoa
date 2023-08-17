<template>
  <v-expansion-panel>
    <v-expansion-panel-title
      ><span v-html="highlightMatchedText(item.term)"></span>
    </v-expansion-panel-title>
    <v-expansion-panel-text>
      <span v-html="highlightMatchedText(item.definition)"></span>
    </v-expansion-panel-text>
  </v-expansion-panel>
</template>

<script setup>
import { defineProps, onMounted } from 'vue'

const props = defineProps({
  item: Object,
  searchWord: String
})

// 검색어와 일치하는 부분을 진하게 표시하는 함수
function highlightMatchedText(text) {
  if (text === undefined) {
    return text
  }
  if (props.searchWord) {
    const regex = new RegExp(props.searchWord, 'gi')
    return text.replace(regex, (matched) => `<strong>${matched}</strong>`)
  } else {
    return text
  }
}
onMounted(() => {
  highlightMatchedText()
})
</script>

<style></style>
