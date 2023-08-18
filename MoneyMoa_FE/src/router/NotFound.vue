<template>
  <v-sheet class="not-found">
    <div class="message"><strong>404 Page Not Found!</strong></div>
    <p class="mt-10 error">
      죄송합니다. 페이지를 찾을 수 없습니다. <br />
      존재하지 않는 주소를 입력하셨거나<br />
      요청하신 페이지의 주소가 변경, 삭제되어 찾을 수 없습니다.
    </p>
    <img :src="img" width="500" />
  </v-sheet>
</template>
<script setup>
import img from '../assets/img/crying_moa.png'
</script>
<style lang="scss" scoped>
.not-found {
  line-height: 1.2;
  text-align: center;
  font-family: 'Oswald', sans-serif;
  padding: 80px 20px;

  .message {
    font-size: 50px;
  }
  .error {
    font-family: $body-font-family, 'Apple SD Gothic Neo';
  }
}
</style>
