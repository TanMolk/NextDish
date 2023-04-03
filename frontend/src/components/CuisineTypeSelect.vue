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
  height: 2.5em;
  width: 6em;
  border-radius: 3em;
  border-width: 0;
  background: #F38B0C;
  color: white;
  text-align: center;
  font-size: 1.2em;
  font-family: Inter;
  font-weight: 400;
}

@media screen and (min-width: 480px) {
  .cuisine-select-wrapper {
    width: 15%;
  }
  select:hover{
    background: #d76f0c;
  }
}

@media screen and (max-width: 480px) {

}
</style>