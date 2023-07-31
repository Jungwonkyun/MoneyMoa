<template>
  <div>
    <v-sheet class="mx-auto">
      <h5>은행 선택</h5>
      <v-btn variant="plain" @click="selectAllBank"> 모두 선택 </v-btn
      ><v-btn variant="plain" @click="cancelAllBank"> 모두 선택 해제 </v-btn>
      <v-slide-group multiple show-arrows>
        <v-slide-group-item v-for="bank in bankList" :key="bankList">
          <v-btn
            class="ma-2"
            rounded
            :color="bank.selected ? 'primary' : undefined"
            @click="toggleSelected(bank)"
          >
            <v-icon icon="mdi-heart"></v-icon>
            {{ bank.name }}
          </v-btn>
        </v-slide-group-item>
      </v-slide-group>
    </v-sheet>
  </div>
</template>
<script setup>
import { useProductStore } from '@/stores/productStore'
import { storeToRefs } from 'pinia'
const store = useProductStore()
const { bankList } = storeToRefs(store)
const { selectAllBank, cancelAllBank } = store
const toggleSelected = (bank) => {
  bank.selected = !bank.selected
}
</script>
<style scoped lang="scss"></style>
