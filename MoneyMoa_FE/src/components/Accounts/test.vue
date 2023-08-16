<template>
  <div>
    <img id="image" src="picture.jpg" />
  </div>
</template>
<script setup>
import Cropper from 'cropperjs'

const image = document.getElementById('image')
const cropper = new Cropper(image, {
  aspectRatio: 16 / 9,
  crop(event) {
    console.log(event.detail.x)
    console.log(event.detail.y)
    console.log(event.detail.width)
    console.log(event.detail.height)
    console.log(event.detail.rotate)
    console.log(event.detail.scaleX)
    console.log(event.detail.scaleY)
  }
})
</script>
<style lang="scss">
img {
  display: block;

  /* This rule is very important, please don't ignore this */
  max-width: 100%;
}
</style>
