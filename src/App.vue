<template>
  <v-app>
    <HeaderItem></HeaderItem>
    <div></div>
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
import HeaderItem from './components/common/Header.vue'
export default {
  name: 'App',
  components: {
    HeaderItem
  },
  data: () => ({
    //
  })
}
</script>
