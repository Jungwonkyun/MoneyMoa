<template>
  <v-containter>
    <v-row>
      <v-textarea
        variant="outlined"
        prepend-icon="mdi-comment-processing-outline"
        rows="2"
        no-resize
      ></v-textarea>
      <v-btn>댓글 작성</v-btn>
    </v-row>
    <!-- 댓글목록 -->
  </v-containter>
</template>
<script setup>
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
const props = defineProps({
  productCode: String //cma의 경우 id값
})
const store = useProductStore()
const { productType } = storeToRefs(store)
</script>
<style></style>
