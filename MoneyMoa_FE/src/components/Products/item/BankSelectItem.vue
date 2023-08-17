<template>
  <v-sheet class="mx-auto">
    <h5>은행 선택</h5>
    <v-btn variant="plain" @click="selectAllBank(false)"> 모두 선택 </v-btn
    ><v-btn variant="plain" @click="cancelAllBank(false)"> 모두 선택 해제 </v-btn>
    <v-slide-group multiple show-arrows>
      <v-slide-group-item v-for="bank in bankList" :key="bankList">
        <v-btn
          class="ma-2"
          rounded
          :class="{ 'chip-selected': bank.selected }"
          @click="toggleSelected(bank)"
        >
          <v-img v-if="icons[bank.name]" :src="icons[bank.name].default" class="fin-icon"></v-img>
          {{ bank.name }}
        </v-btn>
      </v-slide-group-item>
    </v-slide-group>
  </v-sheet>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { loadBankIcons } from '@/api/icons'
import { storeToRefs } from 'pinia'
const store = useProductStore()
const { bankList } = storeToRefs(store)
const { selectAllBank, cancelAllBank } = store
// const icons = loadBankIcons()
// console.log('icons in bankselectitem:')
// console.log(icons)

const icons = reactive({})
loadBankIcons().then((bankIcons) => {
  Object.assign(icons, bankIcons)
  // console.log(icons)
})

const toggleSelected = (bank) => {
  bank.selected = !bank.selected
}
</script>
<style lang="scss">
.chip-selected {
  background-color: $logo-color;
  // outline: #0008ff solid;
  color: white;
}
</style>
