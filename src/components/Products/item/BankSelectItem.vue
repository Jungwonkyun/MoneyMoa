<template>
  <div>
    <v-sheet class="mx-auto">
      <v-slide-group multiple show-arrows>
        <v-slide-group-item v-for="bank in banks" :key="banks" v-slot="{ isSelected, toggle }">
          <v-btn class="ma-2" rounded :color="isSelected ? 'primary' : undefined" @click="toggle">
            {{ bank }}
          </v-btn>
        </v-slide-group-item>
      </v-slide-group>
    </v-sheet>
  </div>
</template>
<script setup>
import { reactive } from 'vue'

const banks = reactive([
  'KB국민은행',
  '광주은행',
  'NH농협은행',
  '대구은행',
  '신한은행',
  '신협은행',
  '우리은행',
  '하나은행'
])
</script>
<style>
v-slide-group {
  background-color: black;
  display: none;
}
</style>
