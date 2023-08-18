<template>
  <v-expansion-panel>
    <v-expansion-panel-title>
      <span class="highlighted-value">
        <template v-for="(part, index) in highlightMatchedText(item.term)" :key="index">
          <strong v-if="part.matched" class="matched-text">{{ part.text }}</strong>
          <span v-else>{{ part.text }}</span>
        </template>
      </span>
    </v-expansion-panel-title>
    <v-expansion-panel-text>
      <span>
        <template v-for="(part, index) in highlightMatchedText(item.definition)" :key="index">
          <strong v-if="part.matched" class="matched-text">{{ part.text }}</strong>
          <span v-else>{{ part.text }}</span>
        </template>
      </span>
    </v-expansion-panel-text>
  </v-expansion-panel>
</template>

<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  item: Object,
  searchWord: String
})

// 검색어와 일치하는 부분을 강조 표시하는 함수
function highlightMatchedText(text) {
  if (text === undefined) {
    return []
  }
  if (props.searchWord) {
    const regex = new RegExp(props.searchWord, 'gi')
    const parts = []
    let lastIndex = 0
    let match
    while ((match = regex.exec(text)) !== null) {
      if (match.index > lastIndex) {
        parts.push({ text: text.slice(lastIndex, match.index), matched: false })
      }
      parts.push({ text: match[0], matched: true })
      lastIndex = regex.lastIndex
    }
    if (lastIndex < text.length) {
      parts.push({ text: text.slice(lastIndex), matched: false })
    }
    return parts
  } else {
    return [{ text, matched: false }]
  }
}
</script>

<style scoped lang="scss">
.matched-text {
  background-color: rgb(209, 237, 255);
}
</style>
