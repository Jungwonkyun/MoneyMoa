<template>
  <v-form>
    <v-container>
      <v-row>
        <v-col v-if="productType == 'saving'">매달</v-col>
        <v-col>
          <v-text-field
            v-model="amount"
            hide-details
            single-line
            label="금액"
            type="number"
            min="0"
            @keypress="onKeyPress"
          />
        </v-col>
        <v-col v-if="productType == 'saving'">원씩</v-col>
        <v-col v-else>원을</v-col>
        <v-col><v-select label="기간" :items="periods" v-model="period"></v-select></v-col>
        <v-col v-if="productType == 'saving'">개월 동안 적금할거예요.</v-col>
        <v-col v-else>개월 동안 예금할거예요.</v-col>
      </v-row>
    </v-container>
  </v-form>
</template>
<script setup>
import { reactive } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
const store = useProductStore()
const { productType, amount, period } = storeToRefs(store)
const periods = reactive([6, 12, 24, 36])
const onKeyPress = (event) => {
  if (event.key === '+' || event.key === '-') {
    event.preventDefault()
  }
}
</script>
<style></style>
