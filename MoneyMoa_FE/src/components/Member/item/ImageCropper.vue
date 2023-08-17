<template>
  <v-col cols="12" align="center">
    <v-sheet max-width="500">
      <cropper
        class="cropper"
        ref="cropperRef"
        :src="previewURL"
        :minWidth="100"
        :minHeight="100"
        :stencil-props="{
          aspectRatio: 1
        }"
      />
    </v-sheet>
  </v-col>

  <v-col class="text-center" cols="12">
    <v-btn role="button" @click="cropImage" class="Btns">자르기</v-btn>
    <v-btn role="button" @click="$emit('cancel')" class="ms-3 Btns" color="red lighten-5"
      >취소</v-btn
    >
  </v-col>
</template>
<script setup>
// 유저 이미지로 바꿔야함
import { ref, computed, defineProps, defineEmits } from 'vue'
import { Cropper } from 'vue-advanced-cropper'
import 'vue-advanced-cropper/dist/style.css'

const emits = defineEmits(['cancel', 'cropimage'])

const props = defineProps({
  previewURL: String
})
const UploadImg = ref(null)

const isChanged = computed(() => {
  return UploadImg.value && UploadImg.value.length > 0
})

// 유효성 검사후 수정 로직
async function onUpdate() {
  const jsonBlob = new Blob([JSON.stringify(member)], { type: 'application/json' })

  const data = new FormData()
  if (UploadImg.value && UploadImg.value.length > 0) {
    console.log(uploadedImage.value)
    data.append('file', uploadedImage.value)
  }
  data.append('MemberUpdateInfo', jsonBlob)
}

const cropperRef = ref(null)
const uploadedImage = ref(null)

function cropImage() {
  const canvas = cropperRef.value.getResult().canvas
  canvas.toBlob((blob) => {
    emits('cropimage', blob)
  })
}
</script>
<style scoped lang="scss">
textarea {
  resize: none;
}
.Btns {
  color: white;
  background-color: $primary-color;
  border-radius: 20px;
}
.title-left {
  text-align: left;
}
.avatar-image {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 150%;
  height: 150%;
  object-fit: cover;
}
.cropper {
  border: solid 1px #eee;
  background: white;
  min-width: 200px;
  max-width: 500px;
  max-height: 500px;
}
</style>
