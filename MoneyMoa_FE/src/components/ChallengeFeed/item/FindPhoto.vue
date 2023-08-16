<template>
  <v-card class="mx-auto" width="700" height="700" :elevation="10">
    <v-card-title align="center" class="py-5">새 게시글 만들기</v-card-title>
    <v-divider class="border-opacity-20"></v-divider>

    <!-- 파일 업로드를 위한 input 요소 추가 -->
    <v-file-input
      prepend-icon="mdi-camera"
      label="컴퓨터에서 선택"
      variant="solo-filled"
      @change="handleFileUpload"
      class="input-btn"
    ></v-file-input>

    <!-- 사진 미리보기를 위한 img 요소 추가 -->
    <img
      v-if="previewImageUrl"
      :src="previewImageUrl"
      alt="Uploaded Image"
      style="max-width: 100%; max-height: 400px; margin: 20px auto; display: block"
    />

    <!-- 사진 업로드를 위한 버튼 추가 -->
  </v-card>
</template>

<script setup>
import { ref } from 'vue'

// Vue 데이터
const previewImageUrl = ref('')

// 파일 업로드를 처리하는 메서드
const handleFileUpload = (event) => {
  //event.target.files[0]를 통해 업로드한 파일 정보를 가져옵니다.
  const file = event.target.files[0]

  //FileReader 객체를 생성하여 파일을 읽습니다.
  const reader = new FileReader()

  //파일이 읽혔을 때 발생하는 onload 이벤트를 이용하여,
  //파일의 미리보기를 위한 base64 형태의 URL을 생성하고, previewImageUrl에 저장합니다.
  reader.onload = () => {
    previewImageUrl.value = reader.result
  }

  if (file) {
    // 파일을 읽어오고, onload 이벤트가 발생하여 미리보기를 업데이트합니다.
    reader.readAsDataURL(file)
  }
}
</script>

<style>
.input-btn {
  align-items: center;
  width: 30%;
  margin: 20px auto;
}
</style>
