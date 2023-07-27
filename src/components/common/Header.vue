<template>
  <div>
    <v-toolbar>
      <v-toolbar-title><router-link to="/">MoneyMoa</router-link></v-toolbar-title>
      <v-btn><router-link to="/products">내 돈 굴리기</router-link></v-btn>
      <v-btn><router-link to="/challenge">챌린지</router-link></v-btn>
      <v-btn><router-link to="/feed">피드</router-link></v-btn>
      <v-btn><router-link to="/dictionary">금융사전</router-link></v-btn>
      <v-spacer></v-spacer>
      <v-toolbar-items class="hidden-xs-only">
        <v-btn
          ><router-link to="/"><v-icon icon="mdi-bell-outline" /></router-link
        ></v-btn>
        <v-btn
          ><router-link to="/"><v-icon icon="mdi-send" /></router-link
        ></v-btn>
        <v-btn><router-link to="/member">프로필</router-link></v-btn>
      </v-toolbar-items>
    </v-toolbar>
  </div>
</template>
<script>
export default {
  name: 'HeaderItem'
}
</script>
<style></style>
