<template>
  <v-sheet class="mx-auto">
    <h5>증권사 선택</h5>
    <v-btn variant="plain" @click="selectAllBank(true)"> 모두 선택 </v-btn
    ><v-btn variant="plain" @click="cancelAllBank(true)"> 모두 선택 해제 </v-btn>
    <v-slide-group multiple show-arrows>
      <v-slide-group-item v-for="security in securityList" :key="securityList">
        <v-btn
          class="ma-2"
          rounded
          :color="security.selected ? 'primary' : undefined"
          @click="toggleSelected(security)"
        >
          <v-img
            v-if="icons[security.name]"
            :src="icons[security.name].default"
            class="fin-icon"
          ></v-img>
          {{ security.name }}
        </v-btn>
      </v-slide-group-item>
    </v-slide-group>
  </v-sheet>
</template>
<script setup>
import { reactive } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { loadSecuIcons } from '@/api/icons'
import { storeToRefs } from 'pinia'
const store = useProductStore()
const { securityList } = storeToRefs(store)
const { selectAllBank, cancelAllBank } = store

const icons = reactive({})
loadSecuIcons().then((secuIcons) => {
  Object.assign(icons, secuIcons)
  // console.log(icons)
})

const toggleSelected = (security) => {
  security.selected = !security.selected
}
</script>
<style scoped lang="scss"></style>
