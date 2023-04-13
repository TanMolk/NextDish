<!--
 @Name CuisineTypeSelect
 @Description Filter of cuisine
 @author Ashton
 @createDate 2023/03/12
-->
<template>
  <el-select
      ref="elSelect"
      popper-class="custom-select-option"
      placeholder="Filter by"
      v-model="selectedOptionValue"
      @change="optionChange"
  >
    <el-option
        v-for="type in types"
        :key="type"
        :label="type"
        :value="type"
        @click="this.$emit('option-click')"
    />
  </el-select>
</template>

<script>
export default {
  name: "CuisineTypeSelect",
  data() {
    return {
      types: ["All", "American", "British", "Chinese", "Indian", "Italian", "Thai"],
      selectedOptionValue: '',
      isFocused: false,
    }
  },
  methods: {
    optionChange() {
      this.$emit("option-change", this.selectedOptionValue);
    },
    makeSelectBlur(){
      this.$refs.elSelect.blur();
    }
  }
}
</script>

<style>
/*Change default select style*/
.el-input__wrapper {
  width: 9em;
  height: 2.5em;
  --el-input-bg-color: var(--application-normal-background-color);
  --el-input-placeholder-color: #FFFFFF;
  --el-input-text-color: #FFFFFF;
}

.el-select .el-input.is-focus .el-input__wrapper {
  --el-input-bg-color: #A4B0BEB2;
}

.custom-select-option {
  --el-bg-color-overlay: var(--application-normal-background-color);
  --el-text-color-regular: #FFFFFF;
  --el-fill-color-light: #3c5b9d;
}
</style>