<template>
  <div>
    <v-sheet class="mx-auto">
      <v-slide-group multiple show-arrows>
        <v-slide-group-item
          v-for="security in securities"
          :key="securities"
          v-slot="{ isSelected, toggle }"
        >
          <v-btn class="ma-2" rounded :color="isSelected ? 'primary' : undefined" @click="toggle">
            <v-icon icon="mdi-heart"></v-icon>
            {{ security }}
          </v-btn>
        </v-slide-group-item>
      </v-slide-group>
    </v-sheet>
  </div>
</template>
<script setup>
import { reactive } from 'vue'

const securities = reactive([
  'KB증권',
  'SK증권',
  'NH투자증권',
  'IBK투자증권',
  '삼성증권',
  '대신증권',
  '한화투자증권',
  '하나증권'
])
</script>
<style></style>
