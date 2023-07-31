<template>
  <v-container>
    <v-row class="d-flex align-center flex-column">
      <v-col cols="lg-8">
        <router-link :to="{ name: 'profilechange' }">회원정보 변경(테스트용 링크)</router-link>
        <router-view />
      </v-col>
    </v-row>
  </v-container>
</template>
<script setup></script>
<style></style>
