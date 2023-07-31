<template>
  <v-card variant="tonal">
    <v-row class="d-flex justify-space-between">
      <v-col>
        <v-card-item>
          <v-card-subtitle>
            <v-icon icon="mdi-face" />
            {{ product.bank_name }}
          </v-card-subtitle>
          <v-card-title>
            {{ product.product_name }}
          </v-card-title>
        </v-card-item>
      </v-col>
      <v-col cols="2" align-self="center">금리 {{ product.interest }}% </v-col>
    </v-row>
  </v-card>
  <v-table>
    <tbody>
      <tr>
        <td>가입대상</td>
        <td>{{ product.join_member }}</td>
      </tr>
      <tr>
        <td>최고한도</td>
        <td>{{ product.max_limit }}</td>
      </tr>
      <tr>
        <td>유의사항</td>
        <td>{{ product.etc_note }}</td>
      </tr>
    </tbody>
  </v-table>
  <IntrCalcItem></IntrCalcItem>
</template>
<script setup>
import { useProductStore } from '../../stores/productStore'
import IntrCalcItem from './item/IntrCalcItem.vue'
const store = useProductStore()
const product = store.selectedProduct
</script>
<style></style>
