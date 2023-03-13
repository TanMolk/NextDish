<template>
  <div class="wrapper"
       @click="clickItem()"
  >
    <div class="img"></div>
    <div class="info">
      <div class="info-text">
        {{ info.name}}
        <br>
        <br>
        {{ info.types}}
      </div>
    </div>
    <div class="mark">
      🌟{{ info.rating }}
    </div>
  </div>
</template>

<script>
export default {
  name: "CuisineListItem",
  props: {
    info: Object
  },
  methods: {
    clickItem() {
      this.$emit("cuisine-item-clicked")
    }
  }
}
</script>

<style scoped>
.wrapper {
  display: grid;
  grid-template-columns: repeat(3, 1fr );
  height: 5em;
  overflow: hidden;
}

.img {
  width: 76px;
  height: 76px;

  margin: auto auto;
  background-color: #bb43bb;
}

.info {
  margin: auto auto;
}

.info-text {
  margin: auto auto;
}

.mark {
  margin: auto auto;
}
</style>