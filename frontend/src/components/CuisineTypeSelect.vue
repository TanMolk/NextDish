<template>
  <select
      class="cuisine-select-wrapper"
      v-model="selectedOptionValue"
      @change="optionChange()"
  >
    <option v-for="type in types">
      {{ type }}
    </option>
  </select>
</template>

<script>
export default {
  name: "CuisineTypeSelect",
  data() {
    return {
      types: ["All", "American", "British", "Chinese", "Indian", "Italian", "Thai"],
      selectedOptionValue: "All"
    }
  },
  methods: {
    optionChange() {
      this.$emit("option-change", this.selectedOptionValue);
    }
  }
}
</script>

<style scoped>
.cuisine-select-wrapper {
  height: 3em;
  width: 5em;
  border-radius: 1em;
}

@media screen and (min-width: 480px) {
  .cuisine-select-wrapper {
    width: 10%;
  }
}
</style>