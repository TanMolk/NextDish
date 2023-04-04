<template>
  <select
      class="cuisine-select-wrapper"
      v-model="selectedOptionValue"
      @change="optionChange()"
  >
    <option value="" disabled selected hidden>Filter by</option>
    <option
        class="cuisine-select-option"
        v-for="type in types">
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
select{
  height: 45px;
  width: 180px;
  border-radius: 5px;
  border-width: 0;
  background: #0050F8;
  color: white;
  text-align: center;
  font-size: 1.2em;
  font-family: sans-serif;
  font-weight: 400;
  -webkit-appearance: none;
}

select option{
  background: #0050F8;
  text-align: center;
  border-radius: 1em;
}

select:focus{
  outline: none;
}


@media screen and (min-width: 480px) {
  .cuisine-select-wrapper {
    width: 15%;
  }
  select:focus{
    background: #A4B0BE 70%;
  }
}

@media screen and (max-width: 480px) {
  .cuisine-select-wrapper{
    text-align: center;
  }
  .cuisine-select-option{
    text-align: center;
  }
}
</style>