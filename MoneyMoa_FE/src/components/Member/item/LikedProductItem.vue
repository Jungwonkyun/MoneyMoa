<template>
  <v-container>
    <router-link :to="getDetailRoute(liked.productType)">
      <v-card variant="flat" class="product-card product-preview">
        <v-row class="justify-center">
          <v-col>
            <v-card-item>
              <v-card-subtitle>
                <v-icon icon="mdi-face" />
                {{ liked.bankName }}
              </v-card-subtitle>
              <v-card-title>
                {{ liked.productName }}
              </v-card-title>
            </v-card-item>
          </v-col>
          <v-col cols="2" align-self="center">{{ liked.result }}원 받아용 </v-col>
          <v-col cols="2"><v-btn icon="mdi-trash-can-outline" variant="plain"></v-btn></v-col>
        </v-row>
      </v-card>
    </router-link>
  </v-container>
</template>
<script setup>
const props = defineProps({
  liked: Object
})
const getDetailRoute = (type) => {
  if (type === 'deposit') {
    return {
      name: 'depositDetail',
      params: { productCode: props.liked.productCode }
    }
  } else if (type === 'saving') {
    return {
      name: 'savingDetail',
      params: { productCode: props.liked.productCode }
    }
  } else {
    return {
      name: 'cmaDetail',
      params: { id: props.liked.id }
    }
  }
}
</script>
<style scoped lang="scss"></style>
