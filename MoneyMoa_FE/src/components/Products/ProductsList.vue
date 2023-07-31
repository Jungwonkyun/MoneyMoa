<template>
  <div>
    <BankSelectItem />
    {{ productType }} 리스트를 불러오자
  </div>
</template>
<script setup>
import BankSelectItem from './item/BankSelectItem.vue'
defineProps({
  productType: String
})
</script>
<style></style>
