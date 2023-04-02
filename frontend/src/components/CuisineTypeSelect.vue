<template>
  <select
      class="cuisine-select-wrapper"
      v-model="selectedOptionValue"
      @change="optionChange()"
  >
    <option value="" disabled selected hidden>Filter by</option>
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
select, select option{
  height: 3em;
  width: 6em;
  border-radius: 3em;
  border-width: 0;
  text-align: center;
  font-size: 1em;
  background: coral;
  color: white;
  font-family: sans-serif;
  font-weight: bold;
  outline: none;
}

@media screen and (min-width: 480px) {
  .cuisine-select-wrapper {
    width: 15%;
  }
  select:hover{
    background: #e17350;
  }
}

@media screen and (max-width: 480px) {

}
</style>