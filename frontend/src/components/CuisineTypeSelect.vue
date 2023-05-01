<!--
 @Name CuisineTypeSelect
 @Description Filter of cuisine
 @author Ashton
 @createDate 2023/03/12
-->
<template>
  <div class="select-wrapper">
    <el-select
        ref="elSelect"
        :popper-class="guidanceMode ? 'custom-select-option v-tour__target--highlighted' : 'custom-select-option'"
        placeholder="Filter by"
        v-model="selectedOptionValue"
        @change="optionChange"
        @click="this.$emit('click')"
    >
      <el-option
          v-for="type in types"
          :key="type"
          :label="type"
          :value="type"
          @click="type === selectedOptionValue ? this.$emit('option-click', type) : ''"
      />
    </el-select>
  </div>
</template>

<script>
export default {
  name: "CuisineTypeSelect",
  props: {
    guidanceMode: Boolean
  },
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
    makeSelectBlur() {
      this.$refs.elSelect.blur();
    }
  }
}
</script>

<style scoped>
/*Change default select style*/
.select-wrapper:deep(.el-input__wrapper) {
  width: 9em;
  height: 2.5em;
  --el-input-bg-color: var(--application-normal-background-color);
  --el-input-placeholder-color: #FFFFFF;
  --el-input-text-color: #FFFFFF;
}

.select-wrapper:deep(.el-select .el-input.is-focus .el-input__wrapper) {
  --el-input-bg-color: #A4B0BEB2;
}
</style>