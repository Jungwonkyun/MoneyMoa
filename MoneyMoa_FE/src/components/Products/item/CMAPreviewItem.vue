<template>
  <v-container @click="setProduct(product)">
    <!-- CMA는 productCode 대신 id 쓸거임 -->
    <router-link
      :to="{
        name: 'cmaDetail',
        params: { id: product.id }
      }"
    >
      <v-card variant="tonal">
        <v-row>
          <v-col>
            <v-card-item>
              <v-card-subtitle>
                <v-icon icon="mdi-face" />
                {{ product.stockName }}
              </v-card-subtitle>
              <v-card-title>
                {{ product.cmaName }}
              </v-card-title>
            </v-card-item>
          </v-col>
          <!-- <v-col cols="2" align-self="center"> {{ product.interest }}% </v-col> -->
        </v-row>
      </v-card>
    </router-link>
  </v-container>
</template>
<script setup>
defineProps({
  product: Object
})
</script>
<style></style>
