<template>
  <select
      class="cuisine-select-wrapper"
      v-model="selectedOptionValue"
      @change="optionChange()"
  >
    <option disabled value="">Filter by</option>
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
      selectedOptionValue: ''
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
select {
  height: 3em;
  width: 6em;
  border-radius: 1em;
  text-align-last: center;
  font-size: 1em;
  background: coral;
  color: white;
}

@media screen and (min-width: 480px) {
  .cuisine-select-wrapper {
    width: 10%;
  }
}
</style>