<template>
  <div class="cropper-container">
    <div class="cropper">
      <img ref="image" :src="imageSrc" class="cropper-image" alt="image" />
    </div>
    <div class="cropper-preview">
      <h4 class="cropper-preview-title">미리보기</h4>
      <div class="preview" ref="preview"></div>
    </div>
    <div class="cropper-controls">
      <button @click="zoomIn" class="cropper-button">확대</button>
      <button @click="zoomOut" class="cropper-button">축소</button>
      <button @click="rotateLeft" class="cropper-button">왼쪽 회전</button>
      <button @click="rotateRight" class="cropper-button">오른쪽 회전</button>
    </div>
    <div class="cropper-actions">
      <button @click="crop" class="cropper-button">자르기</button>
      <button @click="cancel" class="cropper-button">취소</button>
    </div>
  </div>
</template>

<script>
import Cropper from 'cropperjs'
import 'cropperjs/dist/cropper.css'

export default {
  name: 'CropperImage',
  props: {
    imageSrc: String,
    onCrop: Function,
    onCancel: Function
  },
  mounted() {
    this.initCropper()
  },
  methods: {
    initCropper() {
      this.cropper = new Cropper(this.$refs.image, {
        aspectRatio: 1,
        viewMode: 2,
        dragMode: 'move',
        responsive: true,
        minCropBoxWidth: 100,
        minCropBoxHeight: 100
      })
    },
    zoomIn() {
      this.cropper.zoom(0.1)
    },
    zoomOut() {
      this.cropper.zoom(-0.1)
    },
    rotateLeft() {
      this.cropper.rotate(-90)
    },
    rotateRight() {
      this.cropper.rotate(90)
    },
    crop() {
      const croppedCanvas = this.cropper.getCroppedCanvas()
      this.onCrop(croppedCanvas.toDataURL())
    },
    cancel() {
      this.onCancel()
    }
  },
  data() {
    return {
      cropper: null
    }
  }
}
</script>

<style scoped>
.cropper-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
}

.cropper {
  width: 100%;
  max-width: 500px;
  border: 2px solid #ccc;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}

.cropper-image {
  max-width: 100%;
}

.cropper-preview {
  margin-top: 16px;
  width: 100%;
  text-align: center;
}

.cropper-preview-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}

.preview {
  width: 100px;
  height: 100px;
  border: 2px solid #ccc;
  border-radius: 8px;
  margin: 0 auto;
}

.cropper-controls,
.cropper-actions {
  margin-top: 16px;
}

.cropper-button {
  padding: 8px 16px;
  margin-right: 8px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
}

.cropper-button:last-child {
  margin-right: 0;
}
</style>
