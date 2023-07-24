<template>
  <v-card :text="product.bank_name" variant="tonal">{{ product.product_name }}</v-card>
</template>
<script setup>
defineProps({
  product: Object
})
</script>
<style></style>
