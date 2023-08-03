<template>
  <v-expansion-panel :title="item.word" :text="item.description"> </v-expansion-panel>
</template>

<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  item: Object
})
</script>

<style></style>
