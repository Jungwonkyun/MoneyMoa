<template>
  <v-container>
    <v-row>
      <v-col cols="6">
        <UserInfo :memberId="$route.params.id" />
      </v-col>
      <v-col cols="6">
        <router-view />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import UserInfo from '@/components/Member/UserInfo.vue'

export default {
  components: {
    UserInfo
  }
}
</script>

<style></style>
