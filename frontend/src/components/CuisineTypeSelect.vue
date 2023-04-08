<!--
 @Name CuisineTypeSelect
 @Description Filter of cuisine
 @author Ashton
 @createDate 2023/03/12
-->
<template>
  <el-select
      placeholder="Filter by"
      v-model="selectedOptionValue"
      @change="optionChange"
  >
    <el-option
        v-for="type in types"
        :key="type"
        :label="type"
        :value="type"
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
    onMouseDown(){
      this.isFocused = true;
    },
    onBlur(event){
      if(!event.relatedTarget || !event.relatedTarget.classList.contains('custom-option')){
      this.isFocused = false
      }
    },
    optionChange() {
      this.$emit("option-change", this.selectedOptionValue);
      // this.isFocused = false;
    }
  },
  created() {
    //change style
    document.body.style.setProperty('--el-bg-color-overlay', 'var(--application-normal-background-color)');
    document.body.style.setProperty('--el-text-color-regular', '#FFFFFF');
    document.body.style.setProperty('--el-fill-color-light', '#3c5b9d');
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
}
</style>